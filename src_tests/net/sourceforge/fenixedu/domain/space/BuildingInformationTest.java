package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.DomainTestBase;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class BuildingInformationTest extends DomainTestBase {

    private class SpaceImpl extends Space {
    }

    public void testBuildingInformationConstructor() {
        try {
            new BuildingInformation(null, "");
            fail("Expected a NullPointerException: building information must be relative to a building.");
        } catch (NullPointerException ex) {
            assertEquals("error.building.cannot.be.null", ex.getMessage());
        }

        final OldBuilding building = new OldBuilding("Some building");
        final BuildingInformation buildingInformation = (BuildingInformation) building.getSpaceInformation();
        assertSame(building, buildingInformation.getSpace());
        assertEquals("Some building", buildingInformation.getName());

        try {
            new BuildingInformation(building, null);
            fail("Expected a NullPointerException: building name cannot be null.");
        } catch (NullPointerException ex) {
            assertEquals("error.building.name.cannot.be.null", ex.getMessage());
        }

        final BuildingInformation newBuildingInformation = new BuildingInformation(building, "Some other name");
        assertSame(building, newBuildingInformation.getSpace());
        assertSame(building, buildingInformation.getSpace());
        assertEquals(new YearMonthDay(), buildingInformation.getValidUntil());
        assertNull(newBuildingInformation.getValidUntil());
    }

    public void testSetName() {
        final OldBuilding building = new OldBuilding("Some building");
        final BuildingInformation buildingInformation = (BuildingInformation) building.getSpaceInformation();

        try {
            buildingInformation.setName(null);
            fail("Expected a NullPointerException: building name cannot be null.");
        } catch (NullPointerException ex) {
            assertEquals("error.building.name.cannot.be.null", ex.getMessage());
        }

        buildingInformation.setName("Some other name");
        assertEquals("Some other name", buildingInformation.getName());
    }

    public void testSetSpace() {
        final OldBuilding building = new OldBuilding("Some building");
        final BuildingInformation buildingInformation = building.getSpaceInformation();

        try {
            buildingInformation.setSpace(null);
            fail("Expected a NullPointerException: building information must be relative to a building.");
        } catch (NullPointerException ex) {
            assertEquals("error.building.cannot.be.null", ex.getMessage());
        }

        try {
            buildingInformation.setSpace(building);
            fail("Expected a DomainException: cannot change the building of a building information.");
        } catch (DomainException ex) {
            assertEquals("error.cannot.change.building", ex.getMessage());
        }

        try {
            buildingInformation.setSpace(new OldBuilding("Some other building"));
            fail("Expected a DomainException: cannot change the building of a building information.");
        } catch (DomainException ex) {
            assertEquals("error.cannot.change.building", ex.getMessage());
        }

        try {
            buildingInformation.setSpace(new SpaceImpl());
            fail("Expected a DomainException: cannot associate a building information with a space that is not a building.");
        } catch (DomainException ex) {
            assertEquals("error.incompatible.space", ex.getMessage());
        }
    }

}
