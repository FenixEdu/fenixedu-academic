package net.sourceforge.fenixedu.domain.teacher;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixframework.Atomic;

public class TeacherServiceComment extends TeacherServiceComment_Base {

    public TeacherServiceComment(TeacherService teacherService, String content) {
        super();
        super.setContent(content);
        setLastModifiedDate(new DateTime());
        setTeacherService(teacherService);
        setCreationDate(new DateTime());
        setCreatedBy(((User) Authenticate.getUser()).getPerson());
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
        return ((User) Authenticate.getUser()).getPerson().equals(getCreatedBy());
    }

    @Atomic
    @Override
    public void delete() {
        new TeacherServiceLog(getTeacherService(), BundleUtil.getStringFromResourceBundle(
                "resources.TeacherCreditsSheetResources", "label.teacher.teacherServiceComment.delete", getContent(),
                getCreationDate().toString(), getLastModifiedDate().toString("yyyy-MM-dd HH:mm")));
        setCreatedBy(null);
        super.delete();
    }
    @Deprecated
    public boolean hasCreatedBy() {
        return getCreatedBy() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasContent() {
        return getContent() != null;
    }

}
