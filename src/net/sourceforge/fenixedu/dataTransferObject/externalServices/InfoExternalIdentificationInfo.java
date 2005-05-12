/*
 * Created on 2:33:23 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:33:23 PM, Mar 11, 2005
 */
public class InfoExternalIdentificationInfo
{
    private String documentType;
    private String number;
    private String expiryDate;
    private String emitionDate;
    private String emitionLocal;
    
    public String getEmitionDate()
    {
        return this.emitionDate;
    }
    
    public void setEmitionDate(String emitionDate)
    {
        this.emitionDate = emitionDate;
    }
    
    public String getEmitionLocal()
    {
        return this.emitionLocal;
    }
    
    public void setEmitionLocal(String emitionLocal)
    {
        this.emitionLocal = emitionLocal;
    }
    
    public String getExpiryDate()
    {
        return this.expiryDate;
    }
    
    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }
    /**
     * @return Returns the documentType.
     */
    public String getDocumentType()
    {
        return this.documentType;
    }
    /**
     * @param documentType The documentType to set.
     */
    public void setDocumentType(String documentType)
    {
        this.documentType = documentType;
    }
    /**
     * @return Returns the number.
     */
    public String getNumber()
    {
        return this.number;
    }
    /**
     * @param number The number to set.
     */
    public void setNumber(String number)
    {
        this.number = number;
    }
}
