package middleware.dataClean.personFilter;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Freguesia {
  private int _chave;
  private int _distrito;
  private int _concelho;
  private String _freguesia;

  public Freguesia(int chave, int distrito, int concelho, String freguesia) {
    _chave = chave;
    _distrito = distrito;
    _concelho = concelho;
    _freguesia = freguesia;
  }

  public int getChave() {return _chave;}
  public int getDistrito() {return _distrito;}
  public int getConcelho() {return _concelho;}
  public String getFreguesia() {return _freguesia;}

  public void setChave( int chave )  {_chave = chave; }
  public void setDistrito( int distrito ) {_distrito = distrito;}
  public void setConcelho( int concelho ) {_concelho = concelho;}
  public void setFreguesia( String freguesia ) {_freguesia = freguesia;}

	public String toString(){
		String result = "Freguesia: [";
		result += " freguesia " + _freguesia; 
		result += " ]";
		return result;
	}
}