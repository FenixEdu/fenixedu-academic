package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoEvaluation;
import DataBeans.InfoSiteSubmitMarks;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IEnrollment;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class ValidateSubmitMarks implements IServico {
    private static ValidateSubmitMarks _service = new ValidateSubmitMarks();

    /**
     * The actor of this class.
     */
    private ValidateSubmitMarks() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ValidateSubmitMarks";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadExecutionCourse
     */
    public static ValidateSubmitMarks getService() {
        return _service;
    }

    public InfoSiteSubmitMarks run(Integer executionCourseCode, Integer evaluationCode, UserView userView)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //execution course and execution course's site
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp
                    .getIPersistentEnrolmentEvaluation();

            final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);

            //evaluation
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,
                    evaluationCode);

            //attend list
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            List attendList = persistentAttend.readByExecutionCourse(executionCourse);

            //verifySubmitMarks(attendList);

            List enrolmentListIds = (List) CollectionUtils.collect(attendList, new Transformer() {

                public Object transform(Object input) {
                    IAttends attend = (IAttends) input;
                    IEnrollment enrolment = attend.getEnrolment();
                    if(enrolment != null) {
                        if(enrolment.getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))
                            return enrolment.getIdInternal();
                    }
                    return null;
                }
            });

            enrolmentListIds = (List) CollectionUtils.select(enrolmentListIds, new Predicate() {
                public boolean evaluate(Object arg0) {
                    return arg0 != null;
                }
            });
            
            List alreadySubmiteMarks = new ArrayList();
            if(!enrolmentListIds.isEmpty()) {
                alreadySubmiteMarks = enrolmentEvaluationDAO.readAlreadySubmitedMarks(enrolmentListIds);
            }

            if (!alreadySubmiteMarks.isEmpty()) {
                throw new FenixServiceException("errors.submitMarks.yetSubmited");
            }

            //marks list
            IPersistentMark persistentMark = sp.getIPersistentMark();
            List markList = persistentMark.readBy(evaluation);

            //Check if there is any mark. If not, we can not submit
            if (markList.isEmpty()) {
                throw new FenixServiceException("errors.submitMarks.noMarks");
            }

            InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

            //CLONER
            //infoSiteSubmitMarks.setInfoEvaluation(Cloner
            //.copyIEvaluation2InfoEvaluation(evaluation));
            infoSiteSubmitMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));

            return infoSiteSubmitMarks;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }
}