package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.dataTransferObject.InfoBuilding;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import pt.ist.fenixframework.Atomic;

public class ReadBuildings {

    @Atomic
    public static List<InfoBuilding> run() {
        final List<InfoBuilding> result = new ArrayList<InfoBuilding>();
        for (final Space building : SpaceUtils.buildings()) {
            result.add(InfoBuilding.newInfoFromDomain(building));
        }
        return result;
    }

}