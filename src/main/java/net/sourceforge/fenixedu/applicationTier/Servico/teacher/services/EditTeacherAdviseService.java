/**
 * Nov 28, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherAdviseService {

    protected void run(Teacher teacher, String executionPeriodID, final Integer studentNumber, Double percentage,
            AdviseType adviseType, RoleType roleType) throws FenixServiceException {

        ExecutionSemester executionSemester = FenixFramework.getDomainObject(executionPeriodID);

        Collection<Registration> students = Bennu.getInstance().getRegistrationsSet();
        Registration registration = (Registration) CollectionUtils.find(students, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Registration tempStudent = (Registration) arg0;
                return tempStudent.getNumber().equals(studentNumber);
            }
        });

        if (registration == null) {
            throw new FenixServiceException("errors.invalid.student-number");
        }

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionSemester);
        }
        List<Advise> advises = registration.getAdvisesByTeacher(teacher);
        Advise advise = null;
        if (advises == null || advises.isEmpty()) {
            advise = new Advise(teacher, registration, adviseType, executionSemester, executionSemester);
        } else {
            advise = advises.iterator().next();
        }

        TeacherAdviseService teacherAdviseService = advise.getTeacherAdviseServiceByExecutionPeriod(executionSemester);
        if (teacherAdviseService == null) {
            teacherAdviseService = new TeacherAdviseService(teacherService, advise, percentage, roleType);
        } else {
            teacherAdviseService.updatePercentage(percentage, roleType);
        }
    }

    // Service Invokers migrated from Berserk

    private static final EditTeacherAdviseService serviceInstance = new EditTeacherAdviseService();

    @Atomic
    public static void runEditTeacherAdviseService(Teacher teacher, String executionPeriodID, Integer studentNumber,
            Double percentage, AdviseType adviseType, RoleType roleType) throws FenixServiceException, NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(teacher, executionPeriodID, studentNumber, percentage, adviseType, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(teacher, executionPeriodID, studentNumber, percentage, adviseType, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(teacher, executionPeriodID, studentNumber, percentage, adviseType, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}