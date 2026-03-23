package com.quantitymeasurement.exception;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementExceptionTest {

    

    @Test
    public void testExtendsRuntimeException() {
        assertTrue(new QuantityMeasurementException("msg") instanceof RuntimeException);
    }

    @Test
    public void testIsUnchecked_ExtendsRuntimeException() {
        assertTrue(RuntimeException.class.isAssignableFrom(
            QuantityMeasurementException.class));
        assertFalse(
            QuantityMeasurementException.class.getSuperclass().equals(Exception.class));
    }

    

    @Test
    public void testConstructor_Message_StoresMessage() {
        QuantityMeasurementException ex =
            new QuantityMeasurementException("Invalid unit provided");
        assertEquals("Invalid unit provided", ex.getMessage());
    }

    @Test
    public void testConstructor_Message_CauseIsNull() {
        QuantityMeasurementException ex =
            new QuantityMeasurementException("some error");
        assertNull(ex.getCause());
    }

    @Test
    public void testConstructor_EmptyMessage_Stored() {
        QuantityMeasurementException ex = new QuantityMeasurementException("");
        assertEquals("", ex.getMessage());
    }

    

    @Test
    public void testConstructor_MessageAndCause_StoresBoth() {
        Throwable cause = new IllegalArgumentException("root cause");
        QuantityMeasurementException ex =
            new QuantityMeasurementException("wrapper message", cause);
        assertEquals("wrapper message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    public void testConstructor_MessageAndCause_CauseMessageAccessible() {
        Throwable cause = new ArithmeticException("division by zero");
        QuantityMeasurementException ex =
            new QuantityMeasurementException("arithmetic error", cause);
        assertEquals("division by zero", ex.getCause().getMessage());
    }

    

    @Test
    public void testThrowAndCatch_MessageOnly() {
        QuantityMeasurementException caught = assertThrows(
            QuantityMeasurementException.class,
            () -> { throw new QuantityMeasurementException("test error"); }
        );
        assertEquals("test error", caught.getMessage());
    }

    @Test
    public void testThrowAndCatch_WithCause() {
        IllegalArgumentException root = new IllegalArgumentException("bad arg");
        QuantityMeasurementException caught = assertThrows(
            QuantityMeasurementException.class,
            () -> { throw new QuantityMeasurementException("wrapper", root); }
        );
        assertSame(root, caught.getCause());
    }

    @Test
    public void testCaughtAsRuntimeException() {
        
        assertThrows(
            RuntimeException.class,
            () -> { throw new QuantityMeasurementException("caught as RuntimeException"); }
        );
    }
}