package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ManageUnitPersistentGroup extends Filtro {

    public static final ManageUnitPersistentGroup instance = new ManageUnitPersistentGroup();

    public void execute(Object[] parameters) throws Exception {
        IUserView userView = AccessControl.getUserView();
        Person person = userView.getPerson();

        PersistentGroupMembers group = null;
        if (parameters[0] instanceof PersistentGroupMembers) {
            group = (PersistentGroupMembers) parameters[0];
        }

        Unit unit = (group == null) ? (Unit) parameters[0] : group.getUnit();

        if (!(unit.getSite() != null && unit.getSite().hasManagers(person))) {
            throw new NotAuthorizedFilterException("error.person.not.manager.of.site");
        }

    }

}
