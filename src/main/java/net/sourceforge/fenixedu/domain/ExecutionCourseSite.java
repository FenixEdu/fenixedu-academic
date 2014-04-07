package net.sourceforge.fenixedu.domain;

import java.util.Formatter;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CompetenceCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.DegreesOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersAndStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.FenixFramework;
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

        course.addForum(new ExecutionCourseForum(new MultiLanguageString().with(Language.pt, course.getNome().replace('?', ' ')
                .replace('/', ' ')), new MultiLanguageString("")));
        setSiteExecutionCourse(course);
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
    public void delete() {
        setSiteExecutionCourse(null);
        super.delete();
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

    public static ExecutionCourseSite readExecutionCourseSiteByOID(String oid) {
        Site site = (Site) FenixFramework.getDomainObject(oid);
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

    @Deprecated
    public ExecutionCourse getExecutionCourse() {
        return super.getSiteExecutionCourse();
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
    public void logRemoveFile(FileContent attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.file.removed", attachment.getDisplayName(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logEditFile(FileContent attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.file.edited", attachment.getDisplayName(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
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

//    public void logSectionInsertInstitutional(Content childContent, Section section) {
//        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
//                "log.executionCourse.content.section.item.insert.institutional", childContent.getName().getContent(), section
//                        .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
//                        .getDegreePresentationString());
//    }

    @Override
    public void logEditSectionPermission(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.permitted", section.getName().getContent(), getSiteExecutionCourse()
                        .getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

//    public void logAddFileToItem(Item item, Section section, Content childContent) {
//        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
//                "log.executionCourse.content.section.item.add.file", childContent.getName().getContent(), item.getName()
//                        .getContent(), section.getName().getContent(), getSiteExecutionCourse().getNome(),
//                getSiteExecutionCourse().getDegreePresentationString());
//    }

    public void logEditFileToItem(FileContent attachment, Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.item.edit.file", attachment.getDisplayName(),
                section.getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                        .getDegreePresentationString());
    }

    @Override
    public void logItemFilePermittedGroup(FileContent attachment, CmsContent section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), "resources.MessagingResources",
                "log.executionCourse.content.section.file.permitted", attachment.getDisplayName(),
                section.getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                        .getDegreePresentationString());
    }

//    public void logSectionInsertFile(Content childContent, Section section) {
//        ContentManagementLog
//                .createLog(getSiteExecutionCourse(), "resources.MessagingResources",
//                        "log.executionCourse.content.section.file.insert", childContent.getName().getContent(), section.getName()
//                                .getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
//                                .getDegreePresentationString());
//    }

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

    @Override
    public String getReversePath() {
        final ExecutionSemester executionSemester = getSiteExecutionCourse().getExecutionPeriod();
        return super.getReversePath()
                + String.format("/%s/%s/%d-semestre", getSiteExecutionCourse().getSigla(), executionSemester.getExecutionYear()
                        .getYear().replace('/', '-'), executionSemester.getSemester());
    }

    @Deprecated
    public boolean hasIntroduction() {
        return getIntroduction() != null;
    }

    @Deprecated
    public boolean hasSiteExecutionCourse() {
        return getSiteExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasLessonPlanningAvailable() {
        return getLessonPlanningAvailable() != null;
    }

    @Deprecated
    public boolean hasMail() {
        return getMail() != null;
    }

    @Deprecated
    public boolean hasDynamicMailDistribution() {
        return getDynamicMailDistribution() != null;
    }

    @Deprecated
    public boolean hasInitialStatement() {
        return getInitialStatement() != null;
    }

}
