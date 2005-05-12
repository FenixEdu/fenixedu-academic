/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoInstitution;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Institution;
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

    public void copyFromDomain(INonAffiliatedTeacher nonAffiliatedTeacher) {
        super.copyFromDomain(nonAffiliatedTeacher);
        if (nonAffiliatedTeacher != null) {
            if (nonAffiliatedTeacher.getInstitution() != null) {
                InfoInstitution infoInstitution = new InfoInstitution();
                infoInstitution.copyFromDomain(nonAffiliatedTeacher.getInstitution());
                setInfoInstitution(infoInstitution);
            }
            setName(nonAffiliatedTeacher.getName());
        }
    }

	public static InfoNonAffiliatedTeacher newInfoFromDomain(INonAffiliatedTeacher naTeacher) {
        InfoNonAffiliatedTeacher infoNaTeacher = null;
        if (naTeacher != null) {
            infoNaTeacher = new InfoNonAffiliatedTeacher();
            infoNaTeacher.copyFromDomain(naTeacher);
        }
        return infoNaTeacher;
    }

    public static INonAffiliatedTeacher newDomainFromInfo(InfoNonAffiliatedTeacher infoNonAffiliatedTeacher) {
        INonAffiliatedTeacher nonAffiliatedTeacher = null;
        if (infoNonAffiliatedTeacher != null) {
            nonAffiliatedTeacher = new NonAffiliatedTeacher();
            infoNonAffiliatedTeacher.copyToDomain(infoNonAffiliatedTeacher, nonAffiliatedTeacher);
        }
        return nonAffiliatedTeacher;
    }

    public void copyToDomain(InfoNonAffiliatedTeacher infoNonAffiliatedTeacher, INonAffiliatedTeacher nonAffiliatedTeacher) {
        if (infoNonAffiliatedTeacher != null && nonAffiliatedTeacher != null) {
            super.copyToDomain(infoNonAffiliatedTeacher, nonAffiliatedTeacher);
            nonAffiliatedTeacher.setName(infoNonAffiliatedTeacher.getName());
            InfoInstitution infoInstitution = infoNonAffiliatedTeacher.getInfoInstitution();
            if (infoInstitution != null){
                IInstitution institution = new Institution();
                infoInstitution.copyToDomain(infoInstitution, institution);
                nonAffiliatedTeacher.setInstitution(institution);
            }
        }
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
	
    public String toString() {
        String result = "[INFONONAFFILIATEDTEACHER";
        result += ", nome=" + this.getName();
        result += "]";
        return result;
    }


}
