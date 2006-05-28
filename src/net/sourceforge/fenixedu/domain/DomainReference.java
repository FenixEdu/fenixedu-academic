package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * A <code>DomainReference</code> allows groups to refer to domain objects and still being
 * persisted in the database as value types. The <code>DomainReference</code> introduces an
 * indirection point between the group and the domain object and can be considerered as a typified
 * universal reference to domain objects.
 * 
 * @author cfgi
 */
public class DomainReference<T extends DomainObject> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    String className;
    Integer oid;

    transient Class type;
    transient T object;

    public DomainReference(T object) {
    	this.object = object;
    	this.type = object.getClass();
        this.className = object.getClass().getName();
        this.oid = object.getIdInternal();        
    }

    public Integer getOid() {
        return this.oid;
    }

    protected String getClassName() {
        return this.className;
    }
    
    public Class getType() {
        if (this.type != null) {
            return this.type;
        }
        else {
            try {
                return this.type = Class.forName(getClassName());
            } catch (ClassNotFoundException e) {
                throw new DomainException("reference.notFound.class", e, getClassName());
            }
        }
    }

    public T getObject() {
        if (this.object != null) { // cache
            return this.object;
        }

        ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
        
        this.object = (T) persistentObject.readByOID(getType(), getOid());

        return this.object;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DomainReference)) {
            return false;
        }

        DomainReference otherReference = (DomainReference) other;

        return this.getType().equals(otherReference.getType())
                && this.getOid().equals(otherReference.getOid());
    }

    @Override
    public int hashCode() {
        return this.getOid().hashCode() + this.getType().hashCode();
    }

}