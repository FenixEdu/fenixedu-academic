package net.sourceforge.fenixedu.applicationTier.Servico.space;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType;
import pt.ist.fenixframework.Atomic;

public class SpaceAccessGroupsManagement {

    @Atomic
    public static void run(Space space, SpaceAccessGroupType accessGroupType, boolean toAdd, boolean isToMaintainElements,
            String expression) throws FenixServiceException {
        space.addOrRemovePersonFromAccessGroup(accessGroupType, Boolean.valueOf(toAdd), Boolean.valueOf(isToMaintainElements),
                expression);
    }
}