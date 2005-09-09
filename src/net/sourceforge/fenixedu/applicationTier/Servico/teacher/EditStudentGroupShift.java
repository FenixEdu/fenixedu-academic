/*
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class EditStudentGroupShift implements IService {

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode,
            Integer groupPropertiesCode, Integer newShiftCode) throws FenixServiceException {

        ITurnoPersistente persistentShift = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IPersistentGrouping persistentGroupProperties = null;

        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            persistentGroupProperties = persistentSupport.getIPersistentGrouping();
            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            persistentShift = persistentSupport.getITurnoPersistente();

            IGrouping grouping = (IGrouping) persistentGroupProperties.readByOID(Grouping.class,
                    groupPropertiesCode);

            if (grouping == null) {
                throw new ExistingServiceException();
            }

            IShift shift = (IShift) persistentShift.readByOID(Shift.class, newShiftCode);

            grouping.checkShiftCapacity(shift);

            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                    StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidArgumentsServiceException();
            }

            studentGroup.editShift(shift);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return Boolean.TRUE;
    }
}