package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import Dominio.Regime;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IRegimePersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerRegime extends ServicoSeguro {
  
  private ArrayList _regimes = null;
  private Regime _regime = null;
  private int _chaveRegime;
  
  public ServicoSeguroLerRegime(ServicoAutorizacao servicoAutorizacaoLerRegime,
  int chaveRegime) {
    super(servicoAutorizacaoLerRegime);
    _chaveRegime = chaveRegime;
  }
  public void execute() throws NotExecuteException {
    IRegimePersistente iRegimePersistente = SuportePersistente.getInstance().iRegimePersistente();
    if(_chaveRegime == 0){
      if((_regimes = iRegimePersistente.lerRegimes()) == null){
        throw new NotExecuteException();
      }
    }else{
      if((_regime = iRegimePersistente.lerRegime(_chaveRegime)) == null){
        throw new NotExecuteException();
      }
    }
  }
  
  public Regime getRegime() {
    return _regime;
  }
  
  public ArrayList getRegimes() {
    return _regimes;
  }
}