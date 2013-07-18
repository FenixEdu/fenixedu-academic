package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.RoomClassification;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteRoomClassification {

    @Service
    public static void run(final RoomClassification roomClassification) {
        roomClassification.delete();
    }

}