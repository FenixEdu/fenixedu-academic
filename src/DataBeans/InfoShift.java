/*
 * InfoShift.java
 *
 * Created on 31 de Outubro de 2002, 12:35
 */

package DataBeans;

/**
 *
 * @author  tfc130
 */
import Util.TipoAula;

public class InfoShift extends InfoObject {
	protected String _nome;
	protected TipoAula _tipo;
	protected Integer _lotacao;
	protected InfoExecutionCourse _infoDisciplinaExecucao;

	public InfoShift() {
	}

	public InfoShift(
		String nome,
		TipoAula tipo,
		Integer lotacao,
		InfoExecutionCourse infoDisciplinaExecucao) {
		setNome(nome);
		setTipo(tipo);
		setLotacao(lotacao);
		setInfoDisciplinaExecucao(infoDisciplinaExecucao);
	}

	public String getNome() {
		return _nome;
	}

	public void setNome(String nome) {
		_nome = nome;
	}

	public InfoExecutionCourse getInfoDisciplinaExecucao() {
		return _infoDisciplinaExecucao;
	}

	public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao) {
		_infoDisciplinaExecucao = infoDisciplinaExecucao;
	}

	public TipoAula getTipo() {
		return _tipo;
	}

	public void setTipo(TipoAula tipo) {
		_tipo = tipo;
	}

	public Integer getLotacao() {
		return _lotacao;
	}

	public void setLotacao(Integer lotacao) {
		_lotacao = lotacao;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoShift) {
			InfoShift infoTurno = (InfoShift) obj;
			resultado =
				(getNome().equals(infoTurno.getNome()))
					&& (getInfoDisciplinaExecucao()
						.equals(infoTurno.getInfoDisciplinaExecucao()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[TURNO";
		result += ", nome=" + _nome;
		result += ", tipo=" + _tipo;
		result += ", lotacao=" + _lotacao;
		result += ", infoDisciplinaExecucao=" + _infoDisciplinaExecucao;
		result += "]";
		return result;
	}

}
