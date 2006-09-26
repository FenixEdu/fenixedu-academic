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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;

public class ReadStudentListByCurricularCourseScope extends Service {

    public List run(IUserView userView, Integer curricularCourseScopeID) throws FenixServiceException {
        CurricularCourseScope curricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(curricularCourseScopeID);

        List enrolmentList = curricularCourseScope.getCurricularCourse().getCurriculumModules();

        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }

        return cleanList(enrolmentList);
    }

    private List cleanList(final List enrolmentList) throws FenixServiceException {

	if (enrolmentList.isEmpty()) {
	    throw new NonExistingServiceException();
	}

	Integer studentNumber = null;
	final List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
	for (final Enrolment enrolment : (List<Enrolment>) enrolmentList) {

	    if (studentNumber == null
		    || studentNumber.intValue() != enrolment.getStudentCurricularPlan().getStudent()
			    .getNumber().intValue()) {

		studentNumber = enrolment.getStudentCurricularPlan().getStudent().getNumber();
		result.add(InfoEnrolment.newInfoFromDomain(enrolment));
	    }
	}
	Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
	return result;
    }

}