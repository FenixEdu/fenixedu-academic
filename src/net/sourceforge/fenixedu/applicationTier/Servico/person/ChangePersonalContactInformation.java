package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;

public class ChangePersonalContactInformation extends Service {

    public IUserView run(IUserView userView, InfoPersonEditor newInfoPerson) {
        if (userView != null) {
            userView.getPerson().editPersonalContactInformation(newInfoPerson);
        }
        return userView;
    }

}