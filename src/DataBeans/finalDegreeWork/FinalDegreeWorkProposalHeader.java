/*
 * Created on 2004/03/09
 *
 */
package DataBeans.finalDegreeWork;

import java.util.List;

import DataBeans.InfoObject;
import Util.FinalDegreeWorkProposalStatus;

/**
 * @author Luis Cruz
 *  
 */
public class FinalDegreeWorkProposalHeader extends InfoObject {

    private Integer proposalNumber;

    private Integer executionDegreeOID;

    private String executionYear;

    private String title;

    private Integer orientatorOID;

    private String orientatorName;

    private Integer coorientatorOID;

    private String coorientatorName;

    private String companyLink;

    private String degreeCode;

    private Boolean editable;

    private FinalDegreeWorkProposalStatus status;

    private List groupProposals;

    private InfoGroup groupAttributedByTeacher;

    private InfoGroup groupAttributed;

    private List branches;

    public FinalDegreeWorkProposalHeader() {
        super();
    }

    /**
     * @return Returns the companyLink.
     */
    public String getCompanyLink() {
        if (companyLink == null)
            return "";
        return companyLink;
    }

    /**
     * @param companyLink
     *            The companyLink to set.
     */
    public void setCompanyLink(String companyLink) {
        this.companyLink = companyLink;
    }

    /**
     * @return Returns the coorientatorName.
     */
    public String getCoorientatorName() {
        if (coorientatorName == null)
            return "";
        return coorientatorName;
    }

    /**
     * @param coorientatorName
     *            The coorientatorName to set.
     */
    public void setCoorientatorName(String coorientatorName) {
        this.coorientatorName = coorientatorName;
    }

    /**
     * @return Returns the coorientatorOID.
     */
    public Integer getCoorientatorOID() {
        return coorientatorOID;
    }

    /**
     * @param coorientatorOID
     *            The coorientatorOID to set.
     */
    public void setCoorientatorOID(Integer coorientatorOID) {
        this.coorientatorOID = coorientatorOID;
    }

    /**
     * @return Returns the orientatorName.
     */
    public String getOrientatorName() {
        return orientatorName;
    }

    /**
     * @param orientatorName
     *            The orientatorName to set.
     */
    public void setOrientatorName(String orientatorName) {
        this.orientatorName = orientatorName;
    }

    /**
     * @return Returns the orientatorOID.
     */
    public Integer getOrientatorOID() {
        return orientatorOID;
    }

    /**
     * @param orientatorOID
     *            The orientatorOID to set.
     */
    public void setOrientatorOID(Integer orientatorOID) {
        this.orientatorOID = orientatorOID;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the degreeCode.
     */
    public String getDegreeCode() {
        return degreeCode;
    }

    /**
     * @param degreeCode
     *            The degreeCode to set.
     */
    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }

    /**
     * @return Returns the editable.
     */
    public Boolean getEditable() {
        return editable;
    }

    /**
     * @param editable
     *            The editable to set.
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /**
     * @return Returns the proposalNumber.
     */
    public Integer getProposalNumber() {
        return proposalNumber;
    }

    /**
     * @param proposalNumber
     *            The proposalNumber to set.
     */
    public void setProposalNumber(Integer proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * @return Returns the status.
     */
    public FinalDegreeWorkProposalStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            The status to set.
     */
    public void setStatus(FinalDegreeWorkProposalStatus status) {
        this.status = status;
    }

    /**
     * @return Returns the groupProposals.
     */
    public List getGroupProposals() {
        return groupProposals;
    }

    /**
     * @param groupProposals
     *            The groupProposals to set.
     */
    public void setGroupProposals(List groupProposals) {
        this.groupProposals = groupProposals;
    }

    /**
     * @return Returns the groupAttributedByTeacher.
     */
    public InfoGroup getGroupAttributedByTeacher() {
        return groupAttributedByTeacher;
    }

    /**
     * @param groupAttributedByTeacher
     *            The groupAttributedByTeacher to set.
     */
    public void setGroupAttributedByTeacher(InfoGroup groupAttributedByTeacher) {
        this.groupAttributedByTeacher = groupAttributedByTeacher;
    }

    /**
     * @return Returns the groupAttributed.
     */
    public InfoGroup getGroupAttributed() {
        return groupAttributed;
    }

    /**
     * @param groupAttributed
     *            The groupAttributed to set.
     */
    public void setGroupAttributed(InfoGroup groupAttributed) {
        this.groupAttributed = groupAttributed;
    }

    /**
     * @return Returns the executionDegreeOID.
     */
    public Integer getExecutionDegreeOID() {
        return executionDegreeOID;
    }

    /**
     * @param executionDegreeOID
     *            The executionDegreeOID to set.
     */
    public void setExecutionDegreeOID(Integer executionDegreeOID) {
        this.executionDegreeOID = executionDegreeOID;
    }

    /**
     * @return Returns the branches.
     */
    public List getBranches() {
        return branches;
    }

    /**
     * @param branches
     *            The branches to set.
     */
    public void setBranches(List branches) {
        this.branches = branches;
    }

    public String getExecutionYear() {
        return executionYear;
    }
    

    public void setExecutionYear(String executionYear) {
        this.executionYear = executionYear;
    }
    
}