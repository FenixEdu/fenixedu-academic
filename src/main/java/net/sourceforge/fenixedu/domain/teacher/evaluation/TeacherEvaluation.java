package net.sourceforge.fenixedu.domain.teacher.evaluation;

import pt.ist.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.EmailAddressList;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

public abstract class TeacherEvaluation extends TeacherEvaluation_Base {

    public TeacherEvaluation() {
        super();
        setCreatedDate(new DateTime());
        setRootDomainObject(Bennu.getInstance());
    }

    public TeacherEvaluationState getState() {
        if (getTeacherEvaluationProcess().getFacultyEvaluationProcess().getAutoEvaluationInterval().getStart().isAfterNow()) {
            return null;
        } else if (getAutoEvaluationLock() == null) {
            return TeacherEvaluationState.AUTO_EVALUATION;
        } else if (getEvaluationLock() == null) {
            return TeacherEvaluationState.EVALUATION;
        } else {
            return TeacherEvaluationState.EVALUATED;
        }
    }

    public abstract TeacherEvaluationType getType();

    public abstract Set<TeacherEvaluationFileType> getAutoEvaluationFileSet();

    public abstract Set<TeacherEvaluationFileType> getEvaluationFileSet();

    public abstract String getFilenameTypePrefix();

    public TeacherEvaluationMark getApprovedEvaluationMark() {
        return getTeacherEvaluationProcess().getApprovedEvaluationMark();
    }

    public void setApprovedEvaluationMark(TeacherEvaluationMark mark) {
        getTeacherEvaluationProcess().setApprovedEvaluationMark(mark);
    }

    @Atomic
    public void lickAutoEvaluationStamp() {
        internalLickingBusiness();
    }

    protected void internalLickingBusiness() {
        setAutoEvaluationLock(new DateTime());
        final Person who = AccessControl.getPerson();
        setUserWhoLockedAutoEvaluation(who == null ? null : who.getUser());
    }

    @Atomic
    public void lickEvaluationStamp() {
        setEvaluationLock(new DateTime());
        final Person who = AccessControl.getPerson();
        setUserWhoLockedEvaluation(who == null ? null : who.getUser());

        final TeacherEvaluationProcess teacherEvaluationProcess = getTeacherEvaluationProcess();
        final Person evaluee = teacherEvaluationProcess.getEvaluee();
        if (evaluee != AccessControl.getPerson()) {
            final Person evaluator = teacherEvaluationProcess.getEvaluator();
            final Recipient recipient = new Recipient(Collections.singletonList(evaluee));
            final Recipient ccRecipient = new Recipient(Collections.singletonList(evaluator));
            final FacultyEvaluationProcess facultyEvaluationProcess = teacherEvaluationProcess.getFacultyEvaluationProcess();
            final String title = facultyEvaluationProcess.getTitle().getContent();
            final String body =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
                            "message.email.stamp.teacher.evaluation.process", title);
            final SystemSender systemSender = Bennu.getInstance().getSystemSender();
            final Message message =
                    new Message(systemSender, Collections.EMPTY_LIST, Collections.EMPTY_LIST, title, body, new EmailAddressList(
                            Collections.EMPTY_LIST).toString());
            message.addTos(recipient);
            message.addCcs(ccRecipient);
        }
    }

    public void delete() {
        setTeacherEvaluationProcess(null);
        for (final TeacherEvaluationFile teacherEvaluationFile : getTeacherEvaluationFileSet()) {
            teacherEvaluationFile.delete();
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Atomic
    public void rubAutoEvaluationStamp() {
        setAutoEvaluationLock(null);
    }

    @Atomic
    public void rubEvaluationStamp() {
        setEvaluationLock(null);
    }

    public abstract void copyAutoEvaluation();

    protected void internalCopyAutoEvaluation(TeacherEvaluation copy) {
        copy.setAutoEvaluationLock(getAutoEvaluationLock());
        for (TeacherEvaluationFile file : getTeacherEvaluationFileSet()) {
            if (file.getTeacherEvaluationFileType().isAutoEvaluationFile()) {
                file.copy(copy);
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFile> getTeacherEvaluationFile() {
        return getTeacherEvaluationFileSet();
    }

    @Deprecated
    public boolean hasAnyTeacherEvaluationFile() {
        return !getTeacherEvaluationFileSet().isEmpty();
    }

    @Deprecated
    public boolean hasAutoEvaluationLock() {
        return getAutoEvaluationLock() != null;
    }

    @Deprecated
    public boolean hasUserWhoLockedEvaluation() {
        return getUserWhoLockedEvaluation() != null;
    }

    @Deprecated
    public boolean hasTeacherEvaluationProcess() {
        return getTeacherEvaluationProcess() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEvaluationMark() {
        return getEvaluationMark() != null;
    }

    @Deprecated
    public boolean hasUserWhoLockedAutoEvaluation() {
        return getUserWhoLockedAutoEvaluation() != null;
    }

    @Deprecated
    public boolean hasAutoEvaluationMark() {
        return getAutoEvaluationMark() != null;
    }

    @Deprecated
    public boolean hasCreatedDate() {
        return getCreatedDate() != null;
    }

    @Deprecated
    public boolean hasEvaluationLock() {
        return getEvaluationLock() != null;
    }

}
