package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class EquivalentEnrolmentForEnrolmentEquivalenceVO extends
		VersionedObjectsBase implements
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence {

	public IEquivalentEnrolmentForEnrolmentEquivalence readByEnrolmentEquivalenceAndEquivalentEnrolment(
			final Integer enrolmentEquivalenceId, Integer equivalentEnrolmentId)
			throws ExcepcaoPersistencia {

		IEnrolment enrolment = (IEnrolment)readByOID(Enrolment.class,equivalentEnrolmentId);
		
		if (enrolment != null) {
			List<IEquivalentEnrolmentForEnrolmentEquivalence> equivs = enrolment.getEquivalentEnrolmentForEnrolmentEquivalences();
			return (IEquivalentEnrolmentForEnrolmentEquivalence) CollectionUtils.find(equivs,new Predicate() {
				public boolean evaluate(Object o) {
					return ((IEquivalentEnrolmentForEnrolmentEquivalence)o).getEnrolmentEquivalence().getIdInternal().equals(enrolmentEquivalenceId);
				}
			});
		}
		
		return null;
	}

}
