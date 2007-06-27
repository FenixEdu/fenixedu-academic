package net.sourceforge.fenixedu.domain.space;

import java.util.Comparator;

public class SpaceComparator {

    public static Comparator<Space> SPACE_COMPARATOR_BY_CLASS = new Comparator<Space>() {

	public int compare(Space space1, Space space2) {
	    if (space1 == null || space2 == null) {
		throw new NullPointerException();
	    }
	    if (space1 == space2) {
		return 0;
	    }
	    final Class<? extends Space> class1 = space1.getClass();
	    final Class<? extends Space> class2 = space2.getClass();
	    if (class1 == class2) {
		return compareSpacesOfSameType(class1, space1, space2);
	    } else if (class1 == Campus.class) {
		return -1;
	    } else if (class2 == Campus.class) {
		return 1;
	    } else if (class1 == Building.class) {
		return -1;
	    } else if (class2 == Building.class) {
		return 1;
	    } else if (class1 == Floor.class) {
		return -1;
	    } else if (class2 == Floor.class) {
		return 1;
	    } else {
		throw new IllegalArgumentException("Unknown space types: " + class1 + " and " + class2);
	    }
	}

	private int compareSpacesOfSameType(final Class<? extends Space> clazz, final Space space1, final Space space2) {
	    if (clazz == Campus.class) {
		return Campus.COMPARATOR_BY_PRESENTATION_NAME.compare((Campus) space1, (Campus) space2);
	    } else if (clazz == Building.class) {
		return Building.COMPARATOR_BY_PRESENTATION_NAME.compare((Building) space1, (Building) space2);
	    } else if (clazz == Floor.class) {
		return Floor.FLOOR_COMPARATOR_BY_LEVEL.compare((Floor) space1, (Floor) space2);
	    } else if (clazz == Room.class) {
		return Room.COMPARATOR_BY_PRESENTATION_NAME.compare((Room) space1, (Room) space2);
	    } else if (clazz == RoomSubdivision.class) {
		return RoomSubdivision.COMPARATOR_BY_PRESENTATION_NAME.compare((RoomSubdivision) space1, (RoomSubdivision) space2);
	    } else {
		throw new IllegalArgumentException("Unknown space type: " + clazz);
	    }
	}
    };

}
