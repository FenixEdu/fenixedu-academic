package Dominio;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */

public class CentroCusto extends DomainObject implements ICostCenter {
	private int codigoInterno;
	private String sigla;
	private String departamento;
	private String seccao1;
	private String seccao2;

	public CentroCusto() {
	}

	public CentroCusto(int codigoInterno, String sigla, String departamento, String seccao1, String seccao2) {
		this.codigoInterno = codigoInterno;
		this.sigla = sigla;
		this.departamento = departamento;
		this.seccao1 = seccao1;
		this.seccao2 = seccao2;
	}

	public CentroCusto(String sigla, String departamento, String seccao1, String seccao2) {
		this.codigoInterno = 0;
		this.sigla = sigla;
		this.departamento = departamento;
		this.seccao1 = seccao1;
		this.seccao2 = seccao2;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof Regime) {
			CentroCusto centroCusto = (CentroCusto) obj;

			resultado =
				(this.getCodigoInterno() == centroCusto.getCodigoInterno()
					&& this.getSigla() == centroCusto.getSigla()
					&& this.getDepartamento() == centroCusto.getDepartamento()
					&& this.getSeccao1() == centroCusto.getSeccao1()
					&& this.getSeccao2() == centroCusto.getSeccao2());
		}
		return resultado;
	}
	/**
	 * @return int
	 */
	public int getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * @return String
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @return String
	 */
	public String getSeccao1() {
		return seccao1;
	}

	/**
	 * @return String
	 */
	public String getSeccao2() {
		return seccao2;
	}

	/**
	 * @return String
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(int codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * Sets the departamento.
	 * @param departamento The departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * Sets the seccao1.
	 * @param seccao1 The seccao1 to set
	 */
	public void setSeccao1(String seccao1) {
		this.seccao1 = seccao1;
	}

	/**
	 * Sets the seccao2.
	 * @param seccao2 The seccao2 to set
	 */
	public void setSeccao2(String seccao2) {
		this.seccao2 = seccao2;
	}

	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}