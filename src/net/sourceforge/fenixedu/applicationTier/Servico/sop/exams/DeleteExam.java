/*
 * Created on 16/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 * 
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteExam implements IService {

    public Object run(Integer examOID) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IExam examToDelete = (IExam) sp.getIPersistentExam().readByOID(Exam.class, examOID);
        if (examToDelete == null) {
            throw new FenixServiceException("error.noExam");
        }

        examToDelete.delete();

        return Boolean.TRUE;
    }

}