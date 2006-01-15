/*
 * Created on 20/Out/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyGroupingAndStudentGroupWithoutShift implements IService {

	public Integer run(Integer studentGroupCode, Integer groupPropertiesCode, String shiftCodeString,
			String username) throws FenixServiceException, ExcepcaoPersistencia {
		IPersistentGrouping persistentGroupProperties = null;

		ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

		persistentGroupProperties = ps.getIPersistentGrouping();

		Grouping groupProperties = (Grouping) persistentGroupProperties.readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		StudentGroup studentGroup = (StudentGroup) ps.getIPersistentObject().readByOID(
				StudentGroup.class, studentGroupCode);

		if (studentGroup == null) {
			throw new InvalidSituationServiceException();
		}

		Integer shiftCode = null;
		if (shiftCodeString != null) {
			shiftCode = new Integer(shiftCodeString);
		}

		if (studentGroup.getShift() != null && shiftCode == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (studentGroup.getShift() == null) {
			if (shiftCode != null)
				throw new InvalidArgumentsServiceException();
		} else {
			if (studentGroup.getShift().getIdInternal().intValue() != shiftCode.intValue()) {
				throw new InvalidArgumentsServiceException();
			}
		}

		if (studentGroup.getShift() != null && groupProperties.getShiftType() != null) {
			return new Integer(1);
		}

		if (studentGroup.getShift() != null && groupProperties.getShiftType() == null) {
			return new Integer(2);
		}

		if (studentGroup.getShift() == null && groupProperties.getShiftType() != null) {
			return new Integer(3);
		}

		if (studentGroup.getShift() == null && groupProperties.getShiftType() == null) {
			return new Integer(4);
		}

		return new Integer(5);

	}
}
