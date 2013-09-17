package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.dataTransferObject.spaceManager.MoveSpaceBean;
import net.sourceforge.fenixedu.domain.space.Space;
import pt.ist.fenixframework.Atomic;

public class MoveSpace {

    @Atomic
    public static void run(MoveSpaceBean bean) {
        if (bean != null && bean.getSpace() != null) {
            Space space = bean.getSpace();
            space.setNewPossibleParentSpace(bean.getSelectedParentSpace());
        }
    }
}