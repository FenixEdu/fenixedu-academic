package net.sourceforge.fenixedu.domain.space;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

public class RoomClassification extends RoomClassification_Base {

	public static final Comparator<RoomClassification> ROOM_CLASSIFICATION_COMPARATORY_BY_CODE = new BeanComparator("code");

	public static SortedSet<RoomClassification> sortByCode(final Collection<RoomClassification> roomClassifications) {
		final SortedSet<RoomClassification> sortedRoomClassifications = new TreeSet<RoomClassification>(ROOM_CLASSIFICATION_COMPARATORY_BY_CODE);
		sortedRoomClassifications.addAll(roomClassifications);
		return sortedRoomClassifications;
	}

	public RoomClassification() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
		setRootDomainObject(null);
		super.deleteDomainObject();
	}

}
