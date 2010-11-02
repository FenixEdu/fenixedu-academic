package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil;

import java.io.Serializable;

public class NumberBean implements Serializable {

    private Integer number;
    private Integer degreeCurricularPeriod;
    public NumberBean() {
    }

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

    public Integer getDegreeCurricularPeriod() {
	return degreeCurricularPeriod;
    }

    public void setDegreeCurricularPeriod(Integer degreeCurricularPeriod) {
	this.degreeCurricularPeriod = degreeCurricularPeriod;
    }
}