/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.GetEnrolmentGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentMarksListByCurricularCourse implements IService {

    public List run(IUserView userView, Integer curricularCourseID, String executionYear)
            throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;

        List enrolmentList = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, curricularCourseID);

            if (executionYear != null) {
                enrolmentList = sp.getIPersistentEnrolment().readByCurricularCourseAndYear(
                        curricularCourseID, executionYear);
            } else {
                enrolmentList = curricularCourse.getEnrolments();
            }
            if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
                throw new NonExistingServiceException();
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return cleanList(enrolmentList, userView);
    }

    /**
     * @param enrollments
     * @return A list of Student curricular Plans without the duplicates
     */
    private List cleanList(List enrollments, IUserView userView) throws FenixServiceException {
        List result = new ArrayList();
        Integer numberAux = null;

        BeanComparator numberComparator = new BeanComparator("studentCurricularPlan.student.number");
        Collections.sort(enrollments, numberComparator);

        Iterator iterator = enrollments.iterator();
        while (iterator.hasNext()) {
            IEnrolment enrolment = (IEnrolment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent().getNumber();

                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (new GetEnrolmentGrade()).run(enrolment);
                
                if (infoEnrolmentEvaluation != null) {

                    InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                            .newInfoFromDomain(enrolment);
                    infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);
                    result.add(infoEnrolment);
                }
            }
        }
        return result;
    }
}