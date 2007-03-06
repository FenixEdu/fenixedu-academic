/*
 * Created on 1/Jun/2005 - 13:26:47
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithAttendsAndInquiriesRegistries;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadStudentsWithAttendsByDegreeCurricularPlanAndExecutionPeriod extends Service {

    public List<InfoStudentWithAttendsAndInquiriesRegistries> run(final Integer degreeCurricularPlanId,
            final Integer executionPeriodId, final Boolean onlyAttendsWithTeachers)
            throws ExcepcaoPersistencia, FenixServiceException {

        if (degreeCurricularPlanId == null) {
            throw new FenixServiceException("nullDegreeCurricularPlanId");
        }
        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }
        if (onlyAttendsWithTeachers == null) {
            throw new FenixServiceException("nullOnlyAttendsWithTeachers");
        }

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
                .readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);

        List<Attends> attendsList = executionPeriod.getAttendsByDegreeCurricularPlan(degreeCurricularPlan);

        // FIXME: It only concerns enrolled courses
        List<InfoStudentWithAttendsAndInquiriesRegistries> res = new ArrayList<InfoStudentWithAttendsAndInquiriesRegistries>();
        InfoStudentWithAttendsAndInquiriesRegistries currentStudent = null;
        for (Attends attends : attendsList) {

            if ((!onlyAttendsWithTeachers)
                    || ((!attends.getExecutionCourse().getProfessorships().isEmpty()) || (!attends
                            .getExecutionCourse().getNonAffiliatedTeachers().isEmpty()))) {

                InfoFrequenta infoAttends = InfoFrequenta.newInfoFromDomain(attends);
                if (!infoAttends.getAluno().equals(currentStudent)) {
                    currentStudent = new InfoStudentWithAttendsAndInquiriesRegistries(attends.getRegistration());
                    currentStudent.setAttends(new ArrayList<InfoFrequenta>());
                    copyStudentInquiriesRegistries(currentStudent, attends.getRegistration());
                    res.add(currentStudent);
                }
                currentStudent.getAttends().add(infoAttends);

            }

        }

        return res;
    }

    private void copyStudentInquiriesRegistries(
            InfoStudentWithAttendsAndInquiriesRegistries currentStudent, Registration aluno) {

        List<InquiriesRegistry> inquiriesRegistries = aluno.getAssociatedInquiriesRegistries();
        List<InfoInquiriesRegistry> infoRegistries = new ArrayList<InfoInquiriesRegistry>(
                inquiriesRegistries.size());

        for (InquiriesRegistry reg : inquiriesRegistries) {
            infoRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(reg));
        }
        currentStudent.setInquiriesRegistries(infoRegistries);
    }

}
