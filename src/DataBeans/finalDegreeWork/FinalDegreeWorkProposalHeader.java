/*
 * Created on 2004/03/09
 *
 */
package DataBeans.finalDegreeWork;

import Util.FinalDegreeWorkProposalStatus;
import DataBeans.InfoObject;

/**
 * @author Luis Cruz
 *
 */
public class FinalDegreeWorkProposalHeader extends InfoObject {

	private Integer proposalNumber;
	private String title;
	private Integer orientatorOID;
	private String orientatorName;
	private Integer coorientatorOID;
	private String coorientatorName;
	private String companyLink;
	private String degreeCode;
	private Boolean editable;
	private FinalDegreeWorkProposalStatus status;

	public FinalDegreeWorkProposalHeader() {
		super();
	}

	/**
	 * @return Returns the companyLink.
	 */
	public String getCompanyLink() {
		if (companyLink == null) return "";
		return companyLink;
	}

	/**
	 * @param companyLink The companyLink to set.
	 */
	public void setCompanyLink(String companyLink) {
		this.companyLink = companyLink;
	}

	/**
	 * @return Returns the coorientatorName.
	 */
	public String getCoorientatorName() {
		if (coorientatorName == null) return "";
		return coorientatorName;
	}

	/**
	 * @param coorientatorName The coorientatorName to set.
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
	 * @param coorientatorOID The coorientatorOID to set.
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
	 * @param orientatorName The orientatorName to set.
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
	 * @param orientatorOID The orientatorOID to set.
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
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the degreeCode.
	 */
	public String getDegreeCode()
	{
		return degreeCode;
	}

	/**
	 * @param degreeCode The degreeCode to set.
	 */
	public void setDegreeCode(String degreeCode)
	{
		this.degreeCode = degreeCode;
	}

	/**
	 * @return Returns the editable.
	 */
	public Boolean getEditable()
	{
		return editable;
	}

	/**
	 * @param editable The editable to set.
	 */
	public void setEditable(Boolean editable)
	{
		this.editable = editable;
	}

	/**
	 * @return Returns the proposalNumber.
	 */
	public Integer getProposalNumber() {
		return proposalNumber;
	}

	/**
	 * @param proposalNumber The proposalNumber to set.
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
	 * @param status The status to set.
	 */
	public void setStatus(FinalDegreeWorkProposalStatus status) {
		this.status = status;
	}

}