package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.SituationOfGuide;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideSituation implements IService {

    public void run(Integer guideID, String remarks, SituationOfGuide situation, Date date)
            throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IGuide guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideID, true);

        IGuideSituation guideSituation = new GuideSituation(situation, remarks, date, guide, new State(
                State.ACTIVE));

        sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);

        for (Iterator iter = guide.getGuideSituations().iterator(); iter.hasNext();) {
            IGuideSituation guideSituationTmp = (IGuideSituation) iter.next();
            guideSituationTmp.setState(new State(State.INACTIVE));
            sp.getIPersistentGuideSituation().simpleLockWrite(guideSituationTmp);

        }

        guide.getGuideSituations().add(guideSituation);

    }

}