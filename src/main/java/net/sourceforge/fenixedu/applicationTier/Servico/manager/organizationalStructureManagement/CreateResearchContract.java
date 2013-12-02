package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.webSiteManager.ResearchContractBean;
import pt.ist.fenixframework.Atomic;

public class CreateResearchContract {

    @Atomic
    public static void run(ResearchContractBean bean) throws FenixServiceException {
        Person person = bean.getPerson();
        if (person == null) {
            if (Person.readPersonByEmailAddress(bean.getEmail()) != null) {
                throw new FenixServiceException("error.email.already.in.use");
            }
            person =
                    Person.createExternalPerson(bean.getPersonNameString(), Gender.MALE, null, null, null, null, bean.getEmail(),
                            bean.getDocumentIDNumber(), bean.getDocumentType());
        }
        ResearchContract.createResearchContract(bean.getContractType(), person, bean.getBegin(), bean.getEnd(), bean.getUnit(),
                bean.getExternalPerson());
        if (person.getPersonRole(RoleType.RESEARCHER) == null) {
            person.addPersonRoleByRoleType(RoleType.RESEARCHER);
        }

        // At this point it is guaranteed that the person has a user
    }
}