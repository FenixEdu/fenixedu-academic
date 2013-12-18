package net.sourceforge.fenixedu.applicationTier.Filtro;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ManageUnitPersistentGroup extends Filtro {

    public static final ManageUnitPersistentGroup instance = new ManageUnitPersistentGroup();

    public void execute(Unit unit) throws NotAuthorizedException {
        User userView = Authenticate.getUser();
        Person person = userView.getPerson();
        if (!(unit.getSite() != null && unit.getSite().getManagersSet().contains(person))) {
            throw new NotAuthorizedException("error.person.not.manager.of.site");
        }
    }

}
