package net.sourceforge.fenixedu.presentationTier.jsf.phaseListeners;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 
 * @author naat
 * 
 */
public class PhaseTracker implements PhaseListener {

    /**
     * 
     */
    private static final long serialVersionUID = -3617760112695979158L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {
    }
}