package Util;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */
public class ScopeType {

	public static final int NORMAL = 0;
	public static final int ANUAL = 1;
	public static final int MANDATORY = 2;
	
	private Integer type;

	public ScopeType() {
	}

	public ScopeType(int state) {
		this.type = new Integer(state);
	}

	public ScopeType(Integer state) {
		this.type = state;
	}

	public java.lang.Integer getType() {
		return type;
	}

	public void setType(Integer state) {
		this.type = state;
	}

	public boolean equals(Object o) {
		if (o instanceof ScopeType) {
			ScopeType aux = (ScopeType) o;
			return this.type.equals(aux.getType());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case NORMAL :
				valueS = "NORMAL";
				break;
			case ANUAL :
				valueS = "ANUAL";
				break;
			case MANDATORY:
				valueS = "MANDATORY";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}
}