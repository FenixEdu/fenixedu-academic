/*
 * InfoExecutionCourse.java
 *
 * Created on 28 de Novembro de 2002, 3:41
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoExecutionCourse implements Serializable {
	protected String _nome;
	protected String _sigla;
	protected String _programa;
	protected Integer _semester;
	private Double _theoreticalHours;
	private Double _praticalHours;
	private Double _theoPratHours;
	private Double _labHours;

	protected InfoExecutionDegree _infoLicenciaturaExecucao;
	// A chave do responsavel falta ainda porque ainda nao existe a respeciva ligacao
	// na base de dados.

	private InfoExecutionPeriod infoExecutionPeriod;
	

	public InfoExecutionCourse() {
	}

	public InfoExecutionCourse(
		String nome,
		String sigla,
		String programa,
		InfoExecutionDegree infoLicenciaturaExecucao,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours) {
		setNome(nome);
		setSigla(sigla);
		setPrograma(programa);
		setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
	}

	public InfoExecutionCourse(
		String nome,
		String sigla,
		String programa,
		InfoExecutionDegree infoLicenciaturaExecucao,
		Double theoreticalHours,
		Double praticalHours,
		Double theoPratHours,
		Double labHours,
		Integer semester) {
		setNome(nome);
		setSigla(sigla);
		setPrograma(programa);
		setInfoLicenciaturaExecucao(infoLicenciaturaExecucao);
		setTheoreticalHours(theoreticalHours);
		setPraticalHours(praticalHours);
		setTheoPratHours(theoPratHours);
		setLabHours(labHours);
	}
	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public Integer getSemester() {
		return _semester;
	}

	public void setSemester(Integer semester) {
		_semester = semester;
	}

	public String getSigla() {
		return _sigla;
	}

	public void setSigla(String sigla) {
		_sigla = sigla;
	}

	public String getPrograma() {
		return _programa;
	}

	public void setPrograma(String programa) {
		_programa = programa;
	}

	public Double getTheoreticalHours() {
		return _theoreticalHours;
	}

	public void setTheoreticalHours(Double theoreticalHours) {
		_theoreticalHours = theoreticalHours;
	}

	public Double getPraticalHours() {
		return _praticalHours;
	}

	public void setPraticalHours(Double praticalHours) {
		_praticalHours = praticalHours;
	}

	public Double getTheoPratHours() {
		return _theoPratHours;
	}

	public void setTheoPratHours(Double theoPratHours) {
		_theoPratHours = theoPratHours;
	}

	public Double getLabHours() {
		return _labHours;
	}

	public void setLabHours(Double labHours) {
		_labHours = labHours;
	}

	public InfoExecutionDegree getInfoLicenciaturaExecucao() {
		return _infoLicenciaturaExecucao;
	}

	public void setInfoLicenciaturaExecucao(InfoExecutionDegree infoLicenciaturaExecucao) {
		_infoLicenciaturaExecucao = infoLicenciaturaExecucao;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		System.out.println(this.infoExecutionPeriod.getName());
		System.out.println(this.infoExecutionPeriod.getInfoExecutionYear().getYear());
		if (obj instanceof InfoExecutionCourse) {
			InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) obj;
			System.out.println("SIGLA = "+infoExecutionCourse.getSigla());
			System.out.println("PERIODO:"+ infoExecutionCourse.getInfoExecutionPeriod().getName());			
			System.out.println("ANO:"+ infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear());
			resultado =
				(getSigla().equals(infoExecutionCourse.getSigla())
					&& getInfoExecutionPeriod().equals(infoExecutionCourse.getInfoExecutionPeriod()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[INFODISCIPLINAEXECUCAO";
		result += ", nome=" + _nome;
		result += ", sigla=" + _sigla;
		result += ", programa=" + _programa;
		result += ", theoreticalHours=" + _theoreticalHours;
		result += ", praticalHours=" + _praticalHours;
		result += ", theoPratHours=" + _theoPratHours;
		result += ", labHours=" + _labHours;
		result += ", infoLicenciaturaExecucao=" + _infoLicenciaturaExecucao;
		result += "]";
		return result;
	}


	/**
	 * Returns the infoExecutionPeriod.
	 * @return InfoExecutionPeriod
	 */
	public InfoExecutionPeriod getInfoExecutionPeriod() {
		return infoExecutionPeriod;
	}


	/**
	 * Sets the infoExecutionPeriod.
	 * @param infoExecutionPeriod The infoExecutionPeriod to set
	 */
	public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
		this.infoExecutionPeriod = infoExecutionPeriod;
	}

}
