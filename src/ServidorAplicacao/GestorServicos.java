package ServidorAplicacao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import ServidorAplicacao.Filtro.GestorFiltros;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidServiceException;

/**
 * This class is the entry point of the system to execute a service. It receives the service to execute,
 * its arguments and an identificator of the entity that wants to run the service.
 * 
 * The static method init must be executed before accessing the singleton of this class.
 * 
 * @author Joao Pereira
 * @version
 */

public class GestorServicos
{

    public class AssociacaoServico
    {
        public IServico servico;
        public GestorFiltros gestorFiltros;
        public InvocadorServicos invocador;

        public AssociacaoServico(IServico servico, GestorFiltros gestor, InvocadorServicos invocador)
        {
            this.servico = servico;
            this.gestorFiltros = gestor;
            this.invocador = invocador;
        }

        public final int hashCode()
        {
            return servico.getNome().hashCode();
        }

        public final boolean equals(Object another)
        {
            return (another instanceof AssociacaoServico)
                && servico.getNome().equals(((AssociacaoServico) another).servico.getNome());
        }

    }

    private static final String SERVICOS_CONF = "/servicos.conf";
    private static final String PREFIXO_SERVICO = "ServidorAplicacao.Servico.";

    private static GestorServicos _manager = new GestorServicos();

    // contains the association between a service and a GestorFiltros
    private Hashtable _table;

    /**
	 * Accesses the singleton.
	 */
    public static final GestorServicos manager()
    {
        return _manager;
    }

    /**
	 * The ctor of this class.
	 * 
	 * @throws InvalidServiceException
	 *                    if the service specification is not valid
	 */
    private GestorServicos()
    {
        _table = new Hashtable();

        Properties properties = new Properties();
        Enumeration enum;

        try
        {
            properties.load(getClass().getResourceAsStream(SERVICOS_CONF));
        }
        catch (NullPointerException ex)
        {
            throw new InvalidServiceException("Service specification not found: " + ex);
        }
        catch (IOException ex)
        {
            throw new InvalidServiceException("Error reading service specification file: " + ex);
        }

        enum = properties.propertyNames();

        while (enum.hasMoreElements())
        {
            String serviceName = (String) enum.nextElement();
            String values;
            StringTokenizer st;
            Set filters;
            int invocationType;

            if (serviceName.endsWith(".type"))
                // it is the property that defines the invocation type
                continue;

            values = properties.getProperty(serviceName);
            st = new StringTokenizer(values, "; \t\n\r\f:");
            filters = new HashSet();

            while (st.hasMoreTokens())
            {
                filters.add(st.nextToken());
                //println(st.nextToken());
            }

            String type = properties.getProperty(serviceName + ".type");

            try
            {
                invocationType = type != null ? Integer.parseInt(type) : InvocadorServicos.NONE;
            }
            catch (NumberFormatException e)
            {
                throw new InvalidServiceException(
                    "Bad format of property "
                        + serviceName
                        + "."
                        + type
                        + " ("
                        + type
                        + ") specified in the service configuration file: "
                        + e);
            }

            registerService(
                createService(PREFIXO_SERVICO + serviceName),
                new GestorFiltros(filters),
                InvocadorServicos.getInvoker(invocationType));
        }
    }

    /**
	 * Creates a service instance given the name of the service.
	 * 
	 * @throws InvalidServiceException
	 *                    if the service is not a valid service, namely, the service class does not exist, the
	 *                    service class does not specify the public static getSingleton method, or the
	 *                    execution of this method throws an exception
	 */
    private IServico createService(String serviceName) throws InvalidServiceException
    {

        Method method = null;
        String methodName = "getService";
        Class serviceClass;

        try
        {

            // load class
            serviceClass = Class.forName(serviceName);

            method = serviceClass.getDeclaredMethod(methodName, null);
        }
        catch (NoClassDefFoundError e)
        {
            e.printStackTrace(System.out);
            throw new InvalidServiceException(
                "Problems with service " + serviceName + " does not exist: " + e);
        }

        catch (ClassNotFoundException e)
        {
            throw new InvalidServiceException(
                "The class corresponding to service " + serviceName + " does not exist: " + e);
        }
        catch (NoSuchMethodException e)
        {
            throw new InvalidServiceException(
                "Method " + methodName + " does not exist for service " + serviceName + ". " + e);
        }
        //        catch (SecurityException e)
        //        {
        //            throw new InvalidServiceException(
        //                "Cannot access " + methodName + " of service class " + serviceName + ". " + e);
        //        }

        try
        {
            return (IServico) method.invoke(serviceClass, null);
        }
        catch (IllegalAccessException e)
        {
            throw new InvalidServiceException(
                "Cannot access " + methodName + " of service class " + serviceName + ". " + e);
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidServiceException(
                "Cannot execute " + methodName + " of service class " + serviceName + ". " + e);
        }
        catch (InvocationTargetException e)
        {
            throw new InvalidServiceException(
                "Exception in execution of method "
                    + methodName
                    + " of service class "
                    + serviceName
                    + ". "
                    + e);
        }
    }

    /**
	 * Executes a given service.
	 * 
	 * @param id
	 *                   represents the identification of the entity that desires to run the service
	 * 
	 * @param service
	 *                   is a string containing the name of the service to execute
	 * 
	 * @param argumentos
	 *                   is a vector with the arguments of the service to execute.
	 * 
	 * @throws FenixServiceException
	 * @throws NotAuthorizedException
	 */
    public Object executar(IUserView id, String servico, Object argumentos[])
        throws FenixServiceException
    {
        AssociacaoServico assoc = getInformacaoServico(servico);

        if (assoc == null || assoc.servico == null)
        {
            throw new FenixServiceException("Servico inválido : " + servico);
        }

        try
        {
            //assoc.gestorFiltros.preFiltragem(id, assoc.servico, argumentos);
            return assoc.invocador.invoke(id, assoc.servico, argumentos, assoc.gestorFiltros);
        }
        catch (FenixServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

    }

    /**
	 * Register a Service and its filter manager
	 */
    private void registerService(IServico servico, GestorFiltros gestor, InvocadorServicos invocador)
    {
        AssociacaoServico assoc = new AssociacaoServico(servico, gestor, invocador);

        _table.put(servico.getNome(), assoc);
    }

    /**
	 * Returns the service and corresponding filter manager given the name of the service.
	 */
    private final AssociacaoServico getInformacaoServico(String nome)
    {
        return (AssociacaoServico) _table.get(nome);
    }
}
