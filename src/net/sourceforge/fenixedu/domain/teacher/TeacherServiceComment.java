package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public class TeacherServiceComment extends TeacherServiceComment_Base {

    public TeacherServiceComment(TeacherService teacherService, String content) {
	super();
	setContent(content);
	setTeacherService(teacherService);
	setCreationDate(new DateTime());
	setCreatedBy(((IUserView) UserView.getUser()).getPerson());
    }

    @Override
    public void setContent(String content) {
	super.setContent(content);
	setLastModifiedDate(new DateTime());
    }

    public boolean getCanEdit() {
	return ((IUserView) UserView.getUser()).getPerson().equals(getCreatedBy());
    }

    @Service
    @Override
    public void delete() {
	removeCreatedBy();
	removeTeacherService();
	removeRootDomainObject();
	super.delete();
    }
}
