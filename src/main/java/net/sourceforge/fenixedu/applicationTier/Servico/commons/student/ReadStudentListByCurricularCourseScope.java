/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.StudentListByCurricularCourseScopeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentListByCurricularCourseScope extends FenixService {

    protected List run(IUserView userView, Integer curricularCourseScopeID) throws FenixServiceException {
        CurricularCourseScope curricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(curricularCourseScopeID);

        final List<Enrolment> enrolmentList = curricularCourseScope.getCurricularCourse().getEnrolments();
        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }
        return cleanList(enrolmentList);
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

    private static final ReadStudentListByCurricularCourseScope serviceInstance = new ReadStudentListByCurricularCourseScope();

    @Service
    public static List runReadStudentListByCurricularCourseScope(IUserView userView, Integer curricularCourseScopeID)
            throws FenixServiceException, NotAuthorizedException {
        StudentListByCurricularCourseScopeAuthorizationFilter.instance.execute(userView, curricularCourseScopeID);
        return serviceInstance.run(userView, curricularCourseScopeID);
    }

}