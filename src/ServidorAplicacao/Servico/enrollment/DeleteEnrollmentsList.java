/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.enrollment;

import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.Enrolment;
import Dominio.IEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.utils.enrolment.DeleteEnrolmentUtils;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteEnrollmentsList implements IService {
    public DeleteEnrollmentsList() {
    }

    // some of these arguments may be null. they are only needed for filter
    public void run(InfoStudent infoStudent, TipoCurso degreeType, List enrolmentIDList)
            throws FenixServiceException {
        try {
            
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment enrolmentDAO = persistentSuport.getIPersistentEnrolment();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = persistentSuport
                    .getIPersistentEnrolmentEvaluation();
            
            if (enrolmentIDList != null && enrolmentIDList.size() > 0) {
                ListIterator iterator = enrolmentIDList.listIterator();
                while (iterator.hasNext()) {
                    Integer enrolmentID = (Integer) iterator.next();
                    
                    final IEnrollment enrollment = (IEnrollment) enrolmentDAO.readByOID(Enrolment.class,
                            enrolmentID);
                    
                    DeleteEnrolmentUtils.deleteEnrollment(enrolmentDAO, enrolmentEvaluationDAO, enrollment);

                    /*DeleteEnrolment deleteEnrolmentService = new DeleteEnrolment();
                    deleteEnrolmentService.run(null, null, enrolmentID);*/                   
                    
                }
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }
}