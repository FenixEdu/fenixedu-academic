package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import Dominio.Modalidade;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IModalidadePersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerModalidade extends ServicoSeguro {
  
  private ArrayList _modalidades = null;
  private Modalidade _modalidade = null;
  private int _chaveModalidade;
  
  public ServicoSeguroLerModalidade(ServicoAutorizacao servicoAutorizacaoLerModalidade,
  int chaveModalidade) {
    super(servicoAutorizacaoLerModalidade);
    _chaveModalidade = chaveModalidade;
  }
  public void execute() throws NotExecuteException {
    IModalidadePersistente iModalidadePersistente = SuportePersistente.getInstance().iModalidadePersistente();
    if(_chaveModalidade == 0){
      if((_modalidades = iModalidadePersistente.lerModalidades()) == null){
        throw new NotExecuteException();
      }
    }else{
      if((_modalidade = iModalidadePersistente.lerModalidade(_chaveModalidade)) == null){
        throw new NotExecuteException();
      }
    }
  }
  
  public Modalidade getModalidade() {
    return _modalidade;
  }
  
  public ArrayList getModalidades() {
    return _modalidades;
  }
}