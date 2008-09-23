package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.OrderChange;

public class ChangeResultParticipationsOrder extends FenixService {
    public void run(ResultParticipation participation, OrderChange orderChange) {
	participation.movePersonToDesiredOrder(orderChange);
    }
}
