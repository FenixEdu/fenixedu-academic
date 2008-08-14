package net.sourceforge.fenixedu.domain;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * A <code>MetaDomainObject</code> represents the type of a specific
 * <code>DomainObject</code>. It's the reification of the domain object's type.
 * 
 * @author cfgi
 */
public class MetaDomainObject extends MetaDomainObject_Base {

    private Class concreteType;

    public MetaDomainObject(Class type) {
	super();

	setType(type.getName());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public MetaDomainObject(String type) throws ClassNotFoundException {
	this(Class.forName(type));
    }

    /**
     * @return the class which name is stored in {@link #getType()}
     */
    public Class getConcreteType() {
	try {
	    if (this.concreteType == null) {
		this.concreteType = Class.forName(getType());
	    }

	    return this.concreteType;
	} catch (ClassNotFoundException e) {
	    throw new DomainException("domain.meta.invalidType", e);
	}
    }

    public boolean isPortalAvailable() {
	return !getPortals().isEmpty();
    }

    public Portal getAssociatedPortal() {
	return getPortals().get(0);
    }

    /**
     * Retrieves a specific meta domain object, if available, for the given
     * type. This method searches in all existing meta objects for the meta
     * object that matches the given type.
     * 
     * <p>
     * Any MetaDomainObject returned by this method will return the given type
     * as the result of {@link #getConcreteType()}.
     * 
     * @param type
     *            a class the extends {@link DomainObject}
     * 
     * @return a MetaDomainObject that is the reification of the given type of
     *         <code>null</code> if none is available
     */
    private static final Map<Class, MetaDomainObject> metaDomainObjectMap = new HashMap<Class, MetaDomainObject>();

    public static MetaDomainObject getMeta(Class type) {
	final MetaDomainObject result = metaDomainObjectMap.get(type);
	if (result != null && result.getRootDomainObject() == RootDomainObject.getInstance() && type == result.getConcreteType()) {
	    return result;
	}

	for (final MetaDomainObject metaDomainObject : RootDomainObject.getInstance().getMetaDomainObjects()) {
	    final Class concreteType = metaDomainObject.getConcreteType();
	    if (type == concreteType) {
		metaDomainObjectMap.put(concreteType, metaDomainObject);
		return metaDomainObject;
	    }
	    if (!metaDomainObjectMap.containsKey(concreteType)) {
		metaDomainObjectMap.put(concreteType, metaDomainObject);
	    }
	}

	return null;
    }

    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.metaDomainObject.cannot.be.deleted");
	}
	setRootDomainObject(null);
	super.deleteDomainObject();
    }

    public boolean canBeDeleted() {
	return !hasAnyFunctionalityParameters() && !hasAnyPortals();
    }
}
