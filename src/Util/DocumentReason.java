package Util;

import java.util.ArrayList;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DocumentReason extends FenixUtil {

	public static final int FAMILY_ALLOWANCE = 1;
	public static final int MILITARY_PURPOSE = 2;
	public static final int BLOOD_PRICE_PENSION = 3;
	public static final int IRS = 4;
	public static final int ADSE = 5;
	public static final int SCHOLARSHIP = 6;
	public static final int PUBLIC_CONTEST = 7;
	

	
	public static final DocumentReason FAMILY_ALLOWANCE_TYPE = new DocumentReason(FAMILY_ALLOWANCE);
	public static final DocumentReason MILITARY_PURPOSE_TYPE = new DocumentReason(MILITARY_PURPOSE);
	public static final DocumentReason BLOOD_PRICE_PENSION_TYPE = new DocumentReason(BLOOD_PRICE_PENSION);
	public static final DocumentReason IRS_TYPE = new DocumentReason(IRS);
	public static final DocumentReason ADSE_TYPE = new DocumentReason(ADSE);
	public static final DocumentReason SCHOLARSHIP_TYPE = new DocumentReason(SCHOLARSHIP);
	public static final DocumentReason PUBLIC_CONTEST_TYPE = new DocumentReason(PUBLIC_CONTEST);

	
	public static final String FAMILY_ALLOWANCE_STRING = "Abono de família";
	public static final String MILITARY_PURPOSE_STRING = "Fins militares";
	public static final String BLOOD_PRICE_PENSION_STRING = "Pensão de preço sangue";
	public static final String IRS_STRING = "I.R.S.";
	public static final String ADSE_STRING = "A.D.S.E.";
	public static final String SCHOLARSHIP_STRING = "Bolsa";
	public static final String PUBLIC_CONTEST_STRING = "Concurso Público";
	
//	public static final String DEFAULT_STRING = "[Escolha uma Opção]";



	private Integer type;

	public DocumentReason() {
	}

	public DocumentReason(int type) {
		this.type = new Integer(type);
	}

	public DocumentReason(Integer type) {
		this.type = type;
	}

	public DocumentReason(String type) {
		if (type.equals(DocumentReason.FAMILY_ALLOWANCE_STRING)) this.type = new Integer(DocumentReason.FAMILY_ALLOWANCE);
		if (type.equals(DocumentReason.MILITARY_PURPOSE_STRING)) this.type = new Integer(DocumentReason.MILITARY_PURPOSE);
		if (type.equals(DocumentReason.BLOOD_PRICE_PENSION_STRING)) this.type = new Integer(DocumentReason.BLOOD_PRICE_PENSION);
		if (type.equals(DocumentReason.IRS_STRING)) this.type = new Integer(DocumentReason.IRS);
		if (type.equals(DocumentReason.ADSE_STRING)) this.type = new Integer(DocumentReason.ADSE);
		if (type.equals(DocumentReason.SCHOLARSHIP_STRING)) this.type = new Integer(DocumentReason.SCHOLARSHIP);
		if (type.equals(DocumentReason.PUBLIC_CONTEST_STRING)) this.type = new Integer(DocumentReason.PUBLIC_CONTEST);
		
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof DocumentReason) {
			DocumentReason ds = (DocumentReason) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
//		result.add(new LabelValueBean(DocumentReason.FAMILY_ABONMENT_STRING, DocumentReason.FAMILY_ABONMENT_STRING));
//		result.add(new LabelValueBean(DocumentReason.MILITARY_FINALY_STRING, DocumentReason.MILITARY_FINALY_STRING));
//		result.add(new LabelValueBean(DocumentReason.PENSAO_PRECO_SANGUE_STRING, DocumentReason.PENSAO_PRECO_SANGUE_STRING));
//		result.add(new LabelValueBean(DocumentReason.IRS_STRING, DocumentReason.IRS_STRING));
//		result.add(new LabelValueBean(DocumentReason.ADSE_STRING, DocumentReason.ADSE_STRING));
//		result.add(new LabelValueBean(DocumentReason.BOLSA_STRING, DocumentReason.BOLSA_STRING));
//		result.add(new LabelValueBean(DocumentReason.PUBLIC_CONCURSO_STRING, DocumentReason.PUBLIC_CONCURSO_STRING));
		result.add(DocumentReason.FAMILY_ALLOWANCE_STRING);
		result.add(DocumentReason.MILITARY_PURPOSE_STRING);
		result.add(DocumentReason.BLOOD_PRICE_PENSION_STRING);
		result.add(DocumentReason.IRS_STRING);
		result.add(DocumentReason.ADSE_STRING);
		result.add(DocumentReason.SCHOLARSHIP_STRING);
		result.add(DocumentReason.PUBLIC_CONTEST_STRING);
			
		return result;	
	}
    
	public String toString() {
		if (type.intValue()== DocumentReason.FAMILY_ALLOWANCE) return DocumentReason.FAMILY_ALLOWANCE_STRING;
		if (type.intValue()== DocumentReason.MILITARY_PURPOSE) return DocumentReason.MILITARY_PURPOSE_STRING;
		if (type.intValue()== DocumentReason.BLOOD_PRICE_PENSION) return DocumentReason.BLOOD_PRICE_PENSION_STRING;
		if (type.intValue()== DocumentReason.IRS) return DocumentReason.IRS_STRING;
		if (type.intValue()== DocumentReason.ADSE) return DocumentReason.ADSE_STRING;
		if (type.intValue()== DocumentReason.SCHOLARSHIP) return DocumentReason.SCHOLARSHIP_STRING;
		if (type.intValue()== DocumentReason.PUBLIC_CONTEST) return DocumentReason.PUBLIC_CONTEST_STRING;
		return "ERRO!"; // Nunca e atingido
	}      


	/**
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param integer
	 */
	public void setType(Integer integer) {
		type = integer;
	}

}