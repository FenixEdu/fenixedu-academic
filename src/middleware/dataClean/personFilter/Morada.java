package middleware.dataClean.personFilter;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Morada {

  private int _chave;
  private String _nacionalidade;
  private String _distrito;
  private String _concelho;
  private String _freguesia;



  public Morada(int chave, String nacionalidade, String distrito, String concelho, String freguesia) {
    _chave = chave;
    _nacionalidade = nacionalidade;
    _distrito = distrito;
    _concelho = concelho;
    _freguesia = freguesia;
  }

  public int getChave() {return _chave;}
  public String getNacionalidade() {
    if (_nacionalidade == null)
      return "00";
    return _nacionalidade;
  }

  public String getDistrito() {return _distrito;}
  public String getConcelho() {return _concelho;}
  public String getFreguesia() {return _freguesia;}

  public void setChave( int chave ) { _chave = chave; }
  public void setNacionalidade( String nacionalidade ) { _nacionalidade = nacionalidade; }
  public void setDistrito( String distrito ) {_distrito = distrito;}
  public void setConcelho( String concelho ) {_concelho = concelho;}
  public void setFreguesia( String freguesia ) {_freguesia = freguesia;}


  public boolean equals(Object moradaObj){
    boolean resultado;
    Morada morada = (Morada)moradaObj;

    if (morada == null)
      resultado = false;
    else
      resultado = (this.getChave() == morada.getChave()) &&
          (this.getDistrito().compareTo(morada.getDistrito()) == 0) &&
          (this.getConcelho().compareTo(morada.getConcelho()) == 0) &&
          (this.getFreguesia().compareTo(morada.getFreguesia()) == 0) &&
      (this.getNacionalidade().compareTo(morada.getNacionalidade()) == 0);
    return resultado;
  }




}