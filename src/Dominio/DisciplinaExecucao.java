package Dominio;

import java.util.List;

public class DisciplinaExecucao implements IDisciplinaExecucao {

	private String nome;
	private String sigla;

	private Double theoreticalHours;
	private Double praticalHours;
	private Double theoPratHours;
	private Double labHours;

	private Integer codigoInterno;

	private List associatedCurricularCourses = null;
	private List associatedExams = null;

	private IExecutionPeriod executionPeriod;
	private Integer keyExecutionPeriod;

	protected String comment;

	/* Construtores */

	public DisciplinaExecucao() {
	}

	public DisciplinaExecucao(
		String nome,
		String sigla,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		IExecutionPeriod executionPeriod) {
		
		setNome(nome);
		setSigla(sigla);
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
		setExecutionPeriod(executionPeriod);
		setComment(new String());
	}
	
	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IDisciplinaExecucao) {
			IDisciplinaExecucao de = (IDisciplinaExecucao) obj;

			resultado =
				(getSigla().equals(de.getSigla()))
					&& (getExecutionPeriod().equals(de.getExecutionPeriod()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[EXECUTION_COURSE";
		result += ", codInt=" + codigoInterno;
		result += ", sigla=" + sigla;
		result += ", nome=" + nome;
		result += ", theoreticalHours=" + theoreticalHours;
		result += ", praticalHours=" + praticalHours;
		result += ", theoPratHours=" + theoPratHours;
		result += ", labHours=" + labHours;
		result += ", executionPeriod=" + getExecutionPeriod();
		result += "]";
		return result;
	}

	/**
	 * Returns the associatedCurricularCourses.
	 * @return List
	 */
	public List getAssociatedCurricularCourses() {
		return associatedCurricularCourses;
	}

	/**
	 * Returns the codigoInterno.
	 * @return Integer
	 */
	public Integer getCodigoInterno() {
		return codigoInterno;
	}

	/**
	 * Returns the nome.
	 * @return String
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Returns the sigla.
	 * @return String
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * Returns the theoreticalHours.
	 * @return double
	 */
	public Double getTheoreticalHours() {
		return theoreticalHours;
	}

	/**
	 * Returns the praticalHours.
	 * @return double
	 */
	public Double getPraticalHours() {
		return praticalHours;
	}

	/**
	 * Returns the theoPratHours.
	 * @return double
	 */
	public Double getTheoPratHours() {
		return theoPratHours;
	}

	/**
	 * Returns the labHours.
	 * @return double
	 */
	public Double getLabHours() {
		return labHours;
	}

	/**
	 * Sets the associatedCurricularCourses.
	 * @param associatedCurricularCourses The associatedCurricularCourses to set
	 */
	public void setAssociatedCurricularCourses(List associatedCurricularCourses) {
		this.associatedCurricularCourses = associatedCurricularCourses;
	}

	/**
	 * Sets the codigoInterno.
	 * @param codigoInterno The codigoInterno to set
	 */
	public void setCodigoInterno(Integer codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	/**
	 * Sets the nome.
	 * @param nome The nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Sets the sigla.
	 * @param sigla The sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * Sets the theoreticalHours.
	 * @param theoreticalHours The theoreticalHours to set 
	 */
	public void setTheoreticalHours(Double theoreticalHours) {
		this.theoreticalHours = theoreticalHours;
	}

	/**
	 * Sets the praticalHours.
	 * @param praticalHours The praticalHours to set
	 */
	public void setPraticalHours(Double praticalHours) {
		this.praticalHours = praticalHours;
	}

	/**
	* Sets the theoPratHours.
	 * @param theoPratHours The theoPratHours to set
	 */
	public void setTheoPratHours(Double theoPratHours) {
		this.theoPratHours = theoPratHours;
	}

	/**
	 * Sets the labHours.
	 * @param labHours The labHours to set
	 */
	public void setLabHours(Double labHours) {
		this.labHours = labHours;
	}

	/**
	 * @see Dominio.IDisciplinaExecucao#getExecutionPeriod()
	 */
	public IExecutionPeriod getExecutionPeriod() {
		return this.executionPeriod;
	}
	/**
	 * @see Dominio.IDisciplinaExecucao#setExecutionPeriod(Dominio.IExecutionPeriod)
	 */
	public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}

	/**
	 * Returns the keyExecutionPeriod.
	 * @return Integer
	 */
	public Integer getKeyExecutionPeriod() {
		return keyExecutionPeriod;
	}

	/**
	 * Sets the keyExecutionPeriod.
	 * @param keyExecutionPeriod The keyExecutionPeriod to set
	 */
	public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
		this.keyExecutionPeriod = keyExecutionPeriod;
	}

	/**
	 * @return
	 */
	public List getAssociatedExams() {
		return associatedExams;
	}

	/**
	 * @param list
	 */
	public void setAssociatedExams(List list) {
		associatedExams = list;
	}

	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
	}

}