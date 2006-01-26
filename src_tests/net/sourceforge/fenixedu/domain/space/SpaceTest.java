package net.sourceforge.fenixedu.domain.space;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.DomainTestBase;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;

public class SpaceTest extends DomainTestBase {

    private class SpaceImpl extends Space {
    }

    private class SpaceInformationImpl extends SpaceInformation {
        SpaceInformationImpl(final Space space) {
            super();
            setSpace(space);
        }
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

}
