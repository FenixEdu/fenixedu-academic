/*
 * Created on 12/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author joaosa and rmalo
 * 
 */

public class UnEnrollStudentGroupShift extends Service {

	public Boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode)
			throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		StudentGroup studentGroup = (StudentGroup) persistentSupport.getIPersistentObject().readByOID(
				StudentGroup.class, studentGroupCode);

		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (!(studentGroup.getShift() != null && groupProperties.getShiftType() == null)
				|| studentGroup.getShift() == null) {
			throw new InvalidChangeServiceException();
		}

		Shift shift = null;
		studentGroup.setShift(shift);

		return new Boolean(true);
	}
}