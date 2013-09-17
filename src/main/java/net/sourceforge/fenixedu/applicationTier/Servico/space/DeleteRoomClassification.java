package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.domain.space.RoomClassification;
import pt.ist.fenixframework.Atomic;

public class DeleteRoomClassification {

    @Atomic
    public static void run(final RoomClassification roomClassification) {
        roomClassification.delete();
    }

}