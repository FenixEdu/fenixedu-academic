package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.Regime;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IRegimePersistente {
  public boolean alterarRegime(Regime regime);
  public boolean apagarRegime(String designacao);
  public boolean escreverRegime(Regime regime);
  public ArrayList lerDesignacaoRegimes(ArrayList listaIdRegimes);  
  public Regime lerRegime(String designacao);
  public Regime lerRegime(int codigoInterno);
  public ArrayList lerRegimes();  
}