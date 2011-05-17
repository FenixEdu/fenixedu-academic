package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil;

import java.io.Serializable;

public class NumberBean implements Serializable {

    private Integer number;
    private String id;
    private Integer degreeCurricularPeriod;

    public NumberBean() {
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Integer getDegreeCurricularPeriod() {
	return degreeCurricularPeriod;
    }

    public void setDegreeCurricularPeriod(Integer degreeCurricularPeriod) {
	this.degreeCurricularPeriod = degreeCurricularPeriod;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

    public Integer getNumber() {
	return number;
    }
}