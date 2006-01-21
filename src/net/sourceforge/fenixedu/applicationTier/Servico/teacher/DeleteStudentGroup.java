/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */
public class DeleteStudentGroup extends Service {

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        StudentGroup deletedStudentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
                StudentGroup.class, studentGroupCode);
        
        if (deletedStudentGroup == null)
            throw new ExistingServiceException();     

        deletedStudentGroup.delete();
        persistentSupport.getIPersistentObject().deleteByOID(StudentGroup.class, deletedStudentGroup.getIdInternal());

        return Boolean.TRUE;
    }
}
