package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements.CreateUnitAnnouncementBoard.UnitAnnouncementBoardParameters;
import net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateUnitSite {

    @Atomic
    public static void run(Unit unit) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        unit.initializeSite();
        createBoards(unit);
    }

    private static void createBoards(Unit unit) throws FenixServiceException {
        CreateUnitAnnouncementBoard service = new CreateUnitAnnouncementBoard();
        createBoard(service, unit, "Anúncios");
        createBoard(service, unit, "Eventos");
    }

    private static void createBoard(CreateUnitAnnouncementBoard service, Unit unit, String name) throws FenixServiceException {
        if (!hasBoard(unit, name)) {
            service.run(new UnitAnnouncementBoardParameters(unit.getExternalId(), name, false,
                    UnitBoardPermittedGroupType.UB_PUBLIC, UnitBoardPermittedGroupType.UB_UNITSITE_MANAGERS,
                    UnitBoardPermittedGroupType.UB_MANAGER));
        }
    }

    private static boolean hasBoard(Unit unit, String name) {
        for (AnnouncementBoard board : unit.getBoards()) {
            if (board.getName().equalInAnyLanguage(name)) {
                return true;
            }
        }

        return false;
    }
}