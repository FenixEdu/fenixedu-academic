/*
 * Created on 19/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.actions.LookupDispatchAction;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class FenixLookupDispatchAction extends LookupDispatchAction {

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
	 */
	 
	protected HttpSession getSession(HttpServletRequest request) 
		  throws ExcepcaoSessaoInexistente {
		HttpSession result = request.getSession(false);
		if (result == null)
		  throw new ExcepcaoSessaoInexistente();
    
		return result;
	  }
	 /**
	  *  This method returns a map (x,y)
	  * 
	  *  x - is a message resource identifier
	  *  y - is the name of the method which will be implemented within the subclasses
	  *   
	  * */
	protected abstract Map getKeyMethodMap();
}
