/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Servico.cms.CmsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cms.website.ExecutionCourseWebsite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import relations.CmsContents;
import relations.CmsUsers;
import relations.ContentCreation;
import relations.ContentOwnership;
import relations.ExecutionCourseWebsiteRelation;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 13:53:22,7/Dez/2005
 * @version $Id$
 */
public class WriteExecutionCourseWebsite extends CmsService
{
	public static class ExecutionCourseAlreadyHasWebsiteException extends FenixServiceException
	{
		private static final long serialVersionUID = -8968236930724948984L;

		public ExecutionCourseAlreadyHasWebsiteException(String msg)
		{
			super(msg);
		}
	}
	static public class WriteExecutionCourseWebsiteParameters
	{
		private String name;
		private String description;
		private Integer executionCourseID;
		private Person person;
		
		public String getDescription()
		{
			return description;
		}
		public void setDescription(String description)
		{
			this.description = description;
		}
		public Integer getExecutionCourseID()
		{
			return executionCourseID;
		}
		public void setExecutionCourseID(Integer executionCourseID)
		{
			this.executionCourseID = executionCourseID;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public Person getPerson()
		{
			return person;
		}
		public void setPerson(Person person)
		{
			this.person = person;
		}
	}
	public ExecutionCourseWebsite run (WriteExecutionCourseWebsiteParameters parameters) throws ExcepcaoPersistencia, ExecutionCourseAlreadyHasWebsiteException
	{
		ExecutionCourseWebsite website = new ExecutionCourseWebsite();
		website.setName(parameters.getName());
		website.setDescription(parameters.getDescription());
		
		ExecutionCourse executionCourse = (ExecutionCourse) PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentObject().readByOID(ExecutionCourse.class,parameters.getExecutionCourseID());
		if (executionCourse.getExecutionCourseWebsite()!=null)
		{
			throw new ExecutionCourseAlreadyHasWebsiteException("The selected execution course already have a website");
		}
		ExecutionCourseWebsiteRelation.add(website,executionCourse);
		
		ContentCreation.add(parameters.getPerson(),website);
		ContentOwnership.add(parameters.getPerson(),website);
		this.updateRootObjectReferences(website);
		return website;
	}

	private void updateRootObjectReferences(ExecutionCourseWebsite website) throws ExcepcaoPersistencia
	{
		CmsContents.add(this.readFenixCMS(),website);
		CmsUsers.add(this.readFenixCMS(),website.getCreator());
	}
}
