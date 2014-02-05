package pt.utl.ist.scripts.process;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UpdatePersonName extends CustomTask {

    @Override
    public void runTask() throws Exception {
        updateName("2306397667631");
    }

    @Atomic
    private void updateName(String personOID) {
        Person person = FenixFramework.getDomainObject(personOID);
        person.setName("Gonçalo Antunes de Oliveira Tiago");
    }

}
