/*
 * Created on 9/Out/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author: - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *          (naat@mega.ist.utl.pt)
 *  
 */
public class ExternalPerson extends DomainObject implements IExternalPerson {
    // database internal codes
    private Integer keyPerson;

    private Integer keyWorkLocation;

    //fields
    private IPerson person;

    private IWorkLocation workLocation;

    /**
     * Creates a new instance of ExternalPerson
     */
    public ExternalPerson() {
    }

    /**
     * Creates a new instance of ExternalPerson
     */
    public ExternalPerson(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public void setKeyPerson(Integer keyPerson) {
        this.keyPerson = keyPerson;
    }

    public Integer getKeyPerson() {
        return keyPerson;
    }

    public void setPerson(IPerson person) {
        this.person = person;
    }

    public IPerson getPerson() {
        return person;
    }

    public void setWorkLocation(IWorkLocation workLocation) {
        this.workLocation = workLocation;
    }

    public IWorkLocation getWorkLocation() {
        return workLocation;
    }

    /**
     * @return Returns the keyWorkLocation.
     */
    public Integer getKeyWorkLocation() {
        return keyWorkLocation;
    }

    /**
     * @param keyWorkLocation
     *            The keyWorkLocation to set.
     */
    public void setKeyWorkLocation(Integer keyWorkLocation) {
        this.keyWorkLocation = keyWorkLocation;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IExternalPerson) {
            IExternalPerson externalPerson = (IExternalPerson) obj;
            result = this.getPerson().equals(externalPerson.getPerson());
        }
        return result;
    }
}