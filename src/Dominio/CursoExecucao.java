/*
 * CursoExecucao.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package Dominio;

/**
 *
 * @author  rpfi
 */

public class CursoExecucao implements ICursoExecucao {
	protected String anoLectivo;
	protected ICurso curso;

	// codigos internos da base de dados
	private Integer codigoInterno;
	private Integer chaveCurso;

	private IExecutionYear executionYear;

	private Integer academicYear;

	private IPlanoCurricularCurso degreeCurricularPlan;
	private Integer keyCurricularPlan;

	/** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
	public CursoExecucao() {
	}

	/**
	 * @deprecated
	 * @param anoLectivo
	 * @param curso
	 * @param executionYear
	 * @param curricularPlan
	 */
	public CursoExecucao(
		String anoLectivo,
		ICurso curso,
		IExecutionYear executionYear,
		IPlanoCurricularCurso curricularPlan) {
		setAnoLectivo(anoLectivo);
		setCurso(curso);
		setExecutionYear(executionYear);
		setCurricularPlan(curricularPlan);
	}

	public CursoExecucao(
		IExecutionYear executionYear,
		IPlanoCurricularCurso curricularPlan) {
		setExecutionYear(executionYear);
		setCurricularPlan(curricularPlan);
	}
	public Integer getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(Integer codInt) {
		this.codigoInterno = codInt;
	}

	public Integer getChaveCurso() {
		return chaveCurso;
	}
	public void setChaveCurso(Integer chaveCurso) {
		this.chaveCurso = chaveCurso;
	}

	public String getAnoLectivo() {
		return anoLectivo;
	}
	public void setAnoLectivo(String anoLec) {
		this.anoLectivo = anoLec;
	}

	public ICurso getCurso() {
		return curso;
	}
	public void setCurso(ICurso curso) {
		this.curso = curso;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof ICursoExecucao) {
			ICursoExecucao cursoExecucao = (ICursoExecucao) obj;
			resultado =
				getAnoLectivo().equals(cursoExecucao.getAnoLectivo())
					&& getCurso().getNome().equals(
						cursoExecucao.getCurso().getNome())
					&& getCurso().getSigla().equals(
						cursoExecucao.getCurso().getSigla());
		}
		return resultado;
	}

	public String toString() {
		String result = "[CURSO_EXECUCAO";
		result += ", codInt=" + codigoInterno;
		result += ", anoLectivo=" + anoLectivo;
		result += ", executionYear=" + executionYear;
		result += ", keyExecutionYear=" + academicYear;
		result += ", planoCurricularCurso=" + degreeCurricularPlan;
		result += ", chaveplanoCurricularCurso=" + keyCurricularPlan;
		result += "]";
		return result;
	}

	/**
	 * 
	 * @see Dominio.ICursoExecucao#getExecutionYear()
	 */
	public IExecutionYear getExecutionYear() {
		return executionYear;
	}

	/**
	 * 
	 * @see Dominio.ICursoExecucao#setExecutionYear(IExecutionYear)
	 */
	public void setExecutionYear(IExecutionYear executionYear) {
		this.executionYear = executionYear;
		this.anoLectivo = executionYear.getYear();
	}

	/**
	 * Returns the academicYear.
	 * @return Integer
	 */
	public Integer getAcademicYear() {
		return academicYear;
	}

	/**
	 * Sets the academicYear.
	 * @param academicYear The academicYear to set
	 */
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}

	/**
	 * Returns the curricularPlan.
	 * @return IPlanoCurricularCurso
	 */
	public IPlanoCurricularCurso getCurricularPlan() {
		return degreeCurricularPlan;
	}

	/**
	 * Returns the keyCurricularPlan.
	 * @return Integer
	 */
	public Integer getKeyCurricularPlan() {
		return keyCurricularPlan;
	}

	/**
	 * Sets the curricularPlan.
	 * @param curricularPlan The curricularPlan to set
	 */
	public void setCurricularPlan(IPlanoCurricularCurso curricularPlan) {
		this.degreeCurricularPlan = curricularPlan;
	}

	/**
	 * Sets the keyCurricularPlan.
	 * @param keyCurricularPlan The keyCurricularPlan to set
	 */
	public void setKeyCurricularPlan(Integer keyCurricularPlan) {
		this.keyCurricularPlan = keyCurricularPlan;
	}

}
