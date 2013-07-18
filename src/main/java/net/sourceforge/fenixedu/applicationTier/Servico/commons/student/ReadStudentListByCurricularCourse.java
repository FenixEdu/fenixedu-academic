package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentListByCurricularCourseAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentListByCurricularCourse {

    protected List run(final IUserView userView, final Integer curricularCourseID, final String executionYear)
            throws FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseID);
        return (executionYear != null) ? cleanList(curricularCourse.getEnrolmentsByYear(executionYear)) : cleanList(curricularCourse
                .getEnrolments());
    }

    private List cleanList(final List<Enrolment> enrolmentList) throws FenixServiceException {

        if (enrolmentList.isEmpty()) {
            throw new NonExistingServiceException();
        }

        Integer studentNumber = null;
        final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        for (final Enrolment enrolment : enrolmentList) {

            if (studentNumber == null
                    || studentNumber.intValue() != enrolment.getStudentCurricularPlan().getRegistration().getNumber().intValue()) {
                studentNumber = enrolment.getStudentCurricularPlan().getRegistration().getNumber();
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentListByCurricularCourse serviceInstance = new ReadStudentListByCurricularCourse();

    @Service
    public static List runReadStudentListByCurricularCourse(IUserView userView, Integer curricularCourseID, String executionYear) throws FenixServiceException  , NotAuthorizedException {
        StudentListByCurricularCourseAuthorizationFilter.instance.execute(userView, curricularCourseID, executionYear);
        return serviceInstance.run(userView, curricularCourseID, executionYear);
    }

}