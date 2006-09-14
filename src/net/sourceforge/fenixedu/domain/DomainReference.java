package net.sourceforge.fenixedu.domain;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * A <code>DomainReference</code> allows a serializable object to refer to a domain object. 
 * The <code>DomainReference</code> introduces an indirection point between the holder object 
 * and the domain object that avoids any data from the domain object to be stored in the 
 * serialization point.
 * 
 * @author cfgi
 */
public class DomainReference<T extends DomainObject> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String className; // is also used to know when we are storing the null value
    private Integer oid;

    transient Class type; // chached type, transient to protect from class hierarchy changes
    transient T object;

    public DomainReference(T object) {
        if (object == null) {
            this.object = null;
            this.type = null;
            this.className = null;
            this.oid = null;
        }
        else {
        	this.object = object;
        	this.type = object.getClass();
            this.className = object.getClass().getName();
            this.oid = object.getIdInternal();
        }
    }

    public Integer getOid() {
        return this.oid;
    }

    protected String getClassName() {
        return this.className;
    }
    
    public Class getType() {
        if (this.className == null) {
            return null;
        }
        
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

        if (this.className == null) { // null object
            return null;
        }
        
        ISuportePersistente persistenceSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = persistenceSupport.getIPersistentObject();
        
        this.object = (T) persistentObject.readByOID(getType(), getOid());

        return this.object;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof DomainReference)) {
            return false;
        }

        DomainReference otherReference = (DomainReference) other;

        if (this.getOid() == null && otherReference.getOid() != null) {
            return false;
        }
        
        if (this.getOid() != null && !this.getOid().equals(otherReference.getOid())) {
            return false;
        }
        
        if (this.getType() == null && otherReference.getType() != null) {
            return false;
        }
        
        if (this.getType() != null && !this.getType().equals(otherReference.getType())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int oidHash;
        int typeHash;
        
        oidHash  = getOid() == null ? 0 : getOid().hashCode();
        typeHash = getType() == null ? 0 : getType().hashCode();
        
        return oidHash + typeHash;
    }

}