/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IContributor {
	
	void setContributorNumber(Integer contributorNumber);
	void setContributorName(String contributorName);
	void setContributorAddress(String contributorAddress);

	Integer getContributorNumber();
	String getContributorName();
	String getContributorAddress();

}
