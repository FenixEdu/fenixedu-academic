/*
 * Created on 18/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Attends;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan extends InfoFrequenta {

    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setAluno(InfoStudent.newInfoFromDomain(frequenta.getAluno()));
            setDisciplinaExecucao(InfoExecutionCourse.newInfoFromDomain(frequenta
                    .getExecutionCourse()));
            setInfoEnrolment(InfoEnrolment.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(Attends frequenta) {
        InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan infoFrequenta = null;
        if (frequenta != null) {
            infoFrequenta = new InfoFrequentaWithAllAndInfoStudentPlanAndInfoDegreePlan();
            infoFrequenta.copyFromDomain(frequenta);
        }

        return infoFrequenta;
    }
}