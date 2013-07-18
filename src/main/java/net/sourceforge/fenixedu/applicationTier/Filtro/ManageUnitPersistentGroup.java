package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ManageUnitPersistentGroup extends Filtro {

    public static final ManageUnitPersistentGroup instance = new ManageUnitPersistentGroup();

    public void execute(Unit unit) throws NotAuthorizedException {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();
        if (!(unit.getSite() != null && unit.getSite().hasManagers(person))) {
            throw new NotAuthorizedException("error.person.not.manager.of.site");
        }
    }

}
