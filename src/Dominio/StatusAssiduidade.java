package Dominio;

/**
 *
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidade {
	private int _codigoInterno;
	private String _sigla;
	private String _designacao;
	private String _estado;
	private String _assiduidade;

	/* Construtores */
	public StatusAssiduidade() {
		_codigoInterno = 0;
		_sigla = null;
		_designacao = null;
		_estado = "inactivo";
		_assiduidade = "false";
	}

	public StatusAssiduidade(String sigla, String designacao, String estado, String assiduidade) {
		_codigoInterno = 0;
		_sigla = sigla;
		_designacao = designacao;
		_estado = estado;
		_assiduidade = assiduidade;
	}

	public StatusAssiduidade(int codigoInterno, String sigla, String designacao, String estado, String assiduidade) {
		_codigoInterno = codigoInterno;
		_sigla = sigla;
		_designacao = designacao;
		_estado = estado;
		_assiduidade = assiduidade;
	}

	/* Selectores */
	public int getCodigoInterno() {
		return _codigoInterno;
	}

	public String getSigla() {
		return _sigla;
	}

	public String getDesignacao() {
		return _designacao;
	}

	public String getEstado() {
		return _estado;
	}

	public String getAssiduidade() {
		return _assiduidade;
	}

	/* Modificadores */
	public void setCodigoInterno(int codigoInterno) {
		_codigoInterno = codigoInterno;
	}

	public void setSigla(String sigla) {
		_sigla = sigla;
	}

	public void setDesignacao(String designacao) {
		_designacao = designacao;
	}

	public void setEstado(String estado) {
		_estado = estado;
	}

	public void setAssiduidade(String assiduidade) {
		_assiduidade = assiduidade;
	}

	/* teste da igualdade */
	public boolean equals(Object obj) {
		boolean resultado = false;

		if (obj instanceof StatusAssiduidade) {
			StatusAssiduidade status = (StatusAssiduidade) obj;

			resultado =
				(this.getCodigoInterno() == status.getCodigoInterno()
					&& this.getSigla() == status.getSigla()
					&& this.getDesignacao() == status.getDesignacao()
					&& this.getEstado() == status.getEstado()
					&& this.getAssiduidade() == status.getAssiduidade());
		}
		return resultado;
	}
}