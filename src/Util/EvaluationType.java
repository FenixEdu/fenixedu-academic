package Util;



/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class EvaluationType extends FenixUtil {

	public static final int EXAM = 1;
	public static final int FINAL = 2;
	public static final int ONLINE_TEST = 3;
	
	
	public static final EvaluationType EXAM_TYPE = new EvaluationType(EXAM);
	public static final EvaluationType FINAL_TYPE = new EvaluationType(FINAL);
	public static final EvaluationType ONLINE_TEST_TYPE = new EvaluationType(ONLINE_TEST);
	
	public static final String EXAM_STRING = "Exame";
	public static final String FINAL_STRING = "Final";
	public static final String ONLINE_TEST_STRING = "Ficha de Trabalho";
	
	

	private Integer type;

	public EvaluationType() {
	}

	public EvaluationType(int type) {
		this.type = new Integer(type);
	}

	public EvaluationType(Integer type) {
		this.type = type;
	}

	public EvaluationType(String type) {
		if (type.equals(EvaluationType.EXAM_STRING)) this.type = new Integer(EvaluationType.EXAM);
		if (type.equals(EvaluationType.FINAL_STRING)) this.type = new Integer(EvaluationType.FINAL);
		if (type.equals(EvaluationType.ONLINE_TEST_STRING)) this.type = new Integer(EvaluationType.ONLINE_TEST);
		
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof EvaluationType) {
			EvaluationType ds = (EvaluationType) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	
	public String toString() {
		if (type.intValue()== EvaluationType.EXAM) return EvaluationType.EXAM_STRING;
		if (type.intValue()== EvaluationType.FINAL) return EvaluationType.FINAL_STRING;
		if (type.intValue()== EvaluationType.ONLINE_TEST) return EvaluationType.ONLINE_TEST_STRING;
		
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