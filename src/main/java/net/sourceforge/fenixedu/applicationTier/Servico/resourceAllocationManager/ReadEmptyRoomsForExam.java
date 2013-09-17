package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.NotImplementedException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.apache.commons.collections.Transformer;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadEmptyRoomsForExam {

    @Atomic
    public static List run(InfoExam infoExam) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        List availableInfoRooms = null;

        Transformer TRANSFORM_TO_INFOROOM = new Transformer() {
            @Override
            public Object transform(Object input) {
                return InfoRoom.newInfoFromDomain((AllocatableSpace) input);
            }
        };

        // TODO : checkthis
        throw new NotImplementedException();
        // List availableRooms =
        // persistentRoom.readAvailableRooms(infoExam.getExternalId());
        // availableInfoRooms = (List) CollectionUtils.collect(availableRooms,
        // TRANSFORM_TO_INFOROOM);
        // return availableInfoRooms;
    }

}