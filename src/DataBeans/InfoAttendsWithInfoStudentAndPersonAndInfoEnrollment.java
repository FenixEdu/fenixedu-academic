/*
 * Created on Dec 9, 2004
 *
 */
package DataBeans;

import Dominio.IFrequenta;

/**
 * @author André Fernandes / João Brito
 */
public class InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment extends
        InfoFrequentaWithInfoStudentAndPerson
{
    public void copyFromDomain(IFrequenta frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setInfoEnrolment(InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IFrequenta attend) {
        InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }

}
