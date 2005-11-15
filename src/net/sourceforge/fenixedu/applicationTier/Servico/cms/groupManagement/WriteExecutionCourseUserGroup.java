/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.cms.groupManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.cms.IExecutionCourseUserGroup;
import net.sourceforge.fenixedu.domain.cms.IUserGroup;
import net.sourceforge.fenixedu.domain.cms.UserGroupTypes;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import relations.CmsContents;
import relations.CmsUsers;
import relations.ContentOwnership;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:32:41,29/Set/2005
 * @version $Id$
 */
public class WriteExecutionCourseUserGroup extends CmsService
{
	public IUserGroup run(IExecutionCourse leafHook, String name, String description, IPerson owner,
			UserGroupTypes type) throws FenixServiceException, InstantiationException,
			IllegalAccessException
	{
		IExecutionCourseUserGroup group = (IExecutionCourseUserGroup) type.getImplementationClass().newInstance();
		group.setName(name);
		group.setDescription(description);
		group.setExecutionCourse(leafHook);
		ContentOwnership.add(owner, group);

		return group;
	}

	public void updateRootObjectReferences(ServiceRequest request, ServiceResponse response) throws FilterException,
			Exception
	{
		IPerson owner = (IPerson) request.getServiceParameters().getParameter(3);
		IUserGroup userGroup = (IUserGroup) response.getReturnObject();
		
		CmsUsers.add(this.readFenixCMS(), owner);
		CmsContents.add(this.readFenixCMS(),userGroup);
	}
	
}