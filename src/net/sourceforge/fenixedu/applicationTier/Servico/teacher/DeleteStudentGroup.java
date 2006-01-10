/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class DeleteStudentGroup implements IService {

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentGroup persistentStudentGroup = persistentSuport.getIPersistentStudentGroup();
      
        StudentGroup deletedStudentGroup = (StudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, studentGroupCode);
        
        if (deletedStudentGroup == null)
            throw new ExistingServiceException();     

        deletedStudentGroup.delete();
        persistentStudentGroup.deleteByOID(StudentGroup.class, deletedStudentGroup.getIdInternal());

        return Boolean.TRUE;
    }
}
