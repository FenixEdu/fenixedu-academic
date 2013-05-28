package net.sourceforge.fenixedu.domain;

import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CompetenceCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreesOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.contents.Attachment;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseSite extends ExecutionCourseSite_Base {

    protected ExecutionCourseSite() {
        super();

        setDynamicMailDistribution(false);
        setLessonPlanningAvailable(false);
    }

    public ExecutionCourseSite(ExecutionCourse course) {
        this();

        setSiteExecutionCourse(course);
        createForum(new MultiLanguageString().with(Language.pt, course.getNome().replace('?', ' ').replace('/', ' ')),
                new MultiLanguageString(""));
    }

    public void edit(final String initialStatement, final String introduction, final String mail, final String alternativeSite) {

        setInitialStatement(initialStatement);
        setIntroduction(introduction);
        setMail(mail);
        setAlternativeSite(alternativeSite);
    }

    public void copyCustomizationOptionsFrom(ExecutionCourseSite siteFrom) {
        setMail(siteFrom.getMail());
        setAlternativeSite(siteFrom.getAlternativeSite());
        setInitialStatement(siteFrom.getInitialStatement());
        setIntroduction(siteFrom.getIntroduction());

        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.customization.options.import", siteFrom.getSiteExecutionCourse().getName(), siteFrom
                        .getSiteExecutionCourse().getDegreePresentationString(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    protected void disconnect() {
        removeSiteExecutionCourse();
        super.disconnect();
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();

        ExecutionCourse executionCourse = getSiteExecutionCourse();

        groups.add(new ExecutionCourseTeachersGroup(executionCourse));
        groups.add(new ExecutionCourseTeachersAndStudentsGroup(executionCourse));
        groups.add(new DegreesOfExecutionCourseGroup(executionCourse));
        groups.add(new CompetenceCourseGroup(executionCourse));

        return groups;
    }

    @Override
    public IGroup getOwner() {
        return new ExecutionCourseTeachersGroup(getSiteExecutionCourse());

    }

    @Override
    public String getAuthorName() {
        return getSiteExecutionCourse().getNome();
    }

    @Override
    public ExecutionSemester getExecutionPeriod() {
        return getSiteExecutionCourse().getExecutionPeriod();
    }

    public static ExecutionCourseSite readExecutionCourseSiteByOID(Integer oid) {
        Site site = (Site) AbstractDomainObject.fromExternalId(ExecutionCourseSite.class, oid);
        if (site == null) {
            return null;
        }

        if (site instanceof ExecutionCourseSite) {
            return (ExecutionCourseSite) site;
        } else {
            return null;
        }
    }

    @Override
    public boolean isFileClassificationSupported() {
        return true;
    }

    @Override
    public boolean isScormContentAccepted() {
        return true;
    }

    @Deprecated
    public ExecutionCourse getExecutionCourse() {
        return super.getSiteExecutionCourse();
    }

    public Collection<ExecutionCourseForum> getForuns() {
        return getChildren(ExecutionCourseForum.class);
    }

    public void addForum(ExecutionCourseForum executionCourseForum) {
        checkIfCanAddForum(executionCourseForum.getNormalizedName());
        addChild(executionCourseForum);
    }

    public void removeForum(ExecutionCourseForum executionCourseForum) {
        removeChild(executionCourseForum);
    }

    public void checkIfCanAddForum(MultiLanguageString name) {
        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
    }

    public boolean hasForumWithName(MultiLanguageString name) {
        return getForumByName(name) != null;
    }

    public ExecutionCourseForum getForumByName(MultiLanguageString name) {
        for (final ExecutionCourseForum executionCourseForum : getForuns()) {
            if (executionCourseForum.getNormalizedName().equalInAnyLanguage(name)) {
                return executionCourseForum;
            }
        }

        return null;
    }

    public void createForum(MultiLanguageString name, MultiLanguageString description) {

        if (hasForumWithName(name)) {
            throw new DomainException("executionCourse.already.existing.forum");
        }
        addForum(new ExecutionCourseForum(name, description));
    }

    @Override
    public MultiLanguageString getName() {
        final ExecutionSemester executionSemester = getSiteExecutionCourse().getExecutionPeriod();
        return new MultiLanguageString().with(
                Language.pt,
                new Formatter().format("%s/%s/%d-semestre", getSiteExecutionCourse().getSigla(),
                        executionSemester.getExecutionYear().getYear().replace('/', '-'), executionSemester.getSemester())
                        .toString());
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
        // unable to optimize because we cannot track changes to name correctly.
        // don't call super.setNormalizedName() !
    }

    @Override
    public void logCreateSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.created", section.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());

    }

    @Override
    public void logEditSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.edited", section.getName().getContent(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logRemoveSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.removed", section.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logRemoveFile(Attachment attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.file.removed", attachment.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logEditFile(Attachment attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.file.edited", attachment.getName().getContent(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    protected Node createChildNode(Content childContent) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.institutional", childContent.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());
        return super.createChildNode(childContent);
    }

    @Override
    public void logRemoveFunctionalityCall(FunctionalityCall functionalityCall) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.remove.institutional", functionalityCall.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logCreateItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.item.created", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logEditItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.item.edited", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logDeleteItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.item.deleted", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logEditItemPermission(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.item.permitted", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logSectionInsertInstitutional(Content childContent, Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.item.insert.institutional", childContent.getName().getContent(), section
                        .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                        .getDegreePresentationString());
    }

    @Override
    public void logEditSectionPermission(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.permitted", section.getName().getContent(), getSiteExecutionCourse()
                        .getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logAddFileToItem(Item item, Section section, Content childContent) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.item.add.file", childContent.getName().getContent(), item.getName()
                        .getContent(), section.getName().getContent(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logEditFileToItem(Attachment attachment, Section section) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.item.edit.file", attachment.getName().getContent(), section
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logItemFilePermittedGroup(Attachment attachment, Section section) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.file.permitted", attachment.getName().getContent(), section
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logSectionInsertFile(Content childContent, Section section) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                        "log.executionCourse.content.section.file.insert", childContent.getName().getContent(), section.getName()
                                .getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void setLessonPlanningAvailable(Boolean lessonPlanningAvailable) {

        if (getSiteExecutionCourse() != null) {
            final String avaiable;

            if (lessonPlanningAvailable) {
                avaiable = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "message.available");
            } else {
                avaiable = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "message.not.available");
            }

            CurricularManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                    "log.executionCourse.curricular.planning.publish", avaiable, getSiteExecutionCourse().getNome(),
                    getSiteExecutionCourse().getDegreePresentationString());
            super.setLessonPlanningAvailable(lessonPlanningAvailable);
        }
    }
}
