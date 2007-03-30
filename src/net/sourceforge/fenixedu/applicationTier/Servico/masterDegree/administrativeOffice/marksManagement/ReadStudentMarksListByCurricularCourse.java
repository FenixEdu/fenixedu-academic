package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;

import org.apache.commons.beanutils.BeanComparator;

public class ReadStudentMarksListByCurricularCourse extends Service {

    public List run(IUserView userView, Integer curricularCourseID, String executionYear)
            throws ExcepcaoInexistente, FenixServiceException {

        final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);

        final List<Enrolment> enrolmentList = (executionYear != null) ? curricularCourse.getEnrolmentsByYear(executionYear) : curricularCourse.getEnrolments();
        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }
        return cleanList(enrolmentList);
    }

    private List cleanList(final List<Enrolment> enrolments) {
        List result = new ArrayList();
        Integer numberAux = null;
        
        for (final Enrolment enrolment : enrolments) {
            if ((numberAux == null) || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getRegistration().getNumber().intValue())) {
        	numberAux = enrolment.getStudentCurricularPlan().getRegistration().getNumber();
        	result.add(InfoEnrolment.newInfoFromDomain(enrolment));
            }
        }
        Collections.sort(result, new BeanComparator("infoStudentCurricularPlan.infoStudent.number"));
        return result;
    }
}