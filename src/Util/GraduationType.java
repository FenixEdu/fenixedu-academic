package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GraduationType extends FenixUtil {

	public static final int MAJOR_DEGREE = 1;
	public static final int MASTER_DEGREE = 2;

	public static final GraduationType MAJOR_DEGREE_TYPE = new GraduationType(MAJOR_DEGREE);
	public static final GraduationType MASTER_DEGREE_TYPE = new GraduationType(MASTER_DEGREE);

	public static final String MAJOR_DEGREE_STRING = "Licenciatura";
	public static final String MASTER_DEGREE_STRING = "Pós-Graduação";
	public static final String DEFAULT = "[Escolha uma Graduação]";


	private Integer type;

	public GraduationType() {
	}

	public GraduationType(int type) {
		this.type = new Integer(type);
	}

	public GraduationType(Integer type) {
		this.type = type;
	}

	public GraduationType(String type) {
		if (type.equals(GraduationType.MAJOR_DEGREE_STRING)) this.type = new Integer(GraduationType.MAJOR_DEGREE);
		if (type.equals(GraduationType.MASTER_DEGREE_STRING)) this.type = new Integer(GraduationType.MASTER_DEGREE);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof GraduationType) {
			GraduationType ds = (GraduationType) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(GraduationType.DEFAULT, null));
		result.add(new LabelValueBean(GraduationType.MAJOR_DEGREE_STRING, GraduationType.MAJOR_DEGREE_STRING));
		result.add(new LabelValueBean(GraduationType.MASTER_DEGREE_STRING, GraduationType.MASTER_DEGREE_STRING));
		return result;	
	}
    
	public String toString() {
		if (type.intValue()== GraduationType.MAJOR_DEGREE) return GraduationType.MAJOR_DEGREE_STRING;
		if (type.intValue()== GraduationType.MASTER_DEGREE) return GraduationType.MASTER_DEGREE_STRING;
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