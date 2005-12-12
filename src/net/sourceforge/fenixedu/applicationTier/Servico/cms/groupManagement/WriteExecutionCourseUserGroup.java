/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.accessControl.IExecutionCourseUserGroup;
import net.sourceforge.fenixedu.domain.accessControl.IUserGroup;
import net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import relations.GroupCreation;
import relations.GroupOwnership;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:32:41,29/Set/2005
 * @version $Id$
 */
public class WriteExecutionCourseUserGroup implements IService
{
	public IUserGroup run(IExecutionCourse leafHook, String name, String description, IPerson owner,
			UserGroupTypes type) throws FenixServiceException, InstantiationException,
			IllegalAccessException, ExcepcaoPersistencia
	{
		IExecutionCourseUserGroup group = (IExecutionCourseUserGroup) type.getImplementationClass().newInstance();
		group.setName(name);
		group.setDescription(description);
		group.setExecutionCourse(leafHook);
		GroupOwnership.add(owner,group);
		GroupCreation.add(owner,group);
		
	
		return group;
	}	
}