package ServidorAplicacao.Filtro;

/**
 * Manages the filters associated with a given service.
 * 
 * @author Joao Pereira
 * @version
 */

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.InvalidServiceException;

public class GestorFiltros
{
    private List _filters;

    private static final String FILTROS_CONF = "/filtros.conf";
    private static final String PREFIXO_FILTRO = "ServidorAplicacao.Filtro.";

    // The vector of existing filters by the order they should be called
    private static String _filterOrder[];
    // The corresponding singleton filters
    private static Filtro _filterSingletons[];

    {
        initializeStaticState();
    }

    /**
	 * The method that initializes the static state of this class. It initializes the ordering of
	 * execution of the filters and the filter instance corresponding to each
	 */
    private void initializeStaticState()
    {
        Properties properties = new Properties();
        String methodName = "getInstance";
        StringTokenizer st;
        int i = 0;

        try
        {
            properties.load(getClass().getResourceAsStream(FILTROS_CONF));
        } catch (NullPointerException e)
        {
            throw new InvalidServiceException("Filter specification not found: " + e);
        } catch (IOException ioe)
        {
            throw new InvalidServiceException("Error reading filter specification file: " + ioe);
        }

        // obtain the filter order
        String order = properties.getProperty("filterOrder");

        if (order == null)
            throw new InvalidServiceException(
                "Filter ordering not specified in the" + " filter specification file.");

        st = new StringTokenizer(order, "; \t\n\r\f:");
        _filterOrder = new String[st.countTokens()];

        while (st.hasMoreTokens())
        {
            _filterOrder[i++] = st.nextToken();
        }

        // computes the association between the filter names and the corresponding instances
        _filterSingletons = new Filtro[_filterOrder.length];

        for (i = 0; i < _filterOrder.length; i++)
        {
            String filterName = _filterOrder[i];
            Class filterClass;
            String filterClassName = null;
            Method method;

            if (filterName == null)
                throw new InvalidServiceException(
                    "Invalid filter specification file. The class of filter "
                        + filterName
                        + " is not specified.");

            try
            {
                // load class
                filterClassName = properties.getProperty(filterName);
                filterClass = Class.forName(PREFIXO_FILTRO + filterClassName);
                // obtain the invokation filter instance
                method = filterClass.getDeclaredMethod(methodName, null);
                _filterSingletons[i] = (Filtro) method.invoke(filterClass, null);
            } catch (ClassNotFoundException e)
            {
                throw new InvalidServiceException(
                    "The class corresponding to filter " + filterName + " does not exist: " + e);
            } catch (NoSuchMethodException e)
            {
                throw new InvalidServiceException(
                    "Method " + methodName + " does not exist for filter " + filterClassName + ". " + e);
            } catch (SecurityException e)
            {
                throw new InvalidServiceException(
                    "Cannot access " + methodName + " of filter class " + filterClassName + ". " + e);
            } catch (IllegalAccessException e)
            {
                throw new InvalidServiceException(
                    "Cannot access " + methodName + " of filter class " + filterClassName + ". " + e);
            } catch (IllegalArgumentException e)
            {
                throw new InvalidServiceException(
                    "Cannot execute " + methodName + " of filter class " + filterClassName + ". " + e);
            } catch (InvocationTargetException e)
            {
                throw new InvalidServiceException(
                    "Exception in execution of method "
                        + methodName
                        + " of filter class "
                        + filterClassName
                        + ". "
                        + e);
            }
        }
    }

    /**
	 * The ctor of the GestorFiltros class.
	 * 
	 * @param filters
	 *            is the set of filter names associated with the service managed by this filter manager.
	 * 
	 * @param executionType
	 *            is the type of the service execution.
	 * @see ServidorAplicacao.InvocadorServicos
	 * 
	 * @throws InvalidServiceException
	 *             if at least one of specified filters is not a valid filter.
	 */
    public GestorFiltros(Set filters)
    {
        _filters = new ArrayList();

        for (int i = 0; i < _filterOrder.length; i++)
        {
            if (filters.contains(_filterOrder[i]))
            {
                _filters.add(_filterSingletons[i]);
            }
        }

        if (filters.size() != _filters.size())
            // Specification of an invalid filter
            throw new InvalidServiceException("Invalid specification of filters: " + filters.toString());
    }

    /**
	 * Calls the preFilter method of each filter registered for the called service. If at least one of
	 * the filters does not allow the execution of the given service, then an exception is thrown.
	 * 
	 * @throws FenixServiceException
	 * @throws NotAuthorizedException
	 */
    public void preFiltragem(IUserView id, IServico servico, Object argumentos[]) throws Exception
    {
        Iterator iter = _filters.iterator();

        while (iter.hasNext())
             ((Filtro) iter.next()).preFiltragem(id, servico, argumentos);

    }
}
