/*
 * Created on 11/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class EnrollStudentGroupShift implements IService {

	public Boolean run(Integer executionCourseCode, Integer studentGroupCode,
			Integer groupPropertiesCode, Integer newShiftCode) throws FenixServiceException,
			ExcepcaoPersistencia {

		ITurnoPersistente persistentShift = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Grouping grouping = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (grouping == null) {
			throw new ExistingServiceException();
		}

		persistentShift = persistentSupport.getITurnoPersistente();
		Shift shift = (Shift) persistentShift.readByOID(Shift.class, newShiftCode);

		if (shift == null) {
			throw new InvalidSituationServiceException();
		}

		StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
				StudentGroup.class, studentGroupCode);

		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (grouping.getShiftType() == null || studentGroup.getShift() != null
				|| (!grouping.getShiftType().equals(shift.getTipo()))) {
			throw new InvalidChangeServiceException();
		}

		studentGroup.setShift(shift);

		return new Boolean(true);
	}
}