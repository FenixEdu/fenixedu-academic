package ServidorAplicacao;

/**
 * @author jorge
 **/

import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class InvocadorServicosTransaccional extends InvocadorServicos {

  public final static InvocadorServicosTransaccional invocador = new InvocadorServicosTransaccional();

  public final Object invoke(IServico servico, Object argumentos[])
    throws Exception
  {
    Object result = null;
    
    try {
      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
      try {
        sp.iniciarTransaccao();
        result = doInvocation(servico, "run", argumentos);
        sp.confirmarTransaccao();
      } catch (NotExecutedException ex) {
        try {
          sp.cancelarTransaccao();
        } catch (ExcepcaoPersistencia newEx) {
          throw new NotExecutedException(newEx.getMessage());
        }
        throw new NotExecutedException(ex.getMessage()); 
      }
    } catch (ExcepcaoPersistencia ex) {
      throw new NotExecutedException(ex.getMessage());
    }
    
    return result;
  }
}
