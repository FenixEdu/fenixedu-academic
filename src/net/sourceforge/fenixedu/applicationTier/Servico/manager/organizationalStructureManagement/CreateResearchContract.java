package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.AddPersonRole;
import net.sourceforge.fenixedu.domain.Login;
import net.sourceforge.fenixedu.domain.LoginPeriod;
import net.sourceforge.fenixedu.domain.LoginRequest;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ResearchContractBean;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class CreateResearchContract extends Service {

	public void run(ResearchContractBean bean, Person creator, String url) {
		Person person = bean.getPerson();
		if (person == null) {
			person = Person.createExternalPerson(bean.getPersonNameString(), Gender.MALE, null, null,
					null, null, bean.getEmail(), bean.getDocumentIDNumber(), bean.getDocumentType());
		}
		new ResearchContract(person, bean.getBegin(), bean.getEnd(), bean.getUnit(), bean
				.getFunctionType(), bean.getExternalPerson());
		
		Login loginIdentification = person.getLoginIdentification();
		if (loginIdentification.getPassword() == null) {
			person.addPersonRoleByRoleType(RoleType.PERSON);
			person.addPersonRoleByRoleType(RoleType.RESEARCHER);
			
			new LoginPeriod(bean.getBegin(), bean.getEnd(), loginIdentification);
			LoginRequest request = new LoginRequest(person.getUser());

			String subject = RenderUtils.getResourceString("WEBSITEMANAGER_RESOURCES",
					"email.login.subject");
			String message = RenderUtils.getResourceString("WEBSITEMANAGER_RESOURCES",
					"email.login.message", new Object[] { bean.getPersonNameString(), creator.getName(),
							url + request.getHash() });

			List<String> tos = new ArrayList<String>();
			tos.add(bean.getEmail());
			new Email(creator.getName(), creator.getEmail(), null, tos, null, null, subject, message);
		}
	}

}
