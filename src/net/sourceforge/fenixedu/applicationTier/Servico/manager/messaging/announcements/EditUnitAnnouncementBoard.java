/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 28, 2006,4:04:39 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 28, 2006,4:04:39 PM
 * 
 */
public class EditUnitAnnouncementBoard extends CreateUnitAnnouncementBoard {

    public void run(UnitAnnouncementBoard board, UnitAnnouncementBoardParameters parameters)
            throws FenixServiceException {

        board.setUnitPermittedReadGroupType(parameters.readersGroupType);
        board.setUnitPermittedWriteGroupType(parameters.writersGroupType);
        board.setUnitPermittedManagementGroupType(parameters.managementGroupType);
        board.setName(parameters.name);
        board.setMandatory(parameters.mandatory);
        board.setReaders(this.buildGroup(parameters.readersGroupType, (Unit) board.getParty()));
        board.setWriters(this.buildGroup(parameters.writersGroupType, (Unit) board.getParty()));
        board.setManagers(this.buildGroup(parameters.managementGroupType, (Unit) board.getParty()));
    }
}
