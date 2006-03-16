package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentContributor;

import org.apache.ojb.broker.query.Criteria;

public class ContributorOJB extends PersistentObjectOJB implements IPersistentContributor {

	public Contributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("contributorNumber", contributorNumber);
		return (Contributor) queryObject(Contributor.class, crit);
	}

}
