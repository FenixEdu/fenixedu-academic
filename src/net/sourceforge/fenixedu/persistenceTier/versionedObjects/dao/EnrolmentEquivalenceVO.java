package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class EnrolmentEquivalenceVO extends VersionedObjectsBase implements
		IPersistentEnrolmentEquivalence {

	public IEnrolmentEquivalence readByEnrolment(Integer enrolmentId)
			throws ExcepcaoPersistencia {
		IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class,enrolmentId);
		
		if (enrolment != null && !enrolment.getEnrolmentEquivalences().isEmpty()) {
			return enrolment.getEnrolmentEquivalences().get(0);
		}
		return null;
	}

}
