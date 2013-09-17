package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.Building;
import pt.ist.fenixframework.Atomic;

public class ReadBuildings {

    @Atomic
    public static List<InfoBuilding> run() {
        final List<InfoBuilding> result = new ArrayList<InfoBuilding>();
        for (final Building building : Building.getAllActiveBuildings()) {
            result.add(InfoBuilding.newInfoFromDomain(building));
        }
        return result;
    }

}