/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

public class ReadStudentListByCurricularCourse extends Service {

    public List run(IUserView userView, Integer curricularCourseID, String executionYear)
            throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

        CurricularCourse curricularCourse = null;

        // Read the Students

        curricularCourse = (CurricularCourse) persistentObject.readByOID(CurricularCourse.class, curricularCourseID);

        List enrolmentList = null;
        if (executionYear != null) {
        	enrolmentList = curricularCourse.getEnrolmentsByYear(executionYear);
        } else {
            enrolmentList = curricularCourse.getCurriculumModules();
        }

        if ((enrolmentList == null) || (enrolmentList.isEmpty())) {
            throw new NonExistingServiceException();
        }

        return cleanList(enrolmentList);
    }

    /**
     * @param enrolmentList
     * @return A list of enrolments without the duplicates
     * @throws ExcepcaoPersistencia 
     */
    private List cleanList(List enrolmentList) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoEnrolment> result = new ArrayList<InfoEnrolment>();
        Integer numberAux = null;

        Iterator iterator = enrolmentList.iterator();
        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent().getNumber();

                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (new GetEnrolmentGrade())
                        .run(enrolment);

                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

                result.add(infoEnrolment);
            }
        }

        BeanComparator numberComparator = new BeanComparator(
                "infoStudentCurricularPlan.infoStudent.number");
        Collections.sort(result, numberComparator);

        return result;
    }

}