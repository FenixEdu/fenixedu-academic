package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSubject extends ThesisSubject_Base {

    protected ThesisSubject() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected ThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
	    Teacher teacher, String externalAdvisor) {
	this();

	checkParameters(focusArea, name, description, teacher);

	setPhdProgramFocusArea(focusArea);
	setName(name);
	setDescription(description);
	setTeacher(teacher);
	setExternalAdvisorName(externalAdvisor);
    }

    private void checkParameters(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
	    Teacher teacher) {
	check(focusArea, "error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.focusArea.required");

	if (name == null) {
	    check(name, "error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.required");
	}

	if (!name.hasContent(Language.en)) {
	    throw new PhdDomainOperationException(
		    "error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.in.english.required");
	}

	if (teacher == null) {
	    throw new PhdDomainOperationException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.teacher.required");
	}
    }

    @Service
    public void edit(MultiLanguageString name, MultiLanguageString description, Teacher teacher, String externalAdvisor) {
	checkParameters(getPhdProgramFocusArea(), name, description, teacher);

	setName(name);
	setDescription(description);
	setTeacher(teacher);
	setExternalAdvisorName(externalAdvisor);
    }

    @Service
    public void delete() {
	if (hasAnyThesisSubjectOrders()) {
	    throw new PhdDomainOperationException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.has.subject.orders");
	}

	removeRootDomainObject();
	removeTeacher();
	removePhdProgramFocusArea();
    }

    @Service
    public static ThesisSubject createThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name,
	    MultiLanguageString description, Teacher teacher, String externalAdvisor) {
	return new ThesisSubject(focusArea, name, description, teacher, externalAdvisor);
    }
}
