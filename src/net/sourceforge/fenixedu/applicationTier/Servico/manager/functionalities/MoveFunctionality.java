package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

/**
 * This service is used to change the structure of the functionality tree.
 * 
 * @author cfgi
 */
public class MoveFunctionality extends Service {
    
    /**
     * The type of movement a functionality can have in the tree.
     * 
     * @author cfgi
     */
    public static enum Movement {
        Up,
        Down,
        Top,
        Bottom,
        Left,
        Right
    }

    /**
     * Changes the functionality position in the tree according to the
     * given movement.
     * 
     * @param functionality the target functionality
     * @param movement the typeof movement
     * 
     * @see Functionality#moveUp()
     * @see Functionality#moveDown()
     * @see Functionality#moveTop()
     * @see Functionality#moveBottom()
     * @see Functionality#moveInner()
     * @see Functionality#moveOutter()
     */
    public void run(Functionality functionality, Movement movement) {
        switch (movement) {
        case Up:
            functionality.moveUp();
            break;
        case Down:
            functionality.moveDown();
            break;
        case Top:
            functionality.moveTop();
            break;
        case Bottom:
            functionality.moveBottom();
            break;
        case Left:
            functionality.moveOutter();
            break;
        case Right:
            functionality.moveInner();
            break;
        }
    }
}
