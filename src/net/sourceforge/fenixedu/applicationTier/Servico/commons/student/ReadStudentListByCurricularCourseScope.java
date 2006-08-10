/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentListByCurricularCourseScope extends Service {

    public List run(IUserView userView, Integer curricularCourseScopeID) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {
        CurricularCourseScope curricularCourseScope = rootDomainObject.readCurricularCourseScopeByOID(curricularCourseScopeID);

        List enrolmentList = curricularCourseScope.getCurricularCourse().getCurriculumModules();

        if ((enrolmentList == null) || (enrolmentList.size() == 0)) {
            throw new NonExistingServiceException();
        }

        return cleanList(enrolmentList, userView);
    }

    /**
     * @param studentCurricularPlans
     * @return A list of Registration curricular Plans without the duplicates
     * @throws ExcepcaoPersistencia
     */
    private List cleanList(List studentCurricularPlans, IUserView userView)
            throws FenixServiceException, ExcepcaoPersistencia {
        List result = new ArrayList();
        Integer numberAux = null;

        Iterator iterator = studentCurricularPlans.iterator();
        while (iterator.hasNext()) {
            Enrolment enrolment = (Enrolment) iterator.next();

            if ((numberAux == null)
                    || (numberAux.intValue() != enrolment.getStudentCurricularPlan().getStudent()
                            .getNumber().intValue())) {
                numberAux = enrolment.getStudentCurricularPlan().getStudent().getNumber();

                /*
                 * Object args[] = { enrolment }; InfoEnrolmentEvaluation
                 * infoEnrolmentEvaluation = (InfoEnrolmentEvaluation)
                 * ServiceManagerServiceFactory .executeService(userView,
                 * "GetEnrolmentGrade", args);
                 */
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = (new GetEnrolmentGrade())
                        .run(enrolment);

                InfoEnrolment infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                        .newInfoFromDomain(enrolment);
                infoEnrolment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);

                result.add(infoEnrolment);
            }
        }

        return result;
    }

}