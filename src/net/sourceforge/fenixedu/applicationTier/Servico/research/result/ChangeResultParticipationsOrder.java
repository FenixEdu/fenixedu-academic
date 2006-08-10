package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;

public class ChangeResultParticipationsOrder extends Service {    
    /**
     * The type of order changes can have in authors list.
     */
    public static enum OrderChange {
        MoveUp,
        MoveDown,
        MoveTop,
        MoveBottom,
    }
    
    /**
     * Changes the order position in the list according to the
     * order change.
     */
    public void run(ResultParticipation participation, OrderChange orderChange) {
        switch (orderChange) {
        case MoveUp:
            participation.moveUp();
            break;
        case MoveDown:
            participation.moveDown();
            break;
        case MoveTop:
            participation.moveTop();
            break;
        case MoveBottom:
            participation.moveBottom();
            break;
        }
        participation.setChangedBy();
    }
}
