package middleware.dataClean.personFilter;



public class LimpaOutput {
  private int _pais;
  private int _distrito;
  private int _concelho;
  private int _freguesia;

	private String _nomePais;
	private String _nomeDistrito;
	private String _nomeConcelho;
	private String _nomeFreguesia;

  public LimpaOutput() {
    _pais = LimpaNaturalidades.other;
    _distrito = LimpaNaturalidades.other;
    _concelho = LimpaNaturalidades.other;
    _freguesia = LimpaNaturalidades.other;
  }

	public String toString(){
		String result = "LimpaOutput: [";
		result += " pais " + _nomePais; 
		result += " distrito " + _nomeDistrito; 
		result += " concelho " + _nomeConcelho; 
		result += " freguesia " + _nomeFreguesia; 
		result += " ]";
		return result;
	}
	
  public LimpaOutput(int pais, int distrito, int concelho, int freguesia) {
    _pais = pais;
    _distrito = distrito;
    _concelho = concelho;
    _freguesia = freguesia;
  }
  
	public LimpaOutput(String pais, String distrito, String concelho, String freguesia) {
		_nomePais = pais;
		_nomeDistrito = distrito;
		_nomeConcelho = concelho;
		_nomeFreguesia = freguesia;
	}

  public int getPais() {
    return _pais;
  }

  public int getDistrito() {
    return _distrito;
  }

  public int getConcelho() {
    return _concelho;
  }

  public int getFreguesia() {
    return _freguesia;
  }

  public void setPais(int pais) {
    _pais = pais;
  }

  public void setDistrito(int distrito) {
    _distrito = distrito;
  }

  public void setConcelho(int concelho) {
    _concelho = concelho;
  }

  public void setFreguesia(int freguesia) {
    _freguesia = freguesia;
  }

	public String getNomeConcelho() {
		return _nomeConcelho;
	}

	public String getNomeDistrito() {
		return _nomeDistrito;
	}

	public String getNomeFreguesia() {
		return _nomeFreguesia;
	}

	public String getNomePais() {
		return _nomePais;
	}

	public void setNomeConcelho(String string) {
		_nomeConcelho = string;
	}

	public void setNomeDistrito(String string) {
		_nomeDistrito = string;
	}

	public void setNomeFreguesia(String string) {
		_nomeFreguesia = string;
	}

	public void setNomePais(String string) {
		_nomePais = string;
	}
}
