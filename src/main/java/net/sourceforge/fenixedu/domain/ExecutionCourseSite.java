/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.util.Formatter;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentSharingDegreeOfCompetenceOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentSharingDegreeOfExecutionCourseGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseSite extends ExecutionCourseSite_Base {

    protected ExecutionCourseSite() {
        super();

        setDynamicMailDistribution(false);
        setLessonPlanningAvailable(false);
    }

    public ExecutionCourseSite(ExecutionCourse course) {
        this();

        course.addForum(new ExecutionCourseForum(new MultiLanguageString().with(MultiLanguageString.pt, course.getNome().replace('?', ' ')
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

        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
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
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = super.getContextualPermissionGroups();

        ExecutionCourse executionCourse = getSiteExecutionCourse();

        groups.add(TeacherGroup.get(executionCourse));
        groups.add(TeacherGroup.get(executionCourse).or(StudentGroup.get(executionCourse)));
        groups.add(StudentSharingDegreeOfExecutionCourseGroup.get(executionCourse));
        groups.add(StudentSharingDegreeOfCompetenceOfExecutionCourseGroup.get(executionCourse));

        return groups;
    }

    @Override
    public Group getOwner() {
        return TeacherGroup.get(getSiteExecutionCourse());
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
                MultiLanguageString.pt,
                new Formatter().format("%s/%s/%d-semestre", getSiteExecutionCourse().getSigla(),
                        executionSemester.getExecutionYear().getYear().replace('/', '-'), executionSemester.getSemester())
                        .toString());
    }

    @Override
    public void logCreateSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.created", section.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());

    }

    @Override
    public void logEditSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.edited", section.getName().getContent(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logRemoveSection(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.removed", section.getName().getContent(),
                getSiteExecutionCourse().getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logRemoveFile(FileContent attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.file.removed", attachment.getDisplayName(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logEditFile(FileContent attachment) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.file.edited", attachment.getDisplayName(), getSiteExecutionCourse().getNome(),
                getSiteExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logCreateItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                        "log.executionCourse.content.section.item.created", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logEditItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                        "log.executionCourse.content.section.item.edited", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    public void logDeleteItemtoSection(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                        "log.executionCourse.content.section.item.deleted", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

    @Override
    public void logEditItemPermission(Item item) {
        ContentManagementLog
                .createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                        "log.executionCourse.content.section.item.permitted", item.getName().getContent(), item.getSection()
                                .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                                .getDegreePresentationString());
    }

//    public void logSectionInsertInstitutional(Content childContent, Section section) {
//        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
//                "log.executionCourse.content.section.item.insert.institutional", childContent.getName().getContent(), section
//                        .getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
//                        .getDegreePresentationString());
//    }

    @Override
    public void logEditSectionPermission(Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.permitted", section.getName().getContent(), getSiteExecutionCourse()
                        .getNome(), getSiteExecutionCourse().getDegreePresentationString());
    }

//    public void logAddFileToItem(Item item, Section section, Content childContent) {
//        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
//                "log.executionCourse.content.section.item.add.file", childContent.getName().getContent(), item.getName()
//                        .getContent(), section.getName().getContent(), getSiteExecutionCourse().getNome(),
//                getSiteExecutionCourse().getDegreePresentationString());
//    }

    public void logEditFileToItem(FileContent attachment, Section section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.item.edit.file", attachment.getDisplayName(),
                section.getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                        .getDegreePresentationString());
    }

    @Override
    public void logItemFilePermittedGroup(FileContent attachment, CmsContent section) {
        ContentManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.section.file.permitted", attachment.getDisplayName(),
                section.getName().getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
                        .getDegreePresentationString());
    }

//    public void logSectionInsertFile(Content childContent, Section section) {
//        ContentManagementLog
//                .createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
//                        "log.executionCourse.content.section.file.insert", childContent.getName().getContent(), section.getName()
//                                .getContent(), getSiteExecutionCourse().getNome(), getSiteExecutionCourse()
//                                .getDegreePresentationString());
//    }

    @Override
    public void setLessonPlanningAvailable(Boolean lessonPlanningAvailable) {
        if (getSiteExecutionCourse() != null) {
            final String avaiable;

            if (lessonPlanningAvailable) {
                avaiable = BundleUtil.getString(Bundle.APPLICATION, "message.available");
            } else {
                avaiable = BundleUtil.getString(Bundle.APPLICATION, "message.not.available");
            }

            CurricularManagementLog.createLog(getSiteExecutionCourse(), Bundle.MESSAGING,
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
