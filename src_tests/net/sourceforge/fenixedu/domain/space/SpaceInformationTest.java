package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.DomainTestBase;

public class SpaceInformationTest extends DomainTestBase {

    private class SpaceInformationImpl extends SpaceInformation {
//		@Override
//		public void createNewSpaceInformation() {
//		}
    }

    public void testSpaceInformationConstructor() {
        // Ensure that the space information constructor does not inittialize anything.
        // This is becouse any subclass will have specific associated types,
        // and because the constructor of this class must be a no-argument
        // constructor, anything else cannot be called by subclasses.
        final SpaceInformation spaceInformation = new SpaceInformationImpl();
        assertNull(spaceInformation.getSpace());
        assertNull(spaceInformation.getValidUntil());
    }

}
