package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation {

	public static final int NON_PAYED = 1;
	public static final int PAYED = 2;
	public static final int ANNULLED = 3;

	public static final GuideSituation NON_PAYED_TYPE = new GuideSituation(NON_PAYED);
	public static final GuideSituation PAYED_TYPE = new GuideSituation(PAYED);
	public static final GuideSituation ANNULLED_TYPE = new GuideSituation(ANNULLED);

	public static final String NON_PAYED_STRING = "Não Paga";
	public static final String PAYED_STRING = "Paga";
	public static final String ANNULLED_STRING = "Anulada";
	public static final String DEFAULT = "[Escolha uma Situação]";


	private Integer situation;

	public GuideSituation() {
	}

	public GuideSituation(int type) {
		this.situation = new Integer(type);
	}

	public GuideSituation(Integer type) {
		this.situation = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof GuideSituation) {
			GuideSituation ds = (GuideSituation) obj;
			resultado = this.getSituation().equals(ds.getSituation());
		}
		return resultado;
	}

	public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(GuideSituation.DEFAULT, null));
		result.add(new LabelValueBean(GuideSituation.ANNULLED_STRING, GuideSituation.ANNULLED_STRING));
		result.add(new LabelValueBean(GuideSituation.NON_PAYED_STRING, GuideSituation.NON_PAYED_STRING));
		result.add(new LabelValueBean(GuideSituation.PAYED_STRING, GuideSituation.PAYED_STRING));
		return result;	
	}
    
	public String toString() {
		if (situation.intValue()== GuideSituation.ANNULLED) return GuideSituation.ANNULLED_STRING;
		if (situation.intValue()== GuideSituation.NON_PAYED) return GuideSituation.NON_PAYED_STRING;
		if (situation.intValue()== GuideSituation.PAYED) return GuideSituation.PAYED_STRING;
		return "ERRO!"; // Nunca e atingido
	}      


	/**
	 * @return
	 */
	public Integer getSituation() {
		return this.situation;
	}

	/**
	 * @param integer
	 */
	public void setSituation(Integer situation) {
		this.situation = situation;
	}

}