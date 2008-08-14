/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoNonAffiliatedTeacher extends InfoObject {

    protected String name;

    protected Integer keyInstitution;

    protected InfoInstitution infoInstitution;

    public InfoNonAffiliatedTeacher() {
    }

    public void copyFromDomain(NonAffiliatedTeacher nonAffiliatedTeacher) {
	super.copyFromDomain(nonAffiliatedTeacher);
	if (nonAffiliatedTeacher != null) {
	    if (nonAffiliatedTeacher.getInstitutionUnit() != null) {
		InfoInstitution infoInstitution = new InfoInstitution();
		infoInstitution.copyFromDomain(nonAffiliatedTeacher.getInstitutionUnit());
		setInfoInstitution(infoInstitution);
	    }
	    setName(nonAffiliatedTeacher.getName());
	}
    }

    public static InfoNonAffiliatedTeacher newInfoFromDomain(NonAffiliatedTeacher naTeacher) {
	InfoNonAffiliatedTeacher infoNaTeacher = null;
	if (naTeacher != null) {
	    infoNaTeacher = new InfoNonAffiliatedTeacher();
	    infoNaTeacher.copyFromDomain(naTeacher);
	}
	return infoNaTeacher;
    }

    public InfoInstitution getInfoInstitution() {
	return infoInstitution;
    }

    public void setInfoInstitution(InfoInstitution infoInstitution) {
	this.infoInstitution = infoInstitution;
    }

    public Integer getKeyInstitution() {
	return keyInstitution;
    }

    public void setKeyInstitution(Integer keyInstitution) {
	this.keyInstitution = keyInstitution;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	String result = "[INFONONAFFILIATEDTEACHER";
	result += ", nome=" + this.getName();
	result += "]";
	return result;
    }

}
