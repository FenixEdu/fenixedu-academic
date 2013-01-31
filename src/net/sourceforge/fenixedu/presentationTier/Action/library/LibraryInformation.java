package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class LibraryInformation implements Serializable {
	public static class PlaceProvider extends AbstractDomainObjectProvider {
		@Override
		public Object provide(Object source, Object currentValue) {
			LibraryInformation attendance = (LibraryInformation) source;
			Set<Space> availableSpaces = new HashSet<Space>();
			for (Space space : attendance.getLibrary().getActiveContainedSpaces()) {
				if (space.canAddAttendance()) {
					availableSpaces.add(space);
				}
			}
			return availableSpaces;
		}
	}

	public static class RoleTypeProvider implements DataProvider {
		@Override
		public Object provide(Object source, Object currentValue) {
			List<RoleType> roles = new ArrayList<RoleType>();
			roles.add(RoleType.STUDENT);
			roles.add(RoleType.TEACHER);
			roles.add(RoleType.EMPLOYEE);
			roles.add(RoleType.GRANT_OWNER);
			roles.add(RoleType.ALUMNI);
			return roles;
		}

		@Override
		public Converter getConverter() {
			return new EnumConverter(RoleType.class);
		}
	}

	private Space library;

	private int capacity;

	private int lockers;

	public LibraryInformation() {
	}

	public LibraryInformation(Space library) {
		setLibrary(library);
	}

	public Space getLibrary() {
		return library;
	}

	public void setLibrary(Space library) {
		this.library = library;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setLockers(int lockers) {
		this.lockers = lockers;
	}

	public int getLockers() {
		return lockers;
	}

}