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
				System.out.print("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome());
				if (user != null)  {
					System.out.println(" USERVIEW= " + user.getUtilizador());
				} else {
					System.out.println(" USERVIEW= no user view");
				}
				sp.iniciarTransaccao();
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + "Started Transaction");
				filterChain.preFiltragem(user, servico, argumentos);
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + "Finished PreFilter");
				sp.confirmarTransaccao();
				sp.iniciarTransaccao();
				result = doInvocation(servico, "run", argumentos);
				sp.confirmarTransaccao();
				System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + servico.getNome() + " finished sucessfully.");
			} catch (Exception ex) {
					System.out.println("=========== Exception running one service ==============");
					if (ex instanceof RuntimeException) {
						System.out.println("The cache was cleared!");
						sp.clearCache();
					}
					System.out.print("LOGTIME= " + new Date());
					if (user != null)  {
						System.out.print(" USERVIEW= " + user.getUtilizador());
					} else {
						System.out.print(" USERVIEW= no user view");
					}
					if (servico != null) {
						System.out.println(" SERVICE= " + servico.getNome());
					} else {
						System.out.println(" SERVICE= no service");
					}
					ex.printStackTrace(System.out);
					System.out.println("========= End of Exception (before abort transaction) ================");
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
