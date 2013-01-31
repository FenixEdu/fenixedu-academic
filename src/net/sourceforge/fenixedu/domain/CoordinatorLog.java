package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CoordinatorLog extends CoordinatorLog_Base {

	public CoordinatorLog() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public CoordinatorLog(DateTime time, OperationType operation, Person person, ExecutionDegree executionDegree, Person personWho) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		super.setDate(time);
		super.setOperation(operation);
		super.setPerson(person);
		super.setPersonWho(personWho);
		super.setExecutionDegree(executionDegree);
	}

	@Service
	public static CoordinatorLog createCoordinatorLog(DateTime time, OperationType operation, Person personActing,
			Coordinator coordinator) {

		return new CoordinatorLog(time, operation, coordinator.getPerson(), coordinator.getExecutionDegree(), personActing);
	}

}
