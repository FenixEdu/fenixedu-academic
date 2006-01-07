/*
 * Created on 21/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentContributor;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorOJB extends PersistentObjectOJB implements
		IPersistentContributor {

	public Contributor readByContributorNumber(Integer contributorNumber)
			throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("contributorNumber", contributorNumber);
		return (Contributor) queryObject(Contributor.class, crit);
	}

	public List readAll() throws ExcepcaoPersistencia {
		return queryList(Contributor.class, null);
	}
}