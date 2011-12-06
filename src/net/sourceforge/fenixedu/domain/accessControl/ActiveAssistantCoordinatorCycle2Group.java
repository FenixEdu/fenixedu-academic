package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Coordinator;

public class ActiveAssistantCoordinatorCycle2Group extends ActiveCoordinatorCycle2Group {

    private static final long serialVersionUID = -1670838873686375271L;

    @Override
    protected boolean isToAddCoordinator(Coordinator coordinator) {
	return !coordinator.isResponsible();
    }

}