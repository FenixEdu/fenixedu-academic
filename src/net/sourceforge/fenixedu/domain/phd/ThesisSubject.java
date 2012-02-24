package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSubject extends ThesisSubject_Base {

    public ThesisSubject() {
	super();
    }

    public ThesisSubject(MultiLanguageString name, MultiLanguageString description, Teacher teacher) {
	this();
	setName(name);
	setDescription(description);
	setTeacher(teacher);
    }

    @Override
    @Service
    public void removePhdProgramFocusArea() {
	super.removePhdProgramFocusArea();
	if (hasAnyThesisSubjectOrders()) {
	} else {
	    delete();
	}
    }

    private void delete() {
	removeRootDomainObject();
	removeTeacher();

	deleteDomainObject();
    }

    @Service
    public static ThesisSubject createThesisSubject(MultiLanguageString name, MultiLanguageString description, Teacher teacher) {
	return new ThesisSubject(name, description, teacher);
    }
}
