/*
 * Created on 1:57:40 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 1:57:40 PM, Mar 11, 2005
 */
public class InfoExternalPersonInfo
{
    private String name;
    private String sex;
    private String birthday;
    private String nationality;
    private InfoExternalCitizenshipInfo citizenship;
    private InfoExternalAdressInfo address;
    private String phone;
    private String celularPhone;
    private String email;
    private InfoExternalIdentificationInfo identification;
    private String fiscalNumber;
    private String accountIdentificationNumber;
    private String username;
    /**
     * @return Returns the accountIdentificationNumber.
     */
    public String getAccountIdentificationNumber()
    {
        return this.accountIdentificationNumber;
    }
    /**
     * @param accountIdentificationNumber The accountIdentificationNumber to set.
     */
    public void setAccountIdentificationNumber(String accountIdentificationNumber)
    {
        this.accountIdentificationNumber = accountIdentificationNumber;
    }
    /**
     * @return Returns the address.
     */
    public InfoExternalAdressInfo getAddress()
    {
        return this.address;
    }
    /**
     * @param address The address to set.
     */
    public void setAddress(InfoExternalAdressInfo address)
    {
        this.address = address;
    }
    /**
     * @return Returns the birthday.
     */
    public String getBirthday()
    {
        return this.birthday;
    }
    /**
     * @param birthday The birthday to set.
     */
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }
    /**
     * @return Returns the citizenship.
     */
    public InfoExternalCitizenshipInfo getCitizenship()
    {
        return this.citizenship;
    }
    /**
     * @param citizenship The citizenship to set.
     */
    public void setCitizenship(InfoExternalCitizenshipInfo citizenship)
    {
        this.citizenship = citizenship;
    }
    /**
     * @return Returns the email.
     */
    public String getEmail()
    {
        return this.email;
    }
    /**
     * @param email The email to set.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    /**
     * @return Returns the fiscalNumber.
     */
    public String getFiscalNumber()
    {
        return this.fiscalNumber;
    }
    /**
     * @param fiscalNumber The fiscalNumber to set.
     */
    public void setFiscalNumber(String fiscalNumber)
    {
        this.fiscalNumber = fiscalNumber;
    }
    /**
     * @return Returns the identification.
     */
    public InfoExternalIdentificationInfo getIdentification()
    {
        return this.identification;
    }
    /**
     * @param identification The identification to set.
     */
    public void setIdentification(InfoExternalIdentificationInfo identification)
    {
        this.identification = identification;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the nationality.
     */
    public String getNationality()
    {
        return this.nationality;
    }
    /**
     * @param nationality The nationality to set.
     */
    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }
    /**
     * @return Returns the phone.
     */
    public String getPhone()
    {
        return this.phone;
    }
    /**
     * @param phone The phone to set.
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    /**
     * @return Returns the sex.
     */
    public String getSex()
    {
        return this.sex;
    }
    /**
     * @param sex The sex to set.
     */
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    /**
     * @return Returns the celularPhone.
     */
    public String getCelularPhone()
    {
        return this.celularPhone;
    }
    /**
     * @param celularPhone The celularPhone to set.
     */
    public void setCelularPhone(String celularPhone)
    {
        this.celularPhone = celularPhone;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
