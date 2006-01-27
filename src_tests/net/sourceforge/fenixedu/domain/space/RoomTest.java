package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainTestBase;

public class RoomTest extends DomainTestBase {

    public void testRoomConstructor() {
        final OldBuilding building = new OldBuilding("building1");

        try {
            new OldRoom((Space) null);
            fail("OldRoom cannot be created without a surrounding space.");
        } catch (NullPointerException ex) {
            assertEquals("error.surrounding.space", ex.getMessage());
        }

        final OldRoom room = new OldRoom(building);
        assertEquals(1, room.getSpaceInformationsCount());
        final SpaceInformation spaceInformation = room.getSpaceInformation();
        assertSame(RoomInformation.class, spaceInformation.getClass());
        assertSame(building, room.getSuroundingSpace());
    }

    public void testGetSpaceInformation() {
        final OldBuilding building = new OldBuilding("building1");

        final OldRoom room = new OldRoom(building);

        assertSame(RoomInformation.class, room.getSpaceInformation().getClass());
        assertSame(RoomInformation.class, room.getSpaceInformation(new YearMonthDay()).getClass());
    }

}
