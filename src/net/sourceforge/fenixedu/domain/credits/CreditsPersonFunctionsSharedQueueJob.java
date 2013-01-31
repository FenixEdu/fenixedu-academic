package net.sourceforge.fenixedu.domain.credits;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunctionShared;
import net.sourceforge.fenixedu.domain.organizationalStructure.SharedFunction;

public class CreditsPersonFunctionsSharedQueueJob extends CreditsPersonFunctionsSharedQueueJob_Base {

	public CreditsPersonFunctionsSharedQueueJob(ExecutionSemester executionSemester) {
		super();
		setExecutionSemester(executionSemester);
	}

	@Override
	public QueueJobResult execute() throws Exception {
		ExecutionSemester previousExecutionSemester =
				getExecutionSemester().getExecutionYear().getPreviousExecutionYear()
						.getExecutionSemesterFor(getExecutionSemester().getSemester());
		for (PersonFunction personFunction : previousExecutionSemester.getPersonFunction()) {
			if (personFunction instanceof PersonFunctionShared) {
				PersonFunctionShared personFunctionShared = (PersonFunctionShared) personFunction;
				SharedFunction sharedFunction = personFunctionShared.getSharedFunction();
				if (sharedFunction.getEndDateYearMonthDay() == null) {
					sharedFunction.setEndDateYearMonthDay(previousExecutionSemester.getEndDateYearMonthDay());
					new SharedFunction(sharedFunction.getTypeName(), getExecutionSemester().getBeginDateYearMonthDay(), null,
							sharedFunction.getFunctionType(), sharedFunction.getUnit(), BigDecimal.ZERO);
				}
			}
		}
		QueueJobResult queueJobResult = new QueueJobResult();
		queueJobResult.setDone(true);
		return queueJobResult;
	}
}
