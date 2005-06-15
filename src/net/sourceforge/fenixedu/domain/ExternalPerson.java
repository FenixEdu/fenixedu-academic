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
        boolean result = false;
        if (obj instanceof IExternalPerson) {
            IExternalPerson externalPerson = (IExternalPerson) obj;
            result = this.getPerson().equals(externalPerson.getPerson());
        }
        return result;
    }

}
