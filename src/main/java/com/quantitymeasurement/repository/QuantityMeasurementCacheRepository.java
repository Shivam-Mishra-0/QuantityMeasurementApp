package com.quantitymeasurement.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.quantitymeasurement.entity.QuantityMeasurementEntity;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    
    private static final Logger logger = Logger.getLogger(
        QuantityMeasurementCacheRepository.class.getName()
    );

    
    static class AppendableObjectOutputStream extends ObjectOutputStream {

        
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        
        @Override
        protected void writeStreamHeader() throws IOException {
            File file = new File(QuantityMeasurementCacheRepository.FILE_NAME);
            if (!file.exists() || file.length() == 0) {
                super.writeStreamHeader();
            } else {
                reset();
            }
        }
    }

    
    public static final String FILE_NAME = "data/quantity_measurement_repo.ser";

    
    private List<QuantityMeasurementEntity> quantityMeasurementEntityCache;

    
    private static QuantityMeasurementCacheRepository instance;

    
    private QuantityMeasurementCacheRepository() {
        quantityMeasurementEntityCache = new ArrayList<>();
        loadFromDisk();
        logger.info("QuantityMeasurementCacheRepository initialized with "
            + quantityMeasurementEntityCache.size() + " cached entities.");
    }

    
    public static QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    
    @Override
    public void save(QuantityMeasurementEntity entity) {
        quantityMeasurementEntityCache.add(entity);
        saveToDisk(entity);
        logger.fine("Entity saved to cache and disk: " + entity.operation);
    }

    
    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return quantityMeasurementEntityCache;
    }

    
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return quantityMeasurementEntityCache.stream()
            .filter(e -> operation.equalsIgnoreCase(e.operation))
            .collect(Collectors.toList());
    }

    
    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        return quantityMeasurementEntityCache.stream()
            .filter(e -> measurementType.equalsIgnoreCase(e.thisMeasurementType))
            .collect(Collectors.toList());
    }

    
    @Override
    public int getTotalCount() {
        return quantityMeasurementEntityCache.size();
    }

    
    @Override
    public void deleteAll() {
        quantityMeasurementEntityCache.clear();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        logger.info("All measurements cleared from cache repository.");
    }

   
    @Override
    public String getPoolStatistics() {
        return "CacheRepository: no connection pool. Cache size=" + quantityMeasurementEntityCache.size();
    }

    
    private void saveToDisk(QuantityMeasurementEntity entity) {
        try (
            FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
            AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos)
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            logger.severe("Error saving entity to disk: " + e.getMessage());
        }
    }

    
    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity =
                        (QuantityMeasurementEntity) ois.readObject();
                    quantityMeasurementEntityCache.add(entity);
                } catch (EOFException e) {
                    break;
                }
            }
            logger.info("Loaded " + quantityMeasurementEntityCache.size()
                + " entities from disk.");
        } catch (IOException | ClassNotFoundException ex) {
            logger.severe("Error loading entities from disk: " + ex.getMessage());
        }
    }
}
