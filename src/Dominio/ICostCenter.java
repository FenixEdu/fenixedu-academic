/*
 * Created on 12/Jul/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public interface ICostCenter {
	/**
	 * @return String
	 */
	public abstract String getDepartamento();
	/**
	 * @return String
	 */
	public abstract String getSeccao1();
	/**
	 * @return String
	 */
	public abstract String getSeccao2();
	/**
	 * @return String
	 */
	public abstract String getSigla();
	/**
	 * Sets the departamento.
	 * @param departamento The departamento to set
	 */
	public abstract void setDepartamento(String departamento);
	/**
	 * Sets the seccao1.
	 * @param seccao1 The seccao1 to set
	 */
	public abstract void setSeccao1(String seccao1);
	/**
	 * Sets the seccao2.
	 * @param seccao2 The seccao2 to set
	 */
	public abstract void setSeccao2(String seccao2);
	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public abstract void setSigla(String sigla);
}