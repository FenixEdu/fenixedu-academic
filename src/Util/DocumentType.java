package Util;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DocumentType implements Serializable {

	public static final int CERTIFICATE = 1;
	public static final int CERTIFICATE_OF_DEGREE = 2;
	public static final int ACADEMIC_PROOF_EMOLUMENT = 3;
	public static final int APPLICATION_EMOLUMENT = 4;
	public static final int ENROLMENT = 5;
	public static final int FINE = 6;
	public static final int INSURANCE = 7;
	public static final int RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS = 8;
	public static final int OTHERS = 9;
	public static final int GRATUITY = 10;

	public static final DocumentType CERTIFICATE_TYPE = new DocumentType(CERTIFICATE);
	public static final DocumentType CERTIFICATE_OF_DEGREE_TYPE = new DocumentType(CERTIFICATE_OF_DEGREE);
	public static final DocumentType ACADEMIC_PROOF_EMOLUMENT_TYPE = new DocumentType(ACADEMIC_PROOF_EMOLUMENT);
	public static final DocumentType APPLICATION_EMOLUMENT_TYPE = new DocumentType(APPLICATION_EMOLUMENT);
	public static final DocumentType ENROLMENT_TYPE = new DocumentType(ENROLMENT);
	public static final DocumentType FINE_TYPE = new DocumentType(FINE);
	public static final DocumentType INSURANCE_TYPE = new DocumentType(INSURANCE);
	public static final DocumentType RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_TYPE = new DocumentType(RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS);
	public static final DocumentType OTHERS_TYPE = new DocumentType(OTHERS);
	public static final DocumentType GRATUITY_TYPE = new DocumentType(GRATUITY);

	public static final String CERTIFICATE_STRING = "Certidão";
	public static final String CERTIFICATE_OF_DEGREE_STRING = "Diploma";
	public static final String ACADEMIC_PROOF_EMOLUMENT_STRING = "Emolumentos para Admissão a Provas Académicas";
	public static final String APPLICATION_EMOLUMENT_STRING = "Emolumentos de Candidatura";
	public static final String ENROLMENT_STRING = "Inscrições";
	public static final String FINE_STRING = "Multa";
	public static final String INSURANCE_STRING = "Seguro";
	public static final String RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_STRING = "Processo de Equivalência e Reconhecimento de Grau";
	public static final String OTHERS_STRING = "Outros Documentos";
	public static final String GRATUITY_STRING = "Propinas";
	public static final String DEFAULT = "[Escolha um Tipo de Documento]";


	private Integer type;

	public DocumentType() {
	}

	public DocumentType(int type) {
		this.type = new Integer(type);
	}

	public DocumentType(Integer type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof DocumentType) {
			DocumentType ds = (DocumentType) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(DocumentType.DEFAULT, null));
		result.add(new LabelValueBean(DocumentType.CERTIFICATE_STRING, DocumentType.CERTIFICATE_STRING));
		result.add(new LabelValueBean(DocumentType.CERTIFICATE_OF_DEGREE_STRING, DocumentType.CERTIFICATE_OF_DEGREE_STRING));
		result.add(new LabelValueBean(DocumentType.ACADEMIC_PROOF_EMOLUMENT_STRING, DocumentType.ACADEMIC_PROOF_EMOLUMENT_STRING));
		result.add(new LabelValueBean(DocumentType.APPLICATION_EMOLUMENT_STRING, DocumentType.APPLICATION_EMOLUMENT_STRING));
		result.add(new LabelValueBean(DocumentType.ENROLMENT_STRING, DocumentType.ENROLMENT_STRING));
		result.add(new LabelValueBean(DocumentType.FINE_STRING, DocumentType.FINE_STRING));
		result.add(new LabelValueBean(DocumentType.INSURANCE_STRING, DocumentType.INSURANCE_STRING));
		result.add(new LabelValueBean(DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_STRING, DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_STRING));
		result.add(new LabelValueBean(DocumentType.GRATUITY_STRING, DocumentType.GRATUITY_STRING));
		result.add(new LabelValueBean(DocumentType.OTHERS_STRING, DocumentType.OTHERS_STRING));
		return result;	
	}
    
	public String toString() {
		if (type.intValue()== DocumentType.CERTIFICATE) return DocumentType.CERTIFICATE_STRING;
		if (type.intValue()== DocumentType.CERTIFICATE_OF_DEGREE) return DocumentType.CERTIFICATE_OF_DEGREE_STRING;
		if (type.intValue()== DocumentType.ACADEMIC_PROOF_EMOLUMENT) return DocumentType.ACADEMIC_PROOF_EMOLUMENT_STRING;
		if (type.intValue()== DocumentType.APPLICATION_EMOLUMENT) return DocumentType.APPLICATION_EMOLUMENT_STRING;
		if (type.intValue()== DocumentType.ENROLMENT) return DocumentType.ENROLMENT_STRING;
		if (type.intValue()== DocumentType.FINE) return DocumentType.FINE_STRING;
		if (type.intValue()== DocumentType.INSURANCE) return DocumentType.INSURANCE_STRING;
		if (type.intValue()== DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS) return DocumentType.RANK_RECOGNITION_AND_EQUIVALENCE_PROCESS_STRING;		
		if (type.intValue()== DocumentType.OTHERS) return DocumentType.OTHERS_STRING;		
		if (type.intValue()== DocumentType.GRATUITY) return DocumentType.GRATUITY_STRING;		
		
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