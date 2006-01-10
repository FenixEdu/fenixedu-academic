package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class EnrolmentEquivalenceVO extends VersionedObjectsBase implements
		IPersistentEnrolmentEquivalence {

	public EnrolmentEquivalence readByEnrolment(Integer enrolmentId)
			throws ExcepcaoPersistencia {
		Enrolment enrolment = (Enrolment)readByOID(Enrolment.class,enrolmentId);
		
		if (enrolment != null && !enrolment.getEnrolmentEquivalences().isEmpty()) {
			return enrolment.getEnrolmentEquivalences().get(0);
		}
		return null;
	}

}
