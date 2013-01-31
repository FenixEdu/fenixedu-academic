/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.StringAppender;
import dml.DomainClass;
import dml.DomainModel;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base {

	static final public Comparator<DomainObject> COMPARATOR_BY_ID = new Comparator<DomainObject>() {
		@Override
		public int compare(DomainObject o1, DomainObject o2) {
			return o1.getIdInternal().compareTo(o2.getIdInternal());
		}
	};

	private static final boolean ERROR_IF_DELETED_OBJECT_NOT_DISCONNECTED = PropertiesManager
			.getBooleanProperty("error.if.deleted.object.not.disconnected");

	public DomainObject() {
		super();
	}

	@Override
	protected final void ensureIdInternal() {
		try {
			super.ensureIdInternal();
		} catch (UnableToDetermineIdException t) {
			if (LogLevel.WARN) {
				System.out.println("Something went wrong when initializing the idInternal.  Not setting it...");
			}
			throw t;
		}
	}

	@Override
	public boolean isDeleted() {
		return getRootDomainObject() == null;
	}

	protected abstract RootDomainObject getRootDomainObject();

	/*
	 * This method is ugly but very useful when attempting to delete objects with allot of roles.
	 * If you have some free time on your hands think about doing this with the base code generator.
	 */
	private void displayConnectedRole() {
		final DomainModel domainModel = FenixFramework.getDomainModel();
		final DomainClass domainClass = domainModel.findClass(getClass().getName());
		for (final dml.Role role : domainClass.getRoleSlotsList()) {
			final String roleName = role.getName();
			try {
				final Method method =
						getClass().getMethod("get" + Character.toUpperCase(roleName.charAt(0)) + roleName.substring(1));
				final Object result = method.invoke(this);
				if (result != null && (!(result instanceof Collection) || !((Collection) result).isEmpty())) {
					System.out.println("Still connected to " + roleName);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	protected String getCurrentUser() {
		if (AccessControl.getUserView() != null) {
			return AccessControl.getUserView().getUtilizador();
		} else {
			return System.getProperty("user.name", "FENIX");
		}
	}

	@Override
	public final String toString() {
		return StringAppender.append(getClass().getName(), "(", getIdInternal().toString(), ")");
	}

	/**
	 * This method allows you to obtains the reification of this object's type.
	 * Note that the corresponding reification may no yet exist and, thus, this
	 * method may return <code>null</code>.
	 * 
	 * @return the {@link MetaDomainObject} that represents this object's type.
	 */
	public MetaDomainObject getMeta() {
		return MetaDomainObject.getMeta(getClass());
	}

	public void check(final Object obj, final String message, final String... args) {
		if (obj == null) {
			throw new DomainException(message, args);
		}
	}

	protected void check(final String obj, final String message, final String... args) {
		if (obj == null || obj.isEmpty()) {
			throw new DomainException(message, args);
		}
	}

	protected void check(final Object[] obj, final String message, final String... args) {
		if (obj == null || obj.length == 0) {
			throw new DomainException(message, args);
		}
	}

	protected void check(final Collection<Object> obj, final String message, final String... args) {
		if (obj == null || obj.size() == 0) {
			throw new DomainException(message, args);
		}
	}
}
