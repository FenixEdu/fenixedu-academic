/*
 * Frequenta.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
public class InfoFrequenta  {
	protected InfoStudent _aluno;
	protected InfoExecutionCourse _disciplinaExecucao;
	protected InfoEnrolment _enrolment;

	// códigos internos da base de dados
	private Integer _chaveAluno;
	private Integer _chaveDisciplinaExecucao;

	private Integer _keyEnrolment;

	/** Construtor sem argumentos público requerido pela moldura de objectos OJB */
	public InfoFrequenta() {
	}

	public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao) {
		setAluno(aluno);
		setDisciplinaExecucao(disciplinaExecucao);
	}

	public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao, InfoEnrolment enrolment) {
		setAluno(aluno);
		setDisciplinaExecucao(disciplinaExecucao);
		setEnrolment(enrolment);
	}

	public InfoStudent getAluno() {
		return _aluno;
	}

	public void setAluno(InfoStudent aluno) {
		_aluno = aluno;
	}

	public Integer getChaveAluno() {
		return _chaveAluno;
	}

	public void setChaveAluno(Integer chaveAluno) {
		_chaveAluno = chaveAluno;
	}

	public InfoExecutionCourse getDisciplinaExecucao() {
		return _disciplinaExecucao;
	}

	public void setDisciplinaExecucao(InfoExecutionCourse disciplinaExecucao) {
		_disciplinaExecucao = disciplinaExecucao;
	}

	public Integer getChaveDisciplinaExecucao() {
		return _chaveDisciplinaExecucao;
	}

	public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
		_chaveDisciplinaExecucao = chaveDisciplinaExecucao;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoFrequenta) {
			InfoFrequenta frequenta = (InfoFrequenta) obj;
				resultado = //getCodigoInterno().equals(((Frequenta)obj).getCodigoInterno());
	getAluno().equals(frequenta.getAluno()) && getDisciplinaExecucao().equals(getDisciplinaExecucao());
		}
		return resultado;
	}

	public String toString() {
		String result = "[ATTEND";
		result += ", Student=" + _aluno;
		result += ", ExecutionCourse=" + _disciplinaExecucao;
		result += ", Enrolment=" + _enrolment;
		result += "]";
		return result;
	}

	/**
	 * @return
	 */
	public Integer getKeyEnrolment() {
		return _keyEnrolment;
	}

	/**
	 * @param integer
	 */
	public void setKeyEnrolment(Integer integer) {
		_keyEnrolment = integer;
	}

	public InfoEnrolment getEnrolment() {
		return _enrolment;
	}

	public void setEnrolment(InfoEnrolment enrolment) {
		this._enrolment = enrolment;
		
	}

}
