package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationDistributedTests extends InfoObject {

    private List<DistributedTest> distributedTestsToDo;

    private List<DistributedTest> distributedTestsDone;

    private Registration registration;

    public RegistrationDistributedTests(Registration registration, List<DistributedTest> distributedTestsToDo,
            List<DistributedTest> distributedTestsDone) {
        setRegistration(registration);
        setDistributedTestsToDo(distributedTestsToDo);
        setDistributedTestsDone(distributedTestsDone);
    }

    public List<DistributedTest> getDistributedTestsToDo() {
        return distributedTestsToDo;
    }

    public void setDistributedTestsToDo(List<DistributedTest> distributedTests) {
        this.distributedTestsToDo = distributedTests;
    }

    public List<DistributedTest> getDistributedTestsDone() {
        return distributedTestsDone;
    }

    public void setDistributedTestsDone(List<DistributedTest> distributedTests) {
        this.distributedTestsDone = distributedTests;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

}
