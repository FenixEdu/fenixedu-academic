package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
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
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import org.joda.time.YearMonthDay;

public class CreateResearchContract extends Service {

	public void run(ResearchContractBean bean, Person creator, String url) throws FenixServiceException {
		Person person = bean.getPerson();
		if (person == null) {
			if (Person.readPersonByEmailAddress(bean.getEmail())!=null) {
				throw new FenixServiceException("error.email.already.in.use"); 
			}
			person = Person.createExternalPerson(bean.getPersonNameString(), Gender.MALE, null, null,
					null, null, bean.getEmail(), bean.getDocumentIDNumber(), bean.getDocumentType());
		}
		ResearchContract.createResearchContract(bean.getContractType(),person, bean.getBegin(), bean.getEnd(), bean.getUnit(), bean.getExternalPerson());
		if (person.getPersonRole(RoleType.RESEARCHER) == null) {
		    person.addPersonRoleByRoleType(RoleType.RESEARCHER);			
		}
		
		Login loginIdentification = person.getLoginIdentification();
		
		if(loginIdentification == null) {
		    User user = person.getUser();
		    if(user == null) {
			user = new User(person);
		    }
		    loginIdentification = new Login(user);
		}
		
		if (loginIdentification.getPassword() == null) {
			person.addPersonRoleByRoleType(RoleType.PERSON);
			
			new LoginPeriod(bean.getBegin(), (bean.getEnd()!=null ? bean.getEnd() : new YearMonthDay().plusYears(1)), loginIdentification);
			LoginRequest request = new LoginRequest(person.getUser());

			String subject = RenderUtils.getResourceString("WEBSITEMANAGER_RESOURCES",
					"email.login.subject");
			String message = RenderUtils.getResourceString("WEBSITEMANAGER_RESOURCES",
					"email.login.message", new Object[] { bean.getPersonNameString(), creator.getName(),
							url + request.getHash(), bean.getUnit().getName(), person.getUsername() });

			List<String> tos = new ArrayList<String>();
			tos.add(bean.getEmail());
			if(person.getEmail() == null) {
			    person.setEmail(bean.getEmail());
			}
			new Email(creator.getName(), creator.getEmail(), null, tos, null, null, subject, message);
		}
	}

}
