package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil;

import java.io.Serializable;

public class NumberBean implements Serializable {

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
}