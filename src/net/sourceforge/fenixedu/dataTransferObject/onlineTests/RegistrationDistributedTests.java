package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationDistributedTests extends InfoObject {

    private DomainListReference<DistributedTest> distributedTestsToDo;

    private DomainListReference<DistributedTest> distributedTestsDone;

    private DomainReference<Registration> registration;

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
	this.distributedTestsToDo = new DomainListReference<DistributedTest>(distributedTests);
    }

    public List<DistributedTest> getDistributedTestsDone() {
	return distributedTestsDone;
    }

    public void setDistributedTestsDone(List<DistributedTest> distributedTests) {
	this.distributedTestsDone = new DomainListReference<DistributedTest>(distributedTests);
    }

    public Registration getRegistration() {
	return registration == null ? null : registration.getObject();
    }

    public void setRegistration(Registration registration) {
	this.registration = new DomainReference<Registration>(registration);
    }

}