package ServidorAplicacao;

/**
 * @author jorge
 **/

import java.util.Date;

import ServidorAplicacao.Filtro.GestorFiltros;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class InvocadorServicosTransaccional extends InvocadorServicos {

	public final static InvocadorServicosTransaccional invocador =
		new InvocadorServicosTransaccional();

	public final Object invoke(IUserView user, IServico servico, Object argumentos[], GestorFiltros filterChain)
		throws Exception {
		Object result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			try {
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome());
				if (user != null)  {
					System.out.println("USERVIEW= " + user.getUtilizador());
				} else {
					System.out.println("USERVIEW= no user view");
				}
				sp.iniciarTransaccao();
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + "Started Transaction");
				filterChain.preFiltragem(user, servico, argumentos);
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + "Finished PreFilter");
				result = doInvocation(servico, "run", argumentos);
				sp.confirmarTransaccao();
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + " finished sucessfully.");
			} catch (Exception ex) {
					if (ex instanceof RuntimeException) {
						System.out.println("*************************** CACHE CLEARED");
						sp.clearCache();
					}
						
				
					System.out.println("LOGTIME= " + new Date());
					if (user != null)  {
						System.out.println("USERVIEW= " + user.getUtilizador());
					} else {
						System.out.println("USERVIEW= no user view");
					}
					if (servico != null) {
						System.out.println("SERVICE= " + servico.getNome());
					} else {
						System.out.println("SERVICE= no service");
					}
					try {
						sp.cancelarTransaccao();
					} catch (ExcepcaoPersistencia newEx) {
						throw new FenixServiceException(newEx.getMessage());
					}
					throw ex;
					//throw new FenixServiceException(ex.getMessage());         	
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}
}
