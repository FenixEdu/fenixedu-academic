package net.sourceforge.fenixedu.applicationTier.Servico.sop.exams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoRoomWithInfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SearchRooms extends Service {

    public List run(String name, String building, Integer floor, Integer type, Integer normal,
            Integer exam) throws FenixServiceException, ExcepcaoPersistencia {
        final Set<OldRoom> rooms = OldRoom.findOldRoomsBySpecifiedArguments(name, building, floor, type, normal, exam);
        final List<InfoRoom> infoRooms = new ArrayList();
        for (final OldRoom room : rooms) {
            infoRooms.add(InfoRoomWithInfoInquiriesRoom.newInfoFromDomain(room));
        }
        return infoRooms;
    }

}