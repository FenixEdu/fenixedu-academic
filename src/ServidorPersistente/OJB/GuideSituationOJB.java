package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideSituation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideSituationOJB extends ObjectFenixOJB implements IPersistentGuideSituation
{

    public GuideSituationOJB()
    {
    }

    public void write(IGuideSituation guideSituationToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IGuideSituation guideSituationBD = null;
        if (guideSituationToWrite == null)
            // Should we throw an exception saying nothing to write or
            // something of the sort?
            // By default, if OJB received a null object it would complain.
            return;

        // read SituationOfGuide

        guideSituationBD =
            this.readByGuideAndSituation(
                guideSituationToWrite.getGuide(),
                guideSituationToWrite.getSituation());

        // if (guide situation not in database) then write it
        if (guideSituationBD == null)
        {
            super.lockWrite(guideSituationToWrite);
        }
        // else if (guide situation is mapped to the database then write any
		// existing changes)
        else if (
            (guideSituationToWrite != null)
                && ((GuideSituation) guideSituationBD).getIdInternal().equals(
                    ((GuideSituation) guideSituationToWrite).getIdInternal()))
        {
            super.lockWrite(guideSituationBD);

        }
        else
            throw new ExistingPersistentException();
    }

    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        return queryList(GuideSituation.class, crit);

    }

    public IGuideSituation readGuideActiveSituation(IGuide guide) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        crit.addEqualTo("state", new Integer(State.ACTIVE));
        return (IGuideSituation) queryObject(GuideSituation.class, crit);
    }

    public IGuideSituation readByGuideAndSituation(IGuide guide, SituationOfGuide situationOfGuide)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        crit.addEqualTo("guide.version", guide.getVersion());
        crit.addEqualTo("situation", situationOfGuide.getSituation());
        return (IGuideSituation) queryObject(GuideSituation.class, crit);
        
    }
}
