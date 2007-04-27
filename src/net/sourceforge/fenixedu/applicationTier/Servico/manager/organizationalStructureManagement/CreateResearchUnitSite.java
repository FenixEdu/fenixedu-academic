package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard.UnitAnnouncementBoardParameters;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class CreateResearchUnitSite extends Service {

	public void run(ResearchUnit unit) throws FenixServiceException {
		new ResearchUnitSite(unit);
		CreateUnitAnnouncementBoard boards = new CreateUnitAnnouncementBoard();
		boards.run(new UnitAnnouncementBoardParameters(unit.getIdInternal(), "Anúncios", false,
				UnitBoardPermittedGroupType.UB_PUBLIC, UnitBoardPermittedGroupType.UB_UNITSITE_MANAGERS,
				UnitBoardPermittedGroupType.UB_MANAGER));
		boards.run(new UnitAnnouncementBoardParameters(unit.getIdInternal(), "Eventos", false,
				UnitBoardPermittedGroupType.UB_PUBLIC, UnitBoardPermittedGroupType.UB_UNITSITE_MANAGERS,
				UnitBoardPermittedGroupType.UB_MANAGER));

	}
}
