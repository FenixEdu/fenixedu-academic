/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.IStudentGroup;
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
      
        IStudentGroup deletedStudentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, studentGroupCode);
        if (deletedStudentGroup == null) {
            throw new ExistingServiceException();
        }

        List studentGroupAttendList = deletedStudentGroup.getStudentGroupAttends();

        if (studentGroupAttendList.size() != 0) {
            throw new InvalidSituationServiceException();
        }

        deletedStudentGroup.getAttendsSet().removeStudentGroup(deletedStudentGroup);
        deletedStudentGroup.getShift().getAssociatedStudentGroups().remove(deletedStudentGroup);
        deletedStudentGroup.setAttendsSet(null);
        deletedStudentGroup.setShift(null);
        persistentStudentGroup.deleteByOID(StudentGroup.class, deletedStudentGroup.getIdInternal());

        return new Boolean(true);
    }
}
