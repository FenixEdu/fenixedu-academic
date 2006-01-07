/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;

import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 12:00:18,29/Set/2005
 * @version $Id$
 */
public class DeleteUserGroup implements IService
{
	public void run(UserGroup group)
	{
		group.delete();
	}
}
