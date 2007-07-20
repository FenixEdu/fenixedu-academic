package net.sourceforge.fenixedu.dataTransferObject.commons.student;

import java.io.Serializable;

public class StudentNumberBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6389292009798394845L;

    private Integer number;

    public Integer getNumber() {
	return number;
    }

    public void setNumber(Integer number) {
	this.number = number;
    }

}
