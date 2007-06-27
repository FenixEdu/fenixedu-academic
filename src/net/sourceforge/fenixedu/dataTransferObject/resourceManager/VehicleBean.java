package net.sourceforge.fenixedu.dataTransferObject.resourceManager;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class VehicleBean implements Serializable {

    private String numberPlate;
    
    private String make;
    
    private String model;
    
    private YearMonthDay acquisition;
    
    private YearMonthDay cease;

    public VehicleBean() {
    }
    
    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public YearMonthDay getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(YearMonthDay acquisition) {
        this.acquisition = acquisition;
    }

    public YearMonthDay getCease() {
        return cease;
    }

    public void setCease(YearMonthDay cease) {
        this.cease = cease;
    }           
}
