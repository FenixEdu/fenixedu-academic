package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideSituation extends Service {

    public void run(Integer guideID, String remarks, GuideState situation, Date date)
            throws ExcepcaoPersistencia {

        Guide guide = rootDomainObject.readGuideByOID(guideID);

        for (GuideSituation guideSituation : guide.getGuideSituations()) {
            guideSituation.setState(new State(State.INACTIVE));
        }

        GuideSituation guideSituation = new GuideSituation(situation, remarks, date,
                guide, new State(State.ACTIVE));

        guide.getGuideSituations().add(guideSituation);

    }

}