/*
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
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
			Integer groupPropertiesCode, Integer newShiftCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		ITurnoPersistente persistentShift = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		persistentShift = persistentSupport.getITurnoPersistente();

		Grouping grouping = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (grouping == null) {
			throw new ExistingServiceException();
		}

		Shift shift = (Shift) persistentShift.readByOID(Shift.class, newShiftCode);

		// grouping.checkShiftCapacity(shift);

		StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
				StudentGroup.class, studentGroupCode);

		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		studentGroup.editShift(shift);

		return Boolean.TRUE;
	}
}