package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;

public class TeacherServiceComment extends TeacherServiceComment_Base {

    public TeacherServiceComment(TeacherService teacherService, String content) {
	super();
	super.setContent(content);
	setLastModifiedDate(new DateTime());
	setTeacherService(teacherService);
	setCreationDate(new DateTime());
	setCreatedBy(((IUserView) UserView.getUser()).getPerson());
	new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
		"resources.TeacherCreditsSheetResources", "label.teacher.teacherServiceComment.create", content,
		getCreationDate().toString("yyyy-MM-dd HH:mm")));
    }

    @Override
    public void setContent(String content) {
	super.setContent(content);
	setLastModifiedDate(new DateTime());
	new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
		"resources.TeacherCreditsSheetResources", "label.teacher.teacherServiceComment.edit", content, getCreationDate()
			.toString("yyyy-MM-dd HH:mm"), getLastModifiedDate().toString("yyyy-MM-dd HH:mm")));
    }

    public boolean getCanEdit() {
	return ((IUserView) UserView.getUser()).getPerson().equals(getCreatedBy());
    }

    @Service
    @Override
    public void delete() {
	new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
		"resources.TeacherCreditsSheetResources", "label.teacher.teacherServiceComment.delete", getContent(),
		getCreationDate().toString(), getLastModifiedDate().toString("yyyy-MM-dd HH:mm")));
	removeCreatedBy();
	super.delete();
    }
}
