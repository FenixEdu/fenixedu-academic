/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ProfessionalSituationType;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

/**
 * @author jpvl
 */
public class InfoServiceExemptionCreditLine extends InfoDatePeriodBaseCreditLine {
    private ProfessionalSituationType type;

    /**
     * @return Returns the type.
     */
    public ProfessionalSituationType getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(ProfessionalSituationType type) {
        this.type = type;
    }

    public void populateDomainObject(TeacherServiceExemption serviceExemptionCreditLine) {
        super.populateDomainObject(serviceExemptionCreditLine);
        serviceExemptionCreditLine.setSituationType(getType());
    }

    public static InfoServiceExemptionCreditLine newInfoFromDomain(TeacherServiceExemption serviceExemptionCreditLine) {
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(serviceExemptionCreditLine.getTeacher());

        InfoServiceExemptionCreditLine infoCreditLine = new InfoServiceExemptionCreditLine();
        infoCreditLine.setEnd(serviceExemptionCreditLine.getEndDate());
        infoCreditLine.setIdInternal(serviceExemptionCreditLine.getIdInternal());
        infoCreditLine.setStart(serviceExemptionCreditLine.getBeginDate());
        infoCreditLine.setType(serviceExemptionCreditLine.getType());

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

}