package Util;


/**
 * @author Luis Cruz & Sara Ribeiro
 *
 * 22/Abr/2003
 */
public class ExamState extends FenixUtil {

	public static final int FINAL = 0;
	public static final int PROVISORY = 1;
	
	private Integer type;

	public ExamState() {
	}

	public ExamState(int state) {
		this.type = new Integer(state);
	}

	public ExamState(Integer state) {
		this.type = state;
	}

	public java.lang.Integer getType() {
		return type;
	}

	public void setType(Integer state) {
		this.type = state;
	}

	public boolean equals(Object o) {
		if (o instanceof ExamState) {
			ExamState aux = (ExamState) o;
			return this.type.equals(aux.getType());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.type.intValue();
		String values = null;

		switch (value) {
			case FINAL :
				values = "FINAL";
				break;
			case PROVISORY :
				values = "PROVISORY";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + values + "]";
	}
}