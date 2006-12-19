/*
 * Created on 7/Mai/2005 - 13:54:46
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadAttendsByStudentIdAndExecutionPeriodId extends Service {

    public List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers> run(Integer studentId, Integer executionPeriodId,
            Boolean onlyEnrolledCourses, Boolean onlyAttendsWithTeachers) throws ExcepcaoPersistencia {

        final List<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers> infoAttendsList = new ArrayList<InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers>();

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        final Registration someRegistration = rootDomainObject.readRegistrationByOID(studentId);
        for (final Registration registration : someRegistration.getStudent().getRegistrationsSet()) {
            for (final Attends attends : registration.getAssociatedAttendsSet()) {
                final ExecutionCourse executionCourse = attends.getDisciplinaExecucao();
                if (executionCourse.getExecutionPeriod() == executionPeriod
                        && (!(onlyEnrolledCourses.booleanValue() && (attends.getEnrolment() == null)))) {
                    if(!onlyAttendsWithTeachers) {
                        infoAttendsList.add(InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends));
                    } else if((!attends.getDisciplinaExecucao().getProfessorships().isEmpty()) || 
                            (!attends.getDisciplinaExecucao().getNonAffiliatedTeachers().isEmpty())) {
                        infoAttendsList.add(InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends));
                    }
                }
            }
        }

        return infoAttendsList;
    }

}
