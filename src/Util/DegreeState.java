package Util;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class DegreeState {

	public static final int ACTIVO = 1;
	public static final int INACTIVO = 2;

	private Integer state;

	public DegreeState() {
	}

	public DegreeState(int state) {
		this.state = new Integer(state);
	}

	public DegreeState(Integer state) {
		this.state = state;
	}

	public Integer getDegreeState() {
		return this.state;
	}

	public void setDegreeState(Integer state) {
		this.state = state;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof DegreeState) {
			DegreeState ds = (DegreeState) obj;
			resultado = this.getDegreeState().equals(ds.getDegreeState());
		}
		return resultado;
	}

	public String toString() {

		int value = this.state.intValue();
		String valueS = null;

		switch (value) {
			case ACTIVO :
				valueS = "ACTIVO";
			case INACTIVO :
				valueS = "INACTIVO";
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}

}