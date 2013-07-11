package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.util.State;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class CreateGuideSituation {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String guideID, String remarks, GuideState situation, Date date) {

        Guide guide = FenixFramework.getDomainObject(guideID);

        for (GuideSituation guideSituation : guide.getGuideSituations()) {
            guideSituation.setState(new State(State.INACTIVE));
        }

        GuideSituation guideSituation = new GuideSituation(situation, remarks, date, guide, new State(State.ACTIVE));

        guide.getGuideSituations().add(guideSituation);

    }

}