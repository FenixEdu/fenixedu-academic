package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.Person;

public class ChangePersonalContactInformation extends Service {

    public IUserView run(IUserView userView, InfoPersonEditor newInfoPerson) {
        Person.readPersonByUsername(userView.getUtilizador()).editPersonalContactInformation(newInfoPerson);
        return userView;
    }
}