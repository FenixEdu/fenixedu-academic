/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop.utils
 * 
 * Created on 9/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;

/**
 * @author jpvl
 *
 * 
 */
public abstract class ServiceUtils {

	public static Object executeService(
		IUserView userView,
		String serviceName,
		Object[] args)
		throws Exception {
		GestorServicos serviceManager = GestorServicos.manager();
		return serviceManager.executar(userView, serviceName, args);
	}


}
