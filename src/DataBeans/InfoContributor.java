/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package DataBeans;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoContributor  extends InfoObject {
	private Integer contributorNumber;
	private String contributorName;
	private String contributorAddress;
	
	public InfoContributor() {} 
		
	public InfoContributor(Integer contributorNumber, String contributorName, String contributorAddress){
	 		this.contributorNumber = contributorNumber;
			this.contributorName = contributorName;
			this.contributorAddress = contributorAddress;
	}
	
	public boolean equals(Object o) {
		return
		((o instanceof InfoContributor) &&
		(contributorNumber.equals(((InfoContributor)o).getContributorNumber())) &&
		(contributorName.equals(((InfoContributor)o).getContributorName())) &&
		(contributorAddress.equals(((InfoContributor)o).getContributorAddress())));

	}

	public String toString() {
		String result = "InfoContributor:\n";
		result += "\n  - Contributor Number : " + contributorNumber;
		result += "\n  - Contributor Name : " + contributorName;
		result += "\n  - Contributor Address : " + contributorAddress;
		return result;
	}
    
	/**
	 * @return String
	 */
	public String getContributorAddress() {
		return contributorAddress;
	}

	/**
	 * @return String
	 */
	public String getContributorName() {
		return contributorName;
	}

	/**
	 * @return Integer
	 */
	public Integer getContributorNumber() {
		return contributorNumber;
	}


	/**
	 * Sets the contributorAddress.
	 * @param contributorAddress The contributorAddress to set
	 */
	public void setContributorAddress(String contributorAddress) {
		this.contributorAddress = contributorAddress;
	}

	/**
	 * Sets the contributorName.
	 * @param contributorName The contributorName to set
	 */
	public void setContributorName(String contributorName) {
		this.contributorName = contributorName;
	}

	/**
	 * Sets the contributorNumber.
	 * @param contributorNumber The contributorNumber to set
	 */
	public void setContributorNumber(Integer contributorNumber) {
		this.contributorNumber = contributorNumber;
	}



}
