/*
 * Created on Dec 9, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Attends;

/**
 * @author André Fernandes / João Brito
 */
public class InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment extends
        InfoFrequentaWithInfoStudentAndPerson
{
    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setInfoEnrolment(InfoEnrolment.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(Attends attend) {
        InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }

}
