/*
 * Created on 21/Mar/2003
 * 
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Contributor;
import Dominio.IContributor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorOJB extends ObjectFenixOJB implements IPersistentContributor
{

    public ContributorOJB()
    {
    }

    public IContributor readByContributorNumber(Integer contributorNumber) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("contributorNumber", contributorNumber);
        return (IContributor) queryObject(Contributor.class, crit);

    }

    public void write(IContributor contributor) throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IContributor contributorBD = null;

        // If there is nothing to write, simply return.
        if (contributor == null)
        {
            return;
        }

        // Read contributor from database.
        contributorBD = this.readByContributorNumber(contributor.getContributorNumber());

        // If contributor is not in database, then write it.
        if (contributorBD == null)
        {
            super.lockWrite(contributor);
            return;
        }

        // the contributor is mapped to the database, then write any existing
        // changes.
        if ((contributor instanceof Contributor)
            && ((Contributor) contributorBD).getIdInternal().equals(
                ((Contributor) contributor).getIdInternal()))
        {
            super.lockWrite(contributorBD);
            return;

        }

        throw new ExistingPersistentException();

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();

        return queryList(Contributor.class, crit);
    }

    public List readContributorListByNumber(Integer contributorNumber) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        if (contributorNumber != null)
        {
            crit.addEqualTo("contributorNumber", contributorNumber);
        }

        crit.addOrderBy("contributorNumber", true);

        return queryList(Contributor.class, crit);

    }

}
