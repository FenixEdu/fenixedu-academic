/*
 * SelectRooms.java
 *
 * Created on January 12th, 2003, 01:25
 */

package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;

public class SelectRooms {

    @Service
    public static Object run(InfoRoomEditor infoRoom) {

        List<AllocatableSpace> salas =
                AllocatableSpace.findActiveAllocatableSpacesBySpecifiedArguments(infoRoom.getNome(), infoRoom.getEdificio(),
                        infoRoom.getPiso(), infoRoom.getTipo(), infoRoom.getCapacidadeNormal(), infoRoom.getCapacidadeExame());

        if (salas == null) {
            return new ArrayList();
        }

        Iterator iter = salas.iterator();
        List salasView = new ArrayList();
        AllocatableSpace sala;

        while (iter.hasNext()) {
            sala = (AllocatableSpace) iter.next();
            if (sala.containsIdentification()) {
                salasView.add(InfoRoom.newInfoFromDomain(sala));
            }
        }

        return salasView;
    }

}