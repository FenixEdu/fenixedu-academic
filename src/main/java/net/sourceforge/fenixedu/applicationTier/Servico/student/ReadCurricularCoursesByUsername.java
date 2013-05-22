/*
 * Created on 3/Ago/2003, 21:37:27
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 *         This service reads all the Curricular Courses for this student's
 *         curricular plans, and not only the Curricular Courses currently in
 *         execution. For this case use ReadDisciplinesByStudent Created at
 *         3/Ago/2003, 21:37:27
 * 
 */
public class ReadCurricularCoursesByUsername extends FenixService {

    protected List run(String username) throws BDException, NonExistingServiceException {
        List curricularCourses = new LinkedList();

        Registration registration = Registration.readByUsername(username);
        if (registration == null) {
            throw new NonExistingServiceException();
        }
        List<StudentCurricularPlan> curricularPlans = registration.getStudentCurricularPlans();
        for (Object element : curricularPlans) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) element;
            for (Object element2 : studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourses()) {
                CurricularCourse curricularCourse = (CurricularCourse) element2;
                curricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
            }
        }

        return curricularCourses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCurricularCoursesByUsername serviceInstance = new ReadCurricularCoursesByUsername();

    @Service
    public static List runReadCurricularCoursesByUsername(String username) throws BDException, NonExistingServiceException,
            NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run(username);
    }

}