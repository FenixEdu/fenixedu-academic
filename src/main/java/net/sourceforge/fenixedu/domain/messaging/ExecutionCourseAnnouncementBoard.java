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
package net.sourceforge.fenixedu.domain.messaging;

import java.util.Comparator;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ContentManagementLog;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseBoardPermittedGroupType;
import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExecutionCourseAnnouncementBoard extends ExecutionCourseAnnouncementBoard_Base {

    public static final Comparator<ExecutionCourseAnnouncementBoard> COMPARE_BY_EXECUTION_PERIOD_AND_NAME =
            new Comparator<ExecutionCourseAnnouncementBoard>() {
                @Override
                public int compare(ExecutionCourseAnnouncementBoard o1, ExecutionCourseAnnouncementBoard o2) {
                    int result =
                            -o1.getExecutionCourse().getExecutionPeriod().compareTo(o2.getExecutionCourse().getExecutionPeriod());
                    if (result == 0) {
                        result = o1.getExecutionCourse().getNome().compareTo(o2.getExecutionCourse().getNome());
                    }
                    return (result == 0) ? o1.getExecutionCourse().getExternalId()
                            .compareTo(o2.getExecutionCourse().getExternalId()) : result;
                }
            };

    public ExecutionCourseAnnouncementBoard() {
        super();
    }

    public ExecutionCourseAnnouncementBoard(String name, ExecutionCourse executionCourse, Group writers, Group readers,
            Group managers, ExecutionCourseBoardPermittedGroupType writersGroupType,
            ExecutionCourseBoardPermittedGroupType readersGroupType, ExecutionCourseBoardPermittedGroupType managersGroupType) {

        this();
        init(name, executionCourse, writers, readers, managers, writersGroupType, readersGroupType, managersGroupType);
    }

    private void init(String name, ExecutionCourse executionCourse, Group writers, Group readers, Group managers,
            ExecutionCourseBoardPermittedGroupType writersGroupType, ExecutionCourseBoardPermittedGroupType readersGroupType,
            ExecutionCourseBoardPermittedGroupType managersGroupType) {

        checkParameters(name, executionCourse);

        setName(new MultiLanguageString(name.replace('?', ' ').replace('/', ' ')));
        setExecutionCourse(executionCourse);
        setMandatory(Boolean.FALSE);

        setWriters(writers);
        setReaders(readers);
        setManagers(managers);

        setExecutionCoursePermittedWriteGroupType(writersGroupType);
        setExecutionCoursePermittedReadGroupType(readersGroupType);
        setExecutionCoursePermittedManagementGroupType(managersGroupType);
    }

    private void checkParameters(String name, ExecutionCourse executionCourse) {
        if (name == null) {
            throw new DomainException("error.messaging.ExecutionCourseAnnouncementBoard.name.cannot.be.null");
        }
        if (executionCourse == null) {
            throw new DomainException("error.messaging.ExecutionCourseAnnouncementBoard.executionCourse.cannot.be.null");
        }
    }

    @Override
    public String getFullName() {
        final StringBuilder result = new StringBuilder(20);
        result.append(getExecutionCourse().getNome()).append(" ");
        result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("ºSem. ");
        result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return result.toString();
    }

    @Override
    public String getQualifiedName() {
        final StringBuilder result = new StringBuilder(20);
        result.append(getExecutionCourse().getNome()).append(" ");
        result.append(getExecutionCourse().getExecutionPeriod().getSemester()).append("ºSem. ");
        result.append(getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear()).append(" ");
        final Iterator<Degree> degrees = getExecutionCourse().getDegreesSortedByDegreeName().iterator();
        while (degrees.hasNext()) {
            result.append(degrees.next().getSigla()).append(degrees.hasNext() ? "," : "");
        }
        return result.toString();
    }

    @Override
    public void delete() {
        setExecutionCourse(null);
        super.delete();
    }

    @Override
    public boolean isCurrentUserApprover() {
        return false;
    }

    @Override
    public boolean hasApprover(Person person) {
        return false;
    }

    @Override
    public boolean isPublicToApprove() {
        return false;
    }

    @Override
    public Boolean getInitialAnnouncementsApprovedState() {
        return true;
    }

    @Override
    public Site getSite() {
        return getExecutionCourse().getSite();
    }

    @Override
    public String getSiteParamForAnnouncementBoard(Announcement announcement) {
        String base = super.getSiteParamForAnnouncementBoard(announcement);

        StringBuffer actionPath = new StringBuffer();
        ExecutionCourse executionCourse = this.getExecutionCourse();

        actionPath.append("&executionCourseID=" + executionCourse.getExternalId());
        return base + actionPath.toString();
    }

    @Override
    public void logCreate(Announcement announcement) {
        ContentManagementLog.createLog(this.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.announcement.added", announcement.getName().getContent(), this.getExecutionCourse()
                        .getNome(), this.getExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logEdit(Announcement announcement) {
        ContentManagementLog.createLog(this.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.announcement.edited", announcement.getName().getContent(), this.getExecutionCourse()
                        .getNome(), this.getExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logRemove(Announcement announcement) {
        ContentManagementLog.createLog(this.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.announcement.removed", announcement.getName().getContent(), this
                        .getExecutionCourse().getNome(), this.getExecutionCourse().getDegreePresentationString());
    }

    @Override
    public void logAddFile(FileContent attachment) {
        ContentManagementLog.createLog(getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.file.added", attachment.getDisplayName(), getExecutionCourse().getNome(),
                getExecutionCourse().getDegreePresentationString());
    }

    @Deprecated
    public boolean hasExecutionCoursePermittedManagementGroupType() {
        return getExecutionCoursePermittedManagementGroupType() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasExecutionCoursePermittedWriteGroupType() {
        return getExecutionCoursePermittedWriteGroupType() != null;
    }

    @Deprecated
    public boolean hasExecutionCoursePermittedReadGroupType() {
        return getExecutionCoursePermittedReadGroupType() != null;
    }

}
