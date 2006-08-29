package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.University;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class InfoUniversity extends InfoObject {

    private final DomainReference<University> university;

    public InfoUniversity(final University university) {
    	this.university = new DomainReference<University>(university);
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoUniversity && university == ((InfoUniversity) obj).university;
    }

    public String toString() {
    	return getUniversity().toString();
    }

    public String getCode() {
        return getUniversity().getCode();
    }

    public String getName() {
        return getUniversity().getName();
    }

    @Override
    public Integer getIdInternal() {
        return getUniversity().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private University getUniversity() {
        return university == null ? null : university.getObject();
    }

}