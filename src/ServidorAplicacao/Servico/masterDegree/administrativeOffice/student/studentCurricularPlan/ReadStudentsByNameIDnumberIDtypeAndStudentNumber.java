package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;

/**
 * @author David Santos 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumber implements IService {

    public List run(String studentName, String idNumber, TipoDocumentoIdentificacao idType,
            Integer studentNumber) throws FenixServiceException {
        List result = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();

            List tempResult = persistentStudent
                    .readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(studentName, idNumber,
                            idType, studentNumber);

            if (tempResult != null) {
                Iterator iterator = tempResult.iterator();
                while (iterator.hasNext()) {
                    IStudent student = (IStudent) iterator.next();
                    result.add(Cloner.copyIStudent2InfoStudent(student));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return result;
    }
}