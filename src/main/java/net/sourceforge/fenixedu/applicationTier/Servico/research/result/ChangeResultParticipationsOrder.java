package net.sourceforge.fenixedu.applicationTier.Servico.research.result;


import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.OrderChange;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeResultParticipationsOrder {
    @Service
    public static void run(ResultParticipation participation, OrderChange orderChange) {
        participation.movePersonToDesiredOrder(orderChange);
    }
}