package middleware.dataClean.personFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Pais {
  private int _chave;
  private String _pais;

  public Pais(int chave, String pais) {
    _chave = chave;
    _pais = pais;
  }

  public String getPais() {return _pais;}
  public int getChave() {return _chave;}

  public void setChave( int chave )  {_chave = chave; }
  public void setPais( String pais ) {_pais = pais;}


}