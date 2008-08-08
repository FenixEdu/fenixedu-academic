package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil;

import java.io.Serializable;

public class TutorateBean implements Serializable {

	private Integer personNumber;
	private Integer degreeCurricularPeriod;
	
	public Integer getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(Integer studentNumber) {
		this.personNumber = studentNumber;
	}

	public Integer getDegreeCurricularPeriod() {
	    return degreeCurricularPeriod;
	}

	public void setDegreeCurricularPeriod(Integer degreeCurricularPeriod) {
	    this.degreeCurricularPeriod = degreeCurricularPeriod;
	}
	
}