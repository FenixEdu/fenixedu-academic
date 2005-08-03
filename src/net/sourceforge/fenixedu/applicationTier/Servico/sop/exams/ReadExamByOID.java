/*
 * ReadExamsByOID.java
 *
 * Created on 12/11/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamByOID implements IService {

    public InfoExam run(Integer oid) throws FenixServiceException {
        InfoExam infoExam = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExam exam = (IExam) sp.getIPersistentExam().readByOID(Exam.class, oid);
            if (exam == null) {
                throw new FenixServiceException("The exam does not exist");
            }
            infoExam = InfoExam.newInfoFromDomain(exam);

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoExam;
    }
}