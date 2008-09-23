package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteRoomClassification extends FenixService {

    public void run(final RoomClassification roomClassification) {
	roomClassification.delete();
    }

}
