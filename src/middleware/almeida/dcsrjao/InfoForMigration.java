package middleware.almeida.dcsrjao;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class InfoForMigration {

	public static final String NAME_OF_OLD_DEGREE_CURRICULAR_PLAN = "LEQ";
	public static final String NAME_OF_NEW_DEGREE_CURRICULAR_PLAN = "LEQ2003/2004";

	private String type;
	
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoForMigration) {
			InfoForMigration ds = (InfoForMigration) obj;
			resultado = this.getType().equals(ds.getType());
		}
		return resultado;
	}

	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

}
