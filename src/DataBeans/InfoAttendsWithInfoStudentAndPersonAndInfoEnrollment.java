/*
 * Created on Dec 9, 2004
 *
 */
package DataBeans;

import Dominio.IAttends;

/**
 * @author André Fernandes / João Brito
 */
public class InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment extends
        InfoFrequentaWithInfoStudentAndPerson
{
    public void copyFromDomain(IAttends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setInfoEnrolment(InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(IAttends attend) {
        InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }

}
