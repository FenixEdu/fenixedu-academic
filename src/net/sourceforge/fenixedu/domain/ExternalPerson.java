/*
 * Created on 9/Out/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author: - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *          (naat@mega.ist.utl.pt)
 */
public class ExternalPerson extends ExternalPerson_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IExternalPerson) {
            final IExternalPerson externalPerson = (IExternalPerson) obj;
            return this.getIdInternal().equals(externalPerson.getIdInternal());
        }
        return false;
    }

}
