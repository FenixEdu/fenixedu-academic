package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Angela 04/07/2003
 *  
 */
public class ReadStudentEnrolmentEvaluation implements IService {

    public InfoSiteEnrolmentEvaluation run(Integer studentEvaluationCode) throws FenixServiceException {

        IEnrolmentEvaluation enrolmentEvaluation = null;
        InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
        InfoEnrolment infoEnrolment = new InfoEnrolment();
        InfoTeacher infoTeacher = new InfoTeacher();
        List infoEnrolmentEvaluations = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            enrolmentEvaluation = (IEnrolmentEvaluation) persistentEnrolmentEvaluation.readByOID(
                    EnrolmentEvaluation.class, studentEvaluationCode, false);
            //			get curricularCourseScope for enrolmentEvaluation
            //            ICurricularCourseScope curricularCourseScope = new
            // CurricularCourseScope();
            //            curricularCourseScope.setIdInternal(
            //                enrolmentEvaluation.getEnrolment().getCurricularCourseScope().getIdInternal());
            //            curricularCourseScope =
            //                (ICurricularCourseScope)
            // persistentCurricularCourseScope.readByOId(
            //                    curricularCourseScope,
            //                    false);

            //			ICurricularCourseScope curricularCourseScopeForCriteria =
            //				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
            infoEnrolment = InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod
                    .newInfoFromDomain(enrolmentEvaluation.getEnrolment());

            IPerson person = enrolmentEvaluation.getPersonResponsibleForGrade();
            ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);

            infoEnrolmentEvaluation = Cloner
                    .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
            infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoTeacher.getInfoPerson());
            if (enrolmentEvaluation.getEmployee() != null)
                infoEnrolmentEvaluation.setInfoEmployee(Cloner
                        .copyIPerson2InfoPerson(enrolmentEvaluation.getEmployee().getPerson()));
            infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
            infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);

            //			enrolmenEvaluation.setEnrolment
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

        return infoSiteEnrolmentEvaluation;

    }

}