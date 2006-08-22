package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.University;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class InfoUniversity extends InfoObject {

    private final University university;

    public InfoUniversity(final University university) {
    	this.university = university;
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoUniversity && university == ((InfoUniversity) obj).university;
    }

    public String toString() {
    	return university.toString();
    }

    public String getCode() {
        return university.getCode();
    }

    public String getName() {
        return university.getName();
    }

    @Override
    public Integer getIdInternal() {
        return university.getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}