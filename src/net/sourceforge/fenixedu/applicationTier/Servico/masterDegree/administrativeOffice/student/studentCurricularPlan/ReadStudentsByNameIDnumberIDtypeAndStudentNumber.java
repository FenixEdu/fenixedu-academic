package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.TipoDocumentoIdentificacao;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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