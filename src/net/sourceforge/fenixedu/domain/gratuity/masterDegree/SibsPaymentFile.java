/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFile extends DomainObject implements ISibsPaymentFile {

    private String filename;

    private List sibsPaymentFileEntries;

    public SibsPaymentFile() {
    }

    /**
     * @param filename
     */
    public SibsPaymentFile(String filename) {
        super();
        this.filename = filename;
    }

    /**
     * @return Returns the sibsFileEntries.
     */
    public List getSibsPaymentFileEntries() {
        return sibsPaymentFileEntries;
    }

    /**
     * @param sibsFileEntries
     *            The sibsFileEntries to set.
     */
    public void setSibsPaymentFileEntries(List sibsPaymentFileEntries) {
        this.sibsPaymentFileEntries = sibsPaymentFileEntries;
    }

    /**
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     *            The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if ((obj instanceof ISibsPaymentFile)) {
            ISibsPaymentFile sibsFile = (ISibsPaymentFile) obj;
            if ((sibsFile.getFilename() != null) && (this.filename.equals(sibsFile.getFilename()))) {
                result = true;
            }
        }

        return result;
    }

    public String toString() {

        String result = "[" + this.getClass().getName() + ": \n";
        result += "idInternal = " + getIdInternal() + "; \n";
        result += "filename = " + this.filename.toString() + "; \n";
        result += "] \n";

        return result;
    }
}