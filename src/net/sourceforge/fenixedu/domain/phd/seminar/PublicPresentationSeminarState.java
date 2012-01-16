package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.joda.time.DateTime;

public class PublicPresentationSeminarState extends PublicPresentationSeminarState_Base {

    private PublicPresentationSeminarState() {
	super();
    }

    PublicPresentationSeminarState(final PublicPresentationSeminarProcess process,
	    final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks,
	    final DateTime stateDate) {
	this();
	check(process, "error.PublicPresentationSeminarState.invalid.process");
	check(type, "error.PublicPresentationSeminarState.invalid.type");

	checkType(process, type);
	setProcess(process);

	super.init(person, remarks, stateDate);

	setType(type);
    }

    protected void init(final Person person, final String remarks, DateTime stateDate) {
	throw new RuntimeException("invoke other init");
    }

    private void checkType(final PublicPresentationSeminarProcess process, final PublicPresentationSeminarProcessStateType type) {
	final PublicPresentationSeminarProcessStateType currentType = process.getActiveState();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PublicPresentationSeminarState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Override
    public boolean isLast() {
	return getProcess().getMostRecentState() == this;
    }

    public static PublicPresentationSeminarState createWithInferredStateDate(final PublicPresentationSeminarProcess process,
	    final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {
	DateTime stateDate = null;

	PublicPresentationSeminarState mostRecentState = process.getMostRecentState();

	switch (type) {
	case WAITING_FOR_COMMISSION_CONSTITUTION:
	    if (process.getPresentationRequestDate() == null) {
		throw new PhdDomainOperationException(
			"error.phd.seminar.PublicPresentationSeminarState.presentationRequestDate.required");
	    }

	    stateDate = process.getPresentationRequestDate().toDateTimeAtStartOfDay();
	    break;
	case COMMISSION_WAITING_FOR_VALIDATION:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	case COMMISSION_VALIDATED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	case PUBLIC_PRESENTATION_DATE_SCHEDULED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	case REPORT_WAITING_FOR_VALIDATION:
	    if (process.getMostRecentStateByType(PublicPresentationSeminarProcessStateType.REPORT_WAITING_FOR_VALIDATION) != null) {
		stateDate = mostRecentState.getStateDate().plusMinutes(1);
		break;
	    }

	    if (process.getPresentationDate() == null) {
		throw new PhdDomainOperationException(
			"error.phd.seminar.PublicPresentationSeminarState.presentationDate.required");
	    }

	    stateDate = process.getPresentationDate().toDateTimeAtStartOfDay();
	    break;
	case REPORT_VALIDATED:
	    stateDate = mostRecentState.getStateDate().plusMinutes(1);
	    break;
	case EXEMPTED:
	    stateDate = new DateTime();
	    break;
	}

	return createWithGivenStateDate(process, type, person, remarks, stateDate);
    }

    public static PublicPresentationSeminarState createWithGivenStateDate(final PublicPresentationSeminarProcess process,
	    final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks,
	    final DateTime stateDate) {
	List<PublicPresentationSeminarProcessStateType> possibleNextStates = PublicPresentationSeminarProcessStateType
		.getPossibleNextStates(process);

	if (!possibleNextStates.contains(type)) {
	    String expectedStatesDescription = buildExpectedStatesDescription(possibleNextStates);
	    throw new PhdDomainOperationException("error.phd.seminar.PublicPresentationSeminarState.invalid.next.state",
		    expectedStatesDescription);
	}

	return new PublicPresentationSeminarState(process, type, person, remarks, stateDate);
    }
}
