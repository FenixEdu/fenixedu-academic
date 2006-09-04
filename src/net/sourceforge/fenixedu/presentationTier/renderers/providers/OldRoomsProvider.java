package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class OldRoomsProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Set<OldRoom> oldRooms = new TreeSet<OldRoom>(OldRoom.OLD_ROOM_COMPARATOR_BY_NAME);
        oldRooms.addAll(OldRoom.getOldRooms());
        return oldRooms;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
