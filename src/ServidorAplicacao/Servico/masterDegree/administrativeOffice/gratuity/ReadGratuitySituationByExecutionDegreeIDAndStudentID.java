package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGratuitySituation;
import Dominio.ExecutionDegree;
import Dominio.IExecutionDegree;
import Dominio.IGratuitySituation;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadGratuitySituationByExecutionDegreeIDAndStudentID implements IService {

    /**
     * Constructor
     */
    public ReadGratuitySituationByExecutionDegreeIDAndStudentID() {
    }

    public InfoGratuitySituation run(Integer executionDegreeID, Integer studentID)
            throws FenixServiceException {

        InfoGratuitySituation infoGratuitySituation = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionDegree cursoExecucao = (IExecutionDegree) sp.getIPersistentExecutionDegree()
                    .readByOID(ExecutionDegree.class, executionDegreeID);
            IStudent student = (IStudent) sp.getIPersistentStudent().readByOID(Student.class, studentID);

            if ((cursoExecucao == null) || (student == null)) {
                return null;
            }

            IGratuitySituation gratuitySituation = sp.getIPersistentGratuitySituation()
                    .readGratuitySituationByExecutionDegreeAndStudent(cursoExecucao, student);

            infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuitySituation);

            return infoGratuitySituation;

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

}