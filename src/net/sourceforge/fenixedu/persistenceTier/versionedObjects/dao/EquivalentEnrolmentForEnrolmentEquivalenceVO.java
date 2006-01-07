package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EquivalentEnrolmentForEnrolmentEquivalenceVO extends
		VersionedObjectsBase implements
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence {

	public EquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
			final Integer enrolmentEquivalenceId, Integer equivalentEnrolmentId)
			throws ExcepcaoPersistencia {

		Enrolment enrolment = (Enrolment)readByOID(Enrolment.class,equivalentEnrolmentId);
		
		if (enrolment != null) {
			List<EquivalentEnrolmentForEnrolmentEquivalence> equivs = enrolment.getEquivalentEnrolmentForEnrolmentEquivalences();
			return (EquivalentEnrolmentForEnrolmentEquivalence) CollectionUtils.find(equivs,new Predicate() {
				public boolean evaluate(Object o) {
					return ((EquivalentEnrolmentForEnrolmentEquivalence)o).getEnrolmentEquivalence().getIdInternal().equals(enrolmentEquivalenceId);
				}
			});
		}
		
		return null;
	}

}
