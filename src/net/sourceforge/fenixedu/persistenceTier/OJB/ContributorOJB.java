/*
 * Created on 21/Mar/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentContributor;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorOJB extends PersistentObjectOJB implements IPersistentContributor {

    public ContributorOJB() {
    }

    public IContributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("contributorNumber", contributorNumber);
        return (IContributor) queryObject(Contributor.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();

        return queryList(Contributor.class, crit);
    }

    public List readContributorListByNumber(Integer contributorNumber) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        if (contributorNumber != null) {
            crit.addEqualTo("contributorNumber", contributorNumber);
        }

        return queryList(Contributor.class, crit, "contributorNumber", true);
    }

}