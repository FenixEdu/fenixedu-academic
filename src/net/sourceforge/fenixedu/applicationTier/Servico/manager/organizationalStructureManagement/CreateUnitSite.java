package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard.UnitAnnouncementBoardParameters;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateUnitSite extends Service {

	public void run(Unit unit) throws FenixServiceException {
		unit.initializeSite();
		createBoards(unit);
	}

	private void createBoards(Unit unit) throws FenixServiceException {
		CreateUnitAnnouncementBoard service = new CreateUnitAnnouncementBoard();
		createBoard(service, unit, "Anúncios");
		createBoard(service, unit, "Eventos");
	}

	private void createBoard(CreateUnitAnnouncementBoard service, Unit unit, String name) throws FenixServiceException {
		if (!hasBoard(unit, name)) {
			service.run(new UnitAnnouncementBoardParameters(unit.getIdInternal(), name, false,
					UnitBoardPermittedGroupType.UB_PUBLIC, UnitBoardPermittedGroupType.UB_UNITSITE_MANAGERS,
					UnitBoardPermittedGroupType.UB_MANAGER));
		}
	}
	
	private boolean hasBoard(Unit unit, String name) {
        for (AnnouncementBoard board : unit.getBoards()) {
            if (board.getName().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
}
