/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.ServiceExemptionType;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

/**
 * @author jpvl
 */
public class InfoServiceExemptionCreditLine extends InfoDatePeriodBaseCreditLine {
    private ServiceExemptionType type;

    /**
     * @return Returns the type.
     */
    public ServiceExemptionType getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(ServiceExemptionType type) {
        this.type = type;
    }

    public void populateDomainObject(TeacherServiceExemption serviceExemptionCreditLine) {
        super.populateDomainObject(serviceExemptionCreditLine);
        serviceExemptionCreditLine.setType(getType());
    }

    public static InfoServiceExemptionCreditLine newInfoFromDomain(TeacherServiceExemption serviceExemptionCreditLine) {
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(serviceExemptionCreditLine.getTeacher());

        InfoServiceExemptionCreditLine infoCreditLine = new InfoServiceExemptionCreditLine();
        infoCreditLine.setEnd(serviceExemptionCreditLine.getEnd());
        infoCreditLine.setIdInternal(serviceExemptionCreditLine.getIdInternal());
        infoCreditLine.setStart(serviceExemptionCreditLine.getStart());
        infoCreditLine.setType(serviceExemptionCreditLine.getType());

        infoCreditLine.setInfoTeacher(infoTeacher);
        return infoCreditLine;
    }

}