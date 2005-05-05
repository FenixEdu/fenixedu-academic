package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;

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

    public IGuideSituation readByGuideAndSituation(IGuide guide, GuideState situationOfGuide)
            throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("guide.number", guide.getNumber());
        crit.addEqualTo("guide.year", guide.getYear());
        crit.addEqualTo("guide.version", guide.getVersion());
        crit.addEqualTo("situation", situationOfGuide.name());
        return (IGuideSituation) queryObject(GuideSituation.class, crit);

    }
}