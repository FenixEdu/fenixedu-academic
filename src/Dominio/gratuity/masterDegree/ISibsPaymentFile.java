/*
 * Created on Apr 26, 2004
 *
 */
package Dominio.gratuity.masterDegree;

import java.util.List;

import Dominio.IDomainObject;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface ISibsPaymentFile extends IDomainObject {

    /**
     * @return Returns the sibsPaymentFileEntries.
     */
    public abstract List getSibsPaymentFileEntries();

    /**
     * @param sibsPaymentFileEntries
     *            The sibsPaymentFileEntries to set.
     */
    public abstract void setSibsPaymentFileEntries(List sibsPaymentFileEntries);

    /**
     * @return Returns the filename.
     */
    public abstract String getFilename();

    /**
     * @param filename
     *            The filename to set.
     */
    public abstract void setFilename(String filename);
}