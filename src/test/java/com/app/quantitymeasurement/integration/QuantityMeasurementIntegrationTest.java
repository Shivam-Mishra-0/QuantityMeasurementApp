package com.app.quantitymeasurement.integration;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityInputDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class QuantityMeasurementIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QuantityMeasurementRepository repository;

    private String baseUrl;

    private QuantityDTO feetDTO;
    private QuantityDTO inchesDTO;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/quantities";
        repository.deleteAll();

        feetDTO   = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        inchesDTO = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
    }

    @Test
    public void testSpringBootApplicationStarts() {
        
        assertNotNull(restTemplate);
        assertNotNull(repository);
    }

    @Test
    public void testRestEndpointCompareQuantities() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("compare", response.getBody().getOperation());
        assertEquals("true", response.getBody().getResultString());
        assertFalse(response.getBody().isError());
    }

    @Test
    public void testRestEndpointCompareQuantities_NotEqual() {
        QuantityDTO twoFeet = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityInputDTO input = new QuantityInputDTO(twoFeet, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("false", response.getBody().getResultString());
    }

    @Test
    public void testRestEndpointConvertQuantities() {
        QuantityDTO targetDTO = new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES);
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, targetDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/convert", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("convert", response.getBody().getOperation());
        assertEquals(12.0, response.getBody().getResultValue(), 1e-4);
        assertFalse(response.getBody().isError());
    }

    @Test
    public void testRestEndpointAddQuantities() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("add", response.getBody().getOperation());
        assertEquals(2.0, response.getBody().getResultValue(), 1e-4);
        assertEquals("FEET", response.getBody().getResultUnit());
        assertFalse(response.getBody().isError());
    }

    @Test
    public void testRestEndpointAddQuantities_WithTargetUnit() {
        QuantityDTO yardsTarget = new QuantityDTO(0.0, QuantityDTO.LengthUnit.YARDS);
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, yardsTarget);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/add", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("YARDS", response.getBody().getResultUnit());
        assertTrue(response.getBody().getResultValue() > 0.0);
    }

    @Test
    public void testRestEndpointSubtractQuantities() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/subtract", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("subtract", response.getBody().getOperation());
        assertEquals(0.0, response.getBody().getResultValue(), 1e-4);
    }

    @Test
    public void testRestEndpointDivideQuantities() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, feetDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/divide", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("divide", response.getBody().getOperation());
        assertEquals(1.0, response.getBody().getResultValue(), 1e-4);
    }

    @Test
    public void testRestEndpointInvalidInput_InvalidUnit_Returns400() {
        String badJson = "{"
            + "\"thisQuantityDTO\": {\"value\": 1.0, \"unit\": \"FOOT\", \"measurementType\": \"LengthUnit\"},"
            + "\"thatQuantityDTO\": {\"value\": 12.0, \"unit\": \"INCHE\", \"measurementType\": \"LengthUnit\"}"
            + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(badJson, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/compare", entity, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("status"));
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    public void testRestEndpointIncompatibleTypes_Returns400() {
        QuantityDTO kilogramDTO = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, kilogramDTO, null);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/add", input, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("message").toString().contains("LengthUnit"));
    }

    @Test
    public void testGetOperationHistory_AfterCompare_ReturnsRecord() {
        
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);
        
        ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(
            baseUrl + "/history/operation/compare",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals("compare", response.getBody().get(0).getOperation());
    }

    
    @Test
    public void testGetHistoryByType_AfterOperations_ReturnsMatchingRecords() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);
        restTemplate.postForEntity(baseUrl + "/add", input, QuantityMeasurementDTO.class);

        ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(
            baseUrl + "/history/type/LengthUnit",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() >= 2);
    }

    
    @Test
    public void testGetErrorHistory_AfterFailedOperation_ReturnsErrorRecord() {
        // Trigger an error by adding incompatible types
        QuantityDTO kilogramDTO = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, kilogramDTO, null);
        restTemplate.postForEntity(baseUrl + "/add", input, Map.class);

        ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(
            baseUrl + "/history/errored",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertTrue(response.getBody().get(0).isError());
    }

    @Test
    public void testGetOperationCount_AfterCompare_ReturnsCorrectCount() {
        // Perform two compare operations
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        ResponseEntity<Long> response = restTemplate.getForEntity(
            baseUrl + "/count/compare", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody());
    }

    @Test
    public void testJPARepositoryFindByOperation_ReturnsCorrectEntities() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/add", input, QuantityMeasurementDTO.class);

        List<QuantityMeasurementEntity> entities = repository.findByOperation("add");
        assertFalse(entities.isEmpty());
        assertEquals("add", entities.get(0).getOperation());
    }

    
    @Test
    public void testJPARepositoryFindByIsErrorTrue_ReturnsErrorEntities() {
        QuantityInputDTO goodInput = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/compare", goodInput, QuantityMeasurementDTO.class);

        QuantityDTO kilogramDTO = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityInputDTO badInput = new QuantityInputDTO(feetDTO, kilogramDTO, null);
        restTemplate.postForEntity(baseUrl + "/add", badInput, Map.class);

        List<QuantityMeasurementEntity> errors = repository.findByErrorTrue();
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().allMatch(QuantityMeasurementEntity::isError));
    }

    
    @Test
    public void testJPARepositoryCountByOperationAndIsErrorFalse() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        long count = repository.countByOperationAndErrorFalse("compare");
        assertEquals(2L, count);
    }

    @Test
    public void testActuatorHealthEndpoint_ReturnsUp() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/actuator/health", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
    }

    @Test
    public void testH2ConsoleLaunches() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/h2-console", String.class);

        
        assertNotEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSwaggerUILoads() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/swagger-ui.html", String.class);

        assertNotEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testOpenAPIDocumentation_Returns200() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/v3/api-docs", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("openapi") || response.getBody().contains("paths"));
    }

    @Test
    public void testContentNegotiation_ResponseIsJSON() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentType().toString()
            .contains(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testIntegrationTest_MultipleOperations_AllPersisted() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        QuantityDTO targetInches = new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES);
        QuantityInputDTO convertInput = new QuantityInputDTO(feetDTO, targetInches, null);
        restTemplate.postForEntity(baseUrl + "/convert", convertInput, QuantityMeasurementDTO.class);

        restTemplate.postForEntity(baseUrl + "/add", input, QuantityMeasurementDTO.class);

        // Verify all 3 records are in the database
        List<QuantityMeasurementEntity> all = repository.findAll();
        assertEquals(3, all.size());
    }
}