package middleware.dataClean.personFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Distrito {
  private int _chave;
  private String _distrito;


  public Distrito(int chave, String distrito) {
    _chave = chave;
    _distrito = distrito;
  }

  public String getDistrito() {return _distrito;}
  public int getChave() {return _chave;}

  public void setChave( int chave )  {_chave = chave; }
  public void setDistrito( String distrito ) {_distrito = distrito;}
  
  public String toString(){
  	String result = "Distrito: [";
  	result += " distrito " + _distrito; 
  	result += " ]";
  	return result;
  }

}