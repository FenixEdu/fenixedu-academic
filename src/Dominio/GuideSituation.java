

package Dominio;

import java.util.Date;

import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation implements IGuideSituation {
  protected Integer internalCode;
  protected Integer keyGuide;
  
 
  protected String remarks;
  protected GuideSituation situation;
  protected Date date;
  protected State state;
  
  protected IGuide guide;
 

  public GuideSituation() { }
    
  public GuideSituation(GuideSituation situation,String remarks,Date date,IGuide guide, State state) {
	this.remarks = remarks;
	this.guide = guide;
	this.situation = situation;
	this.date = date;
	this.state = state;
	


  }
  
  public boolean equals(Object obj) {
    boolean resultado = false;
    if (obj instanceof GuideSituation) {
		GuideSituation guideSituation = (GuideSituation)obj;

      resultado = getGuide().equals(guideSituation.getGuide()) &&
				  getSituation().equals(guideSituation.getSituation());
    }

    return resultado;
  }
  
  public String toString() {
    String result = "[GUIDE ENTRY";
    result += ", codInt=" + internalCode;
    result += ", remarks=" + remarks;
    result += ", guide=" + guide;
	result += ", guide Situtation=" + situation;
	result += ", date=" + date;
	result += ", state=" + state;
    result += "]";
    return result;
  }

    
	
	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @return
	 */
	public IGuide getGuide() {
		return guide;
	}
	
	/**
	 * @return
	 */
	public Integer getInternalCode() {
		return internalCode;
	}
	
	/**
	 * @return
	 */
	public Integer getKeyGuide() {
		return keyGuide;
	}
	
	/**
	 * @return
	 */
	public String getRemarks() {
		return remarks;
	}
	
	/**
	 * @return
	 */
	public GuideSituation getSituation() {
		return situation;
	}
	
	/**
	 * @return
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @param guide
	 */
	public void setGuide(IGuide guide) {
		this.guide = guide;
	}
	
	/**
	 * @param integer
	 */
	public void setInternalCode(Integer integer) {
		internalCode = integer;
	}
	
	/**
	 * @param integer
	 */
	public void setKeyGuide(Integer integer) {
		keyGuide = integer;
	}
	
	/**
	 * @param string
	 */
	public void setRemarks(String string) {
		remarks = string;
	}
	
	/**
	 * @param situation
	 */
	public void setSituation(GuideSituation situation) {
		this.situation = situation;
	}
	
	/**
	 * @param state
	 */
	public void setState(State state) {
		this.state = state;
	}

}
