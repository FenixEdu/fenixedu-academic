/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.Transaction;
import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base {

    static final public Comparator<DomainObject> COMPARATOR_BY_ID = new Comparator<DomainObject>() {
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

    public boolean isDeleted() {
	return getRootDomainObject() == null;
    }

    protected abstract RootDomainObject getRootDomainObject();

    protected final void deleteDomainObject() {
	if (!checkDisconnected()) {
	    if (ERROR_IF_DELETED_OBJECT_NOT_DISCONNECTED) {
		throw new Error("Trying to delete a DomainObject that is still connected to other objects: " + this);
	    } else {
		System.err.println("WARNING: Deleting a DomainObject that is still connected to other objects: " + this);
	    }
	}

	Transaction.deleteObject(this);
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
    
    protected void check(final Object obj, final String message, final String ...args) {
	if (obj == null) {
	    throw new DomainException(message, args);
	}
    }
    
    protected void check(final String obj, final String message, final String ...args) {
	if (obj == null || obj.isEmpty()) {
	    throw new DomainException(message, args);
	}
    }

    public final String getExternalId() {
        return String.valueOf(getOID());
    }

    public static <T extends DomainObject> T fromExternalId(String extId) {
        if (extId == null) {
            return null;
        } else {
            return (T) Transaction.getObjectForOID(Long.parseLong(extId));
        }
    }
}
