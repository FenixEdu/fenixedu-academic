package ServidorAplicacao;

/**
 * The abstract class that invokes the "run" method of a service. The
 * subclasses of this class define the semantic of the invocation,
 * e.g., none or transactional.
 *
 * @author Joao Pereira
 * @version
 **/

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public abstract class InvocadorServicos {

  public static final int NONE = 0;
  public static final int TRANSACTIONAL = 1;

  /**
   * Returns the instance of the InvocadorServicos class that handles the
   * invocation of services with the specified semantic type.
   **/
  public static final InvocadorServicos getInvoker(int type) throws InvalidServiceException {
    switch (type) {
      case NONE:
        return InvocadorServicosSimples.invocador;
      case TRANSACTIONAL:
        return InvocadorServicosTransaccional.invocador;
        
      default:
        throw new InvalidServiceException("Invalid service invocation type: " + type);
    }
  }

  /**
   * Does the invocation of the service. It invokes the method called
   * methodName of servico with parameters argumentos.
   *
   * @param servico is the instance of IServico to be executed.
   * @param methodName is the name of the method to invoke.
   * @param argumentos is the set of arguments to be used in the invocation
   * of the service.
   *
   * @return the resukt of the invocation.
   *
   * @throws NotExecutedException if something goes wrong.
   **/
  final protected Object doInvocation(IServico servico, String methodName,
				      Object argumentos[]) throws Exception //NotExecutedException
  {
    // The Java reflexion mewchanism does not handle the case where an angument type
    // is null. In this case, the methos is not found. To overcame this problem,
    // we lookup all methods of the service with the desired name. If these is a single
    // method, we call that method. Otherwise, we try to get the desired method given
    // the types of the arguments. Notice that this may not work, if at least one of the
    // arguments is null.

    Method method = null;
    int methodPos = -1;
    int i = 0;

    try {
      Method methods[] = servico.getClass().getMethods();

      if (methods != null)
	for (i = 0; i < methods.length; i++) {
	  if (methods[i].getName().equals(methodName))
	    if (methodPos == -1)
	      methodPos = i;
	    else {
	      methodPos = -2;
	      break;
	    }
	}

      if (methodPos >= 0) 
	method = methods[methodPos];

      if (methodPos == -1) // not found
	throw new NotExecutedException("Cannot invoke service: "
				       + servico.getNome() + ". Method " + methodName
				       + " does not exist.");

    }
    catch (SecurityException e) {
      throw new NotExecutedException("Cannot execute method " +
				     methodName + " of service: " +
				     servico.getNome() + ": " + e);
    }

    if (methodPos == -2) { // found, but there are several methods with the same name
      try {
	Class types[] = new Class[argumentos != null ? argumentos.length : 0];
	
	for (i = 0; i < types.length; i++)
        types[i] = argumentos[i].getClass();
	
	method = servico.getClass().getDeclaredMethod("run", types);
      }
      catch (NoSuchMethodException e) {
	throw new NotExecutedException("Cannot execute method run of service: "
				       + servico.getNome() + ": " + e);
      }
      catch (SecurityException e) {
	throw new NotExecutedException("Cannot execute method run of service: "
				       + servico.getNome() + ": " + e);
      }
    }
    
    // execute the service.

    try {
      return method.invoke(servico, argumentos);
    }
    catch (IllegalAccessException e) {
      throw new NotExecutedException("Cannot access method run of service: "
				    + servico.getNome() + ": " + e);
    }
    catch ( IllegalArgumentException e) {
      throw new NotExecutedException("Cannot execute method run of service: "
				    + servico.getNome() + ": " + e);
    }
    catch (InvocationTargetException e) {
      if (e.getCause() instanceof Exception) {
        throw (Exception) e.getCause();
      } else {
        throw new NotExecutedException("Exception in execution of method run " +
                                        "of service: " + servico.getNome() + 
                                        ": " + e);
      }
    }
  }

  public abstract Object invoke(IServico service, Object argumentos[]) 
    throws Exception;
}
