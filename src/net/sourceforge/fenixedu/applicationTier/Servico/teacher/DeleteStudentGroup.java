/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 *  
 */
public class DeleteStudentGroup implements IServico {
    private static DeleteStudentGroup service = new DeleteStudentGroup();

    public static DeleteStudentGroup getService() {
        return service;
    }

    private DeleteStudentGroup() {
    }

    public final String getNome() {
        return "DeleteStudentGroup";
    }

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentStudentGroup persistentStudentGroup = persistentSuport
                    .getIPersistentStudentGroup();
            IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport
                    .getIPersistentStudentGroupAttend();

            IStudentGroup deletedStudentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (deletedStudentGroup == null) {
                throw new ExistingServiceException();
            }

            List studentGroupAttendList = persistentStudentGroupAttend
            .readAllByStudentGroup(deletedStudentGroup);

            if (studentGroupAttendList.size() != 0) {
            	throw new InvalidSituationServiceException();
            }
            
            deletedStudentGroup.getAttendsSet().removeStudentGroup(deletedStudentGroup);
            
            persistentStudentGroup.delete(deletedStudentGroup);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }
}

