
package Dominio;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public interface IPersonRole {

	public void setPerson(IPessoa person);
	public void setRole(IRole role);
	
	public IPessoa getPerson();
	public IRole getRole();
	

}
