package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainTestBase;

public class BuildingTest extends DomainTestBase {

    public void testBuildingConstructor() {
        try {
            new Building((String) null);
            fail("Building name cannot be null.");
        } catch (NullPointerException ex) {
            // all is ok
        }

        final Building building = new Building("Some name");
        assertEquals(1, building.getSpaceInformationsCount());
        final SpaceInformation spaceInformation = building.getSpaceInformation();
        assertSame(BuildingInformation.class, spaceInformation.getClass());
        final BuildingInformation buildingInformation = (BuildingInformation) spaceInformation;
        assertEquals("Some name", buildingInformation.getName());
    }

    public void testGetSpaceInformation() {
        final Building building = new Building("Some name");
        assertSame(BuildingInformation.class, building.getSpaceInformation().getClass());
        assertSame(BuildingInformation.class, building.getSpaceInformation(new YearMonthDay()).getClass());
    }

}
