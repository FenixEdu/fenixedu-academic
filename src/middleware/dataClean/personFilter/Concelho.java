package middleware.dataClean.personFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Concelho {
  private int _chave;
  private int _distrito;
  private String _concelho;

  public Concelho(int chave, int distrito, String concelho) {
    _chave = chave;
    _distrito = distrito;
    _concelho = concelho;
  }

  public int getChave() {return _chave;}
  public int getDistrito() {return _distrito;}
  public String getConcelho() {return _concelho;}

  public void setChave( int chave )  {_chave = chave; }
  public void setDistrito( int distrito ) {_distrito = distrito;}
  public void setConcelho( String concelho ) {_concelho = concelho;}
  
	public String toString(){
		String result = "Concelho: [";
		result += " concelho " + _concelho; 
		result += " ]";
		return result;
	}
}