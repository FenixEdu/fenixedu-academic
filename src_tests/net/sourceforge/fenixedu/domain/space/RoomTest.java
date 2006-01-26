package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainTestBase;

public class RoomTest extends DomainTestBase {

    public void testRoomConstructor() {
        final Building building = new Building("building1");

        try {
            new Room((Space) null);
            fail("Room cannot be created without a surrounding space.");
        } catch (NullPointerException ex) {
            assertEquals("error.surrounding.space", ex.getMessage());
        }

        final Room room = new Room(building);
        assertEquals(1, room.getSpaceInformationsCount());
        final SpaceInformation spaceInformation = room.getSpaceInformation();
        assertSame(RoomInformation.class, spaceInformation.getClass());
        assertSame(building, room.getSuroundingSpace());
    }

    public void testGetSpaceInformation() {
        final Building building = new Building("building1");

        final Room room = new Room(building);

        assertSame(RoomInformation.class, room.getSpaceInformation().getClass());
        assertSame(RoomInformation.class, room.getSpaceInformation(new YearMonthDay()).getClass());
    }

}
