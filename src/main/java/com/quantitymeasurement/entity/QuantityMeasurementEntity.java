package com.quantitymeasurement.entity;

import com.quantitymeasurement.model.QuantityModel;
import com.quantitymeasurement.unit.IMeasurable;


public class QuantityMeasurementEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    
    public QuantityMeasurementEntity(com.quantitymeasurement.entity.QuantityModel<IMeasurable> q1, com.quantitymeasurement.entity.QuantityModel<IMeasurable> q2, String string, String string2) {
        
    }

   
    public Double thisValue;
    public String thisUnit;
    public String thisMeasurementType;

    
    public Double thatValue;
    public String thatUnit;
    public String thatMeasurementType;

    
    public String operation;

    
    public Double resultValue;
    public String resultUnit;
    public String resultMeasurementType;

   
    public String resultString;

    
    public boolean isError;
    public String errorMessage;

    
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation,
            String result
    ) {
        this(thisQuantity, thatQuantity, operation);
        this.resultString = result;
    }

    
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation,
            QuantityModel<IMeasurable> result
    ) {
        this(thisQuantity, thatQuantity, operation);
        this.resultValue = result.getValue();
        this.resultUnit = result.getUnit().getUnitName();
        this.resultMeasurementType = result.getUnit().getMeasurementType();
    }

    
    public QuantityMeasurementEntity(
            com.quantitymeasurement.entity.QuantityModel<IMeasurable> q1,
            com.quantitymeasurement.entity.QuantityModel<IMeasurable> q2,
            String operation,
            String errorMessage,
            boolean isError
    ) {
        this(q1, q2, operation);
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    
    public QuantityMeasurementEntity(
            QuantityModel<IMeasurable> thisQuantity,
            QuantityModel<IMeasurable> thatQuantity,
            String operation
    ) {
        if (thisQuantity == null || thatQuantity == null) {
            throw new IllegalArgumentException("Quantities cannot be null");
        }

        this.thisValue = thisQuantity.getValue();
        this.thisUnit = thisQuantity.getUnit().getUnitName();
        this.thisMeasurementType = thisQuantity.getUnit().getMeasurementType();

        this.thatValue = thatQuantity.getValue();
        this.thatUnit = thatQuantity.getUnit().getUnitName();
        this.thatMeasurementType = thatQuantity.getUnit().getMeasurementType();

        this.operation = operation;
    }

    
    public QuantityMeasurementEntity(com.quantitymeasurement.entity.QuantityModel<IMeasurable> q1,
			com.quantitymeasurement.entity.QuantityModel<IMeasurable> q2, String string,
			com.quantitymeasurement.entity.QuantityModel<IMeasurable> result) {
		// TODO Auto-generated constructor stub
	}


	public QuantityMeasurementEntity(com.quantitymeasurement.entity.QuantityModel<IMeasurable> q1,
			com.quantitymeasurement.entity.QuantityModel<IMeasurable> q2, String operation2) {
		// TODO Auto-generated constructor stub
	}


	public QuantityMeasurementEntity() {
		// TODO Auto-generated constructor stub
	}


	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        QuantityMeasurementEntity other = (QuantityMeasurementEntity) obj;
        return Math.abs(this.thisValue - other.thisValue) < 1e-6
                && Math.abs(this.thatValue - other.thatValue) < 1e-6
                && this.thisUnit.equals(other.thisUnit)
                && this.thatUnit.equals(other.thatUnit)
                && this.operation.equals(other.operation);
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(isError ? "[ERROR] " : "[SUCCESS] ")
          .append("operation=").append(operation);

        sb.append(", operand1=")
          .append(thisValue).append(" ")
          .append(thisUnit).append(" ")
          .append(thisMeasurementType);

        sb.append(", operand2=")
          .append(thatValue).append(" ")
          .append(thatUnit).append(" ")
          .append(thatMeasurementType);

        if (isError) {
            sb.append(", message=").append(errorMessage);
        } else if (resultString != null && !resultString.isEmpty()) {
            sb.append(", result=").append(resultString);
        } else {
            sb.append(", result=")
              .append(resultValue).append(" ")
              .append(resultUnit).append(" ")
              .append(resultMeasurementType);
        }

        return sb.toString();
    }
}