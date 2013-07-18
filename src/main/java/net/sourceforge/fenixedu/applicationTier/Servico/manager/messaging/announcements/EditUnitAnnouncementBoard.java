/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 28, 2006,4:04:39 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 28, 2006,4:04:39 PM
 * 
 */
public class EditUnitAnnouncementBoard extends CreateUnitAnnouncementBoard {

    protected void run(UnitAnnouncementBoard board, UnitAnnouncementBoardParameters parameters) throws FenixServiceException {

        board.setUnitPermittedReadGroupType(parameters.readersGroupType);
        board.setUnitPermittedWriteGroupType(parameters.writersGroupType);
        board.setUnitPermittedManagementGroupType(parameters.managementGroupType);
        board.setName(new MultiLanguageString(parameters.name));
        board.setMandatory(parameters.mandatory);
        board.setReaders(this.buildGroup(parameters.readersGroupType, board.getParty()));
        board.setWriters(this.buildGroup(parameters.writersGroupType, board.getParty()));
        board.setManagers(this.buildGroup(parameters.managementGroupType, board.getParty()));
    }
    // Service Invokers migrated from Berserk

    private static final EditUnitAnnouncementBoard serviceInstance = new EditUnitAnnouncementBoard();

    @Service
    public static void runEditUnitAnnouncementBoard(UnitAnnouncementBoard board, UnitAnnouncementBoardParameters parameters) throws FenixServiceException  {
        serviceInstance.run(board, parameters);
    }

}