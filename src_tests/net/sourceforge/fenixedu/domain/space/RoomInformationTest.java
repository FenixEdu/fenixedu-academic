package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.DomainTestBase;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class RoomInformationTest extends DomainTestBase {

    private class SpaceImpl extends Space {
    }

    public void testRoomInformationConstructor() {
        try {
            new RoomInformation((OldRoom) null);
            fail("Expected a NullPointerException: room information must be relative to a room.");
        } catch (NullPointerException ex) {
            assertEquals("error.room.cannot.be.null", ex.getMessage());
        }

        final OldBuilding building = new OldBuilding("building1");

        final OldRoom room = new OldRoom(building);
        final RoomInformation roomInformation = (RoomInformation) room.getSpaceInformation();
        assertSame(room, roomInformation.getSpace());

        final RoomInformation newRoomInformation = new RoomInformation(room);
        assertSame(room, newRoomInformation.getSpace());
        assertSame(room, roomInformation.getSpace());
        assertEquals(new YearMonthDay(), roomInformation.getValidUntil());
        assertNull(newRoomInformation.getValidUntil());
    }

    public void testSetSpace() {
        final OldBuilding building = new OldBuilding("building1");

        final OldRoom room = new OldRoom(building);
        final RoomInformation roomInformation = room.getSpaceInformation();

        try {
            roomInformation.setSpace(null);
            fail("Expected a NullPointerException: room information must be relative to a room.");
        } catch (NullPointerException ex) {
            assertEquals("error.room.cannot.be.null", ex.getMessage());
        }

        try {
            roomInformation.setSpace(room);
            fail("Expected a DomainException: cannot change the room of a room information.");
        } catch (DomainException ex) {
            assertEquals("error.cannot.change.room", ex.getMessage());
        }

        try {
            roomInformation.setSpace(new OldRoom(building));
            fail("Expected a DomainException: cannot change the room of a room information.");
        } catch (DomainException ex) {
            assertEquals("error.cannot.change.room", ex.getMessage());
        }

        try {
            roomInformation.setSpace(new SpaceImpl());
            fail("Expected a DomainException: cannot associate a room information with a space that is not a room.");
        } catch (DomainException ex) {
            assertEquals("error.incompatible.space", ex.getMessage());
        }
    }

}
