package net.sourceforge.fenixedu.domain.phd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdIndividualProgramProcessState implements PhdProcessStateType {

    CANDIDACY(true),

    NOT_ADMITTED,

    CANCELLED,

    WORK_DEVELOPMENT(true),

    SUSPENDED,

    THESIS_DISCUSSION(true),

    FLUNKED,

    CONCLUDED(true),

    TRANSFERRED;

    private boolean activeState;

    private PhdIndividualProgramProcessState(boolean activeState) {
	this.activeState = activeState;
    }

    private PhdIndividualProgramProcessState() {
	this(false);
    }

    public boolean isActive() {
	return activeState;
    }

    @Override
    public String getName() {
	return name();
    }

    @Override
    public String getLocalizedName() {
	return getLocalizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }

    public static List<PhdIndividualProgramProcessState> getPossibleNextStates(final PhdIndividualProgramProcess process) {
	PhdIndividualProgramProcessState activeState = process.getActiveState();

	if (activeState == null) {
	    return Collections.singletonList(CANDIDACY);
	}

	switch (activeState) {
	case CANDIDACY:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, NOT_ADMITTED, SUSPENDED, FLUNKED,
		    CANCELLED });
	case WORK_DEVELOPMENT:
	    if (process.hasThesisProcess()) {
		return Arrays.asList(new PhdIndividualProgramProcessState[] { THESIS_DISCUSSION, NOT_ADMITTED, SUSPENDED,
			FLUNKED, CANCELLED, TRANSFERRED });
	    } else {
		return Arrays.asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED,
			TRANSFERRED });
	    }
	case THESIS_DISCUSSION:
	    return Arrays
		    .asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED, CONCLUDED });
	case NOT_ADMITTED:
	case SUSPENDED:
	case FLUNKED:
	case CANCELLED:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { process.getLastActiveState().getType() });
	case CONCLUDED:
	    return Collections.emptyList();
	case TRANSFERRED:
	    return Collections.singletonList(WORK_DEVELOPMENT);
	default:
	    throw new DomainException("error.PhdIndividualProgramProcess.unknown.process.state.types");
	}
    }

    public static List<PhdIndividualProgramProcessState> getPossibleNextStates(final PhdIndividualProgramProcessState stateType) {
	if (stateType == null) {
	    return Collections.singletonList(CANDIDACY);
	}

	switch (stateType) {
	case CANDIDACY:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, NOT_ADMITTED, SUSPENDED, FLUNKED,
		    CANCELLED });
	case WORK_DEVELOPMENT:
		return Arrays.asList(new PhdIndividualProgramProcessState[] { THESIS_DISCUSSION, NOT_ADMITTED, SUSPENDED,
			FLUNKED, CANCELLED, TRANSFERRED });
	case THESIS_DISCUSSION:
	    return Arrays
		    .asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED, CONCLUDED });
	case SUSPENDED:
	case FLUNKED:
	    return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, THESIS_DISCUSSION });
	case NOT_ADMITTED:
	case CANCELLED:
	    return Collections.emptyList();
	case CONCLUDED:
	    return Collections.emptyList();
	case TRANSFERRED:
	    return Collections.emptyList();
	default:
	    throw new DomainException("error.PhdIndividualProgramProcess.unknown.process.state.types");
	}
    }
}
