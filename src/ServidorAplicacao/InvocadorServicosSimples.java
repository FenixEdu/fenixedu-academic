package ServidorAplicacao;

import ServidorAplicacao.Filtro.GestorFiltros;

/**
 * The InvocadorServicos subclass that simply invokes the services
 * without adding any special functionality.
 *
 * @author Joao Pereira
 * @version
 **/

public class InvocadorServicosSimples extends InvocadorServicos {

	public final static InvocadorServicosSimples invocador =
		new InvocadorServicosSimples();

	/* (non-Javadoc)
	 * @see ServidorAplicacao.InvocadorServicos#invoke(ServidorAplicacao.IUserView, ServidorAplicacao.IServico, java.lang.Object[], ServidorAplicacao.Filtro.GestorFiltros)
	 */
	public Object invoke(
		IUserView user,
		IServico service,
		Object[] argumentos,
		GestorFiltros filterChain)
		throws Exception {
		filterChain.preFiltragem(user, service, argumentos);
		return doInvocation(service, "run", argumentos);
	}
}
