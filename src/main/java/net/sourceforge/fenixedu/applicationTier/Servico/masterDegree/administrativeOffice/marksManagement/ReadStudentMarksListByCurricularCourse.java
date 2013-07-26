package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentListByCurricularCourseAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadStudentMarksListByCurricularCourse {

    protected List run(User userView, String curricularCourseID, String executionYear) throws ExcepcaoInexistente,
            FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseID);

        final List<Enrolment> enrolmentList =
                (executionYear != null) ? curricularCourse.getEnrolmentsByYear(executionYear) : curricularCourse.getEnrolments();
        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }
        return cleanList(enrolmentList);
    }

    private List cleanList(final List<Enrolment> enrolments) {
        List result = new ArrayList();
        Integer numberAux = null;

        for (final Enrolment enrolment : enrolments) {
            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getRegistration().getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getRegistration().getNumber();
                result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentMarksListByCurricularCourse serviceInstance = new ReadStudentMarksListByCurricularCourse();

    @Atomic
    public static List runReadStudentMarksListByCurricularCourse(User userView, String curricularCourseID,
            String executionYear) throws ExcepcaoInexistente, FenixServiceException, NotAuthorizedException {
        StudentListByCurricularCourseAuthorizationFilter.instance.execute(curricularCourseID);
        return serviceInstance.run(userView, curricularCourseID, executionYear);
    }

}