package Dominio;

import java.util.Date;

import Util.SituationOfGuide;
import Util.State;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends DomainObject implements IGuideSituation {
	
	protected Integer keyGuide;

	protected String remarks;
	protected SituationOfGuide situation;
	protected Date date;
	protected State state;

	protected IGuide guide;

	public GuideSituation() {
	}

	public GuideSituation(
		SituationOfGuide situation,
		String remarks,
		Date date,
		IGuide guide,
		State state) {
		this.remarks = remarks;
		this.guide = guide;
		this.situation = situation;
		this.date = date;
		this.state = state;

	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof GuideSituation) {
			GuideSituation guideSituation = (GuideSituation) obj;

			if (((getGuide() == null && guideSituation.getGuide() == null)
				|| (getGuide().equals(guideSituation.getGuide())))
				&& ((getSituation() == null && guideSituation.getSituation() == null)
					|| (getSituation().equals(guideSituation.getSituation())))) {
				resultado = true;
			}
		}

		return resultado;
	}

	public String toString() {
		String result = "[GUIDE SITUATION";
		
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
	public SituationOfGuide getSituation() {
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
	 * @param guide
	 */
	public void setSituation(SituationOfGuide guide) {
		situation = guide;
	}

	/**
	 * @param state
	 */
	public void setState(State state) {
		this.state = state;
	}

}
