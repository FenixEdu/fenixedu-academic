package ServidorAplicacao;

/**
 * The InvocadorServicos subclass that simply invokes the services
 * without adding any special functionality.
 *
 * @author Joao Pereira
 * @version
 **/

public class InvocadorServicosSimples extends InvocadorServicos {

  public final static InvocadorServicosSimples invocador = new InvocadorServicosSimples();

  public final Object invoke(IServico servico, Object argumentos[])
    throws Exception
  {
    return doInvocation(servico, "run", argumentos);
  }
}
