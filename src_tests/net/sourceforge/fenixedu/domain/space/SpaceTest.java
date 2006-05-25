package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.DomainTestBase;

import org.joda.time.YearMonthDay;

public class SpaceTest extends DomainTestBase {

    private class SpaceImpl extends Space {
    }

    private class SpaceInformationImpl extends SpaceInformation {
        SpaceInformationImpl(final Space space) {
            super();
            setSpace(space);
        }

//		@Override
//		public void createNewSpaceInformation() {
//		}
    }

    public void testSpaceConstructor() {
        // Ensure that the space constructor does not inittialize anything.
        // This is becouse any subclass will have specific associated types,
        // and because the constructor of this class must be a no-argument
        // constructor, anything else cannot be called by subclasses.
        final Space space = new SpaceImpl();
        assertTrue(space.getContainedSpaces().isEmpty());
        assertTrue(space.getSpaceInformations().isEmpty());
        assertTrue(space.getSpaceOccupations().isEmpty());
        assertNull(space.getSuroundingSpace());
    }

    public void testGetSpaceInformation() {
        final Space space = new SpaceImpl();

        final YearMonthDay today = new YearMonthDay();

        assertNull(space.getSpaceInformation());
        assertNull(space.getSpaceInformation(today));
        assertNull(space.getSpaceInformation(null));
        assertNull(space.getSpaceInformation(today.plusDays(1)));
        assertNull(space.getSpaceInformation(today.plusDays(-1)));

        final SpaceInformation spaceInformation = new SpaceInformationImpl(space);

        assertSame(spaceInformation, space.getSpaceInformation());
        assertSame(spaceInformation, space.getSpaceInformation(today));
        assertSame(spaceInformation, space.getSpaceInformation(null));
        assertSame(spaceInformation, space.getSpaceInformation(today.plusDays(1)));
        assertSame(spaceInformation, space.getSpaceInformation(today.plusDays(-1)));

        final SpaceInformation otherSpaceInformation = new SpaceInformationImpl(space);

        assertSame(otherSpaceInformation, space.getSpaceInformation());
        assertSame(otherSpaceInformation, space.getSpaceInformation(today));
        assertSame(otherSpaceInformation, space.getSpaceInformation(null));
        assertSame(otherSpaceInformation, space.getSpaceInformation(today.plusDays(1)));
        assertSame(spaceInformation, space.getSpaceInformation(today.plusDays(-1)));

        space.removeSpaceInformations(otherSpaceInformation);

        assertSame(spaceInformation, space.getSpaceInformation());
        assertSame(spaceInformation, space.getSpaceInformation(today));
        assertSame(spaceInformation, space.getSpaceInformation(null));
        assertSame(spaceInformation, space.getSpaceInformation(today.plusDays(1)));
        assertSame(spaceInformation, space.getSpaceInformation(today.plusDays(-1)));
    }

    public void testSpaceSpaceInformationListenerBeforeAdd() {
        final Space space = new SpaceImpl();
        final SpaceInformation spaceInformation = new SpaceInformationImpl(space);
    
        assertNull(spaceInformation.getValidUntil());

        final SpaceInformation otherSpaceInformation = new SpaceInformationImpl(space);

        assertNull(otherSpaceInformation.getValidUntil());
        assertEquals(new YearMonthDay(), spaceInformation.getValidUntil());        
    }

    public void testSpaceSpaceInformationListenerAfterRemove() {
        final Space space = new SpaceImpl();
        final SpaceInformation spaceInformation = new SpaceInformationImpl(space);
        space.removeSpaceInformations(spaceInformation);
    
        assertNull(spaceInformation.getValidUntil());

        space.addSpaceInformations(spaceInformation);
        final SpaceInformation otherSpaceInformation = new SpaceInformationImpl(space);
        space.removeSpaceInformations(otherSpaceInformation);

        assertNull(spaceInformation.getValidUntil());

        space.addSpaceInformations(otherSpaceInformation);
        space.removeSpaceInformations(spaceInformation);

        assertNull(otherSpaceInformation.getValidUntil());
    }

}
