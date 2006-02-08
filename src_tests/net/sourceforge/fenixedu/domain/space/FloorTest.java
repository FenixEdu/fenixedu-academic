package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainTestBase;

public class FloorTest extends DomainTestBase {

    public void testFloorConstructor() {
        final Building building = new Building("building1");

        try {
            new Floor((Space) null, null);
            fail("Floor cannot be created without a surrounding space.");
        } catch (NullPointerException ex) {
            assertEquals("error.surrounding.space", ex.getMessage());
        }

        final Floor floor = new Floor(building, null);
        assertEquals(1, floor.getSpaceInformationsCount());
        final SpaceInformation spaceInformation = floor.getSpaceInformation();
        assertSame(FloorInformation.class, spaceInformation.getClass());
        assertSame(building, floor.getSuroundingSpace());
    }

    public void testGetSpaceInformation() {
        final Building building = new Building("building1");

        final Floor floor = new Floor(building, null);

        assertSame(FloorInformation.class, floor.getSpaceInformation().getClass());
        assertSame(FloorInformation.class, floor.getSpaceInformation(new YearMonthDay()).getClass());
    }

}
