package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.EditPrizeFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonNameBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.presentationTier.Action.research.UnitNameBean;
import pt.ist.fenixWebFramework.services.Service;

public class AddPartyToPrize extends FenixService {

    protected void run(PersonNameBean bean, Prize prize) {
        Person person = (bean.getPersonName() == null) ? getNewExternalPerson(bean.getName()) : bean.getPersonName().getPerson();
        prize.addParties(person);
    }

    protected void run(UnitNameBean bean, Prize prize) {
        Unit unit = (bean.getUnitName() == null) ? getNewExternalUnit(bean.getRawName()) : bean.getUnitName().getUnit();
        prize.addParties(unit);
    }

    protected void run(Party party, Prize prize) {
        prize.addParties(party);
    }

    private Unit getNewExternalUnit(String rawName) {
        return Unit.createNewNoOfficialExternalInstitution(rawName);
    }

    private Person getNewExternalPerson(String name) {
        return Person.createExternalPerson(name, Gender.MALE, null, null, null, null, null,
                String.valueOf(System.currentTimeMillis()), IDDocumentType.EXTERNAL);
    }

    // Service Invokers migrated from Berserk

    private static final AddPartyToPrize serviceInstance = new AddPartyToPrize();

    @Service
    public static void runAddPartyToPrize(PersonNameBean bean, Prize prize) throws NotAuthorizedException {
        EditPrizeFilter.instance.execute(prize);
        serviceInstance.run(bean, prize);
    }

}