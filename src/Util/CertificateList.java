package Util;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class CertificateList implements Serializable {

	public static final int MATRICULA = 1;
	public static final int MATRICULA_INSCRICAO = 2;
	public static final int DURACAO_CURSO = 3;
	public static final int INSCRICAO = 4;
	public static final int APROVEITAMENTO = 5;
	public static final int APROVEITAMENTO_EXTRA_CURRICULAR = 6;
	public static final int FIM_PARTE_ESCOLAR_SIMPLES = 7;
	public static final int FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA = 8;
	public static final int FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA = 9;
	public static final int DIPLOMA = 10;
	

	public static final CertificateList MATRICULA_TYPE = new CertificateList(MATRICULA);
	public static final CertificateList MATRICULA_INSCRICAO_TYPE = new CertificateList(MATRICULA_INSCRICAO);
	public static final CertificateList DURACAO_CURSO_TYPE = new CertificateList(DURACAO_CURSO);
	public static final CertificateList INSCRICAO_TYPE = new CertificateList(INSCRICAO);
	public static final CertificateList APROVEITAMENTO_TYPE = new CertificateList(APROVEITAMENTO);
	public static final CertificateList APROVEITAMENTO_EXTRA_CURRICULAR_TYPE = new CertificateList(APROVEITAMENTO_EXTRA_CURRICULAR);
	public static final CertificateList FIM_PARTE_ESCOLAR_SIMPLES_TYPE = new CertificateList(FIM_PARTE_ESCOLAR_SIMPLES);
	public static final CertificateList FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA_TYPE = new CertificateList(FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA);
	public static final CertificateList FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA_TYPE = new CertificateList(FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA);
	public static final CertificateList DIPLOMA_TYPE = new CertificateList(DIPLOMA);

	public static final String MATRICULA_STRING = "Matrícula";
	public static final String MATRICULA_INSCRICAO_STRING = "Matrícula e Inscrição";
	public static final String DURACAO_CURSO_STRING = "Duração do Curso";
	public static final String INSCRICAO_STRING = "Inscrição";
	public static final String APROVEITAMENTO_STRING = "Aproveitamento";
	public static final String APROVEITAMENTO_EXTRA_CURRICULAR_STRING = "Aproveitamento de Disciplinas Extra Curricular";
	public static final String FIM_PARTE_ESCOLAR_SIMPLES_STRING = "Fim parte escolar simples";
	public static final String FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA_STRING = "Fim parte escolar discriminada sem média";
	public static final String FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA_STRING = "Fim parte escolar discriminada com média";
	public static final String DIPLOMA_STRING = "Diploma";

	public static final String DEFAULT = "[Escolha um Tipo de Documento]";


	private Integer type;

	public CertificateList() {
	}

	public CertificateList(int type) {
		this.type = new Integer(type);
	}

	public CertificateList(Integer type) {
		this.type = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof CertificateList) {
			CertificateList ds = (CertificateList) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(CertificateList.DEFAULT, null));
		result.add(new LabelValueBean(CertificateList.MATRICULA_STRING, CertificateList.MATRICULA_STRING));
		result.add(new LabelValueBean(CertificateList.MATRICULA_INSCRICAO_STRING, CertificateList.MATRICULA_INSCRICAO_STRING));
		result.add(new LabelValueBean(CertificateList.DURACAO_CURSO_STRING, CertificateList.DURACAO_CURSO_STRING));
		result.add(new LabelValueBean(CertificateList.INSCRICAO_STRING, CertificateList.INSCRICAO_STRING));
		result.add(new LabelValueBean(CertificateList.APROVEITAMENTO_STRING, CertificateList.APROVEITAMENTO_STRING));
		result.add(new LabelValueBean(CertificateList.APROVEITAMENTO_EXTRA_CURRICULAR_STRING, CertificateList.APROVEITAMENTO_EXTRA_CURRICULAR_STRING));
		result.add(new LabelValueBean(CertificateList.FIM_PARTE_ESCOLAR_SIMPLES_STRING, CertificateList.FIM_PARTE_ESCOLAR_SIMPLES_STRING));
		result.add(new LabelValueBean(CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA_STRING, CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA_STRING));
		result.add(new LabelValueBean(CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA_STRING, CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA_STRING));
		result.add(new LabelValueBean(CertificateList.DIPLOMA_STRING, CertificateList.DIPLOMA_STRING));
		return result;	
	}
    
	public String toString() {
		if (type.intValue()== CertificateList.MATRICULA) return CertificateList.MATRICULA_STRING;
		if (type.intValue()== CertificateList.MATRICULA_INSCRICAO) return CertificateList.MATRICULA_INSCRICAO_STRING;
		if (type.intValue()== CertificateList.DURACAO_CURSO) return CertificateList.DURACAO_CURSO_STRING;
		if (type.intValue()== CertificateList.INSCRICAO) return CertificateList.INSCRICAO_STRING;
		if (type.intValue()== CertificateList.APROVEITAMENTO) return CertificateList.APROVEITAMENTO_STRING;
		if (type.intValue()== CertificateList.APROVEITAMENTO_EXTRA_CURRICULAR) return CertificateList.APROVEITAMENTO_EXTRA_CURRICULAR_STRING;
		if (type.intValue()== CertificateList.FIM_PARTE_ESCOLAR_SIMPLES) return CertificateList.FIM_PARTE_ESCOLAR_SIMPLES_STRING;
		if (type.intValue()== CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA) return CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_SEM_MEDIA_STRING;		
		if (type.intValue()== CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA) return CertificateList.FIM_PARTE_ESCOLAR_DISCRIMINADA_COM_MEDIA_STRING;		
		if (type.intValue()== CertificateList.DIPLOMA) return CertificateList.DIPLOMA_STRING;
		
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