/*
 * Created on Aug 24, 2004
 *
 */
package DataBeans.Seminaries;

import DataBeans.DataTranferObject;

/**
 * @author João Mota
 *  
 */
public class CandidacyDTO extends DataTranferObject {

    private Integer candidacyId;

    private Integer number;

    private String name;

    private String username;

    private InfoClassification infoClassification;

    private String email;

    private Boolean approved;

    /**
     *  
     */
    public CandidacyDTO() {

    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the number.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number
     *            The number to set.
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return Returns the objectCode.
     */
    public Integer getCandidacyId() {
        return candidacyId;
    }

    /**
     * @param objectCode
     *            The objectCode to set.
     */
    public void setCandidacyId(Integer objectCode) {
        this.candidacyId = objectCode;
    }

    /**
     * @return Returns the infoClassification.
     */
    public InfoClassification getInfoClassification() {
        return infoClassification;
    }

    /**
     * @param infoClassification
     *            The infoClassification to set.
     */
    public void setInfoClassification(InfoClassification infoClassification) {
        this.infoClassification = infoClassification;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Returns the approved.
     */
    public Boolean getApproved() {
        return approved;
    }

    /**
     * @param approved
     *            The approved to set.
     */
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}