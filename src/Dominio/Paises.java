package Dominio;


public class Paises {
  private final int _codigoInterno;
  private int _codigoPais;
  private String _nomePais;
  private String _nacionalidade;
  private String _sigla;

  /* Construtores */
  public Paises(int codigoInterno, int codigoPais, String nomePais, String nacionalidade, String sigla) {
   _codigoInterno = codigoInterno;
   _codigoPais = codigoPais;
   _nomePais = nomePais;
   _nacionalidade = nacionalidade;
   _sigla = sigla;
  }

  public Paises() {
   _codigoInterno = 0;
   _codigoPais = 0;
   _nomePais = new String("");
   _nacionalidade = new String("");
   _sigla = new String("");
  }
  
  /* Selectores */
  public int getCodigoInterno() {
    return _codigoInterno;
  }
  public int getCodigoPais() {
    return _codigoPais;
  }
  public String getNomePais() {
    return _nomePais;
  }
  public String getNacionalidade() {
    return _nacionalidade;
  }
  public String getSigla() {
    return _sigla;
  }


 /* Modificadores */
   public void setCodigoPais(int codigoPais) {
    _codigoPais = codigoPais;
  }
   public void setNomePais(String nomePais) {
    _nomePais = nomePais;
  }
   public void setNacionalidade(String nacionalidade) {
    _nacionalidade = nacionalidade;
  }
   public void setSigla(String sigla) {
    _sigla = sigla;
  }

}