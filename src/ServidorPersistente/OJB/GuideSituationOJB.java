package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuideSituation;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideSituationOJB extends PersistentObjectOJB implements IPersistentGuideSituation {

    public GuideSituationOJB() {
    }

    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        return queryList(GuideSituation.class, crit);

    }

    public IGuideSituation readGuideActiveSituation(IGuide guide) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        crit.addEqualTo("state", new Integer(State.ACTIVE));
        return (IGuideSituation) queryObject(GuideSituation.class, crit);
    }

    public IGuideSituation readByGuideAndSituation(IGuide guide, SituationOfGuide situationOfGuide)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        crit.addEqualTo("guide.version", guide.getVersion());
        crit.addEqualTo("situation", situationOfGuide.getSituation());
        return (IGuideSituation) queryObject(GuideSituation.class, crit);

    }
}