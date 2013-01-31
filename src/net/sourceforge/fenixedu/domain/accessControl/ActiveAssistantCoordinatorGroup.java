package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Coordinator;

public class ActiveAssistantCoordinatorGroup extends ActiveCoordinatorGroup {

	private static final long serialVersionUID = -1670838873686375271L;

	@Override
	protected boolean isToAddCoordinator(Coordinator coordinator) {
		return !coordinator.isResponsible();
	}

}