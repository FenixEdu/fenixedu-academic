package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangePersonalContactInformation extends Service {

    public IUserView run(IUserView userView, InfoPerson newInfoPerson) throws ExcepcaoPersistencia {
        Person person = Person.readPersonByUsername(userView.getUtilizador());

        person.editPersonalContactInformation(newInfoPerson);
        
        return userView;
    }
}