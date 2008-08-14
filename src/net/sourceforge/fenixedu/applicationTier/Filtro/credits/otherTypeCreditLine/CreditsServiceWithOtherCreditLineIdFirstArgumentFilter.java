/*
 * Created on 1/Mar/2004
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.credits.otherTypeCreditLine;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.AbstractTeacherDepartmentAuthorization;
import net.sourceforge.fenixedu.domain.credits.OtherTypeCreditLine;

/**
 * @author jpvl
 */
public class CreditsServiceWithOtherCreditLineIdFirstArgumentFilter extends AbstractTeacherDepartmentAuthorization {

    protected Integer getTeacherId(Object[] arguments) {
	Integer id = (Integer) arguments[0];

	OtherTypeCreditLine otherTypeCreditLine = (OtherTypeCreditLine) rootDomainObject.readCreditLineByOID(id);
	return otherTypeCreditLine == null ? null : otherTypeCreditLine.getTeacher().getIdInternal();
    }

}
