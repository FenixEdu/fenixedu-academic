
package DataBeans;

import java.util.List;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */
public class InfoGuideList extends InfoObject {
    private String situation;
    private Double total;
    private List guides;
        
    public InfoGuideList() { }    

    

	/**
	 * @return
	 */
	public List getGuides() {
		return guides;
	}

	/**
	 * @param guides
	 */
	public void setGuides(List guides) {
		this.guides = guides;
	}

	/**
	 * @return
	 */
	public String getSituation() {
		return situation;
	}

	/**
	 * @param situation
	 */
	public void setSituation(String situation) {
		this.situation = situation;
	}

	/**
	 * @return
	 */
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total
	 */
	public void setTotal(Double total) {
		this.total = total;
	}

}