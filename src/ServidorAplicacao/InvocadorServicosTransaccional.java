package ServidorAplicacao;

/**
 * @author jorge
 **/

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
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
      } catch (FenixServiceException ex) {
      	if (ex.getCause() instanceof ExcepcaoPersistencia) {
	        try {
    	      sp.cancelarTransaccao();
        		} 
        	catch (ExcepcaoPersistencia newEx) {
          		throw new FenixServiceException(newEx.getMessage());
        		}
			throw ex;
			//throw new FenixServiceException(ex.getMessage());         	
      	} else
      		throw ex;
      }
    } catch (ExcepcaoPersistencia ex) {
      throw new FenixServiceException(ex.getMessage());
    }
    
    return result;
  }
}
