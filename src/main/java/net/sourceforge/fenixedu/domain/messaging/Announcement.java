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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ContentManagementLog;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.predicates.AnnouncementPredicates;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Announcement extends Announcement_Base {

    public static final Comparator<Announcement> NEWEST_FIRST = new Comparator<Announcement>() {
        @Override
        public int compare(Announcement o1, Announcement o2) {
            int result = o2.getBeginComparationDate().compareTo(o1.getBeginComparationDate());
            return result != 0 ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    public static final Comparator<Announcement> SUBJECT_BEGIN_DATE = new Comparator<Announcement>() {

        @Override
        public int compare(Announcement o1, Announcement o2) {
            DateTime referedSubjectBegin = o1.getReferedSubjectBegin();
            DateTime referedSubjectBegin2 = o2.getReferedSubjectBegin();
            if (referedSubjectBegin != null && referedSubjectBegin2 != null) {
                return referedSubjectBegin.compareTo(referedSubjectBegin2);
            } else {
                if (referedSubjectBegin == null && referedSubjectBegin2 == null) {
                    return 0;
                } else {
                    return referedSubjectBegin == null ? -1 : 1;
                }
            }

        }

    };

    public static final Comparator<Announcement> PRIORITY_FIRST = new Comparator<Announcement>() {
        @Override
        public int compare(Announcement o1, Announcement o2) {
            int priority1 = (o1.getPriority() != null ? o1.getPriority() : 0);
            int priority2 = (o2.getPriority() != null ? o2.getPriority() : 0);
            int result = priority1 - priority2;
            if (result < -1) {
                result = -1;
            } else if (result > 1) {
                result = 1;
            }

            return result != 0 ? result : o1.getExternalId().compareTo(o2.getExternalId());
        }
    };

    private DateTime getBeginComparationDate() {
        final DateTime publicationBegin = getPublicationBegin();
        return publicationBegin != null ? publicationBegin : getCreationDate();
    }

    public Announcement() {
        super();
        super.setCreationDate(new DateTime());
        super.setLastModification(new DateTime());
        super.setApproved(false);
    }

    @Override
    public void setPlace(String place) {
        super.setPlace(place);
        this.updateLastModification();
    }

    @Override
    public void setAuthor(String author) {
        super.setAuthor(author);
        this.updateLastModification();
    }

    @Override
    public void setAuthorEmail(String authorEmail) {
        super.setAuthorEmail(authorEmail);
        this.updateLastModification();
    }

    @Override
    public void setBody(MultiLanguageString body) {
        super.setBody(body);
        this.updateLastModification();
    }

    @Override
    public void setExcerpt(MultiLanguageString excerpt) {
        super.setExcerpt(excerpt);
        this.updateLastModification();
    }

    @Override
    public void setVisible(Boolean visible) {
        super.setVisible(visible);
        this.updateLastModification();
    }

    @Override
    public void setApproved(Boolean aproved) {
        check(this, AnnouncementPredicates.approvePredicate);
        super.setApproved(aproved);
        this.updateLastModification();
    }

    private void updateLastModification() {
        super.setLastModification(new DateTime());
    }

    public boolean getOnline() {
        return ((this.getPublicationEnd() == null || this.getPublicationEnd().isAfterNow()) && getVisible());
    }

    public boolean isExcerptEmpty() {
        return this.getExcerpt() == null || this.getExcerpt().getContent() == null || this.getExcerpt().getContent().equals("");
    }

    public String getBodyDigest() {
        final int numberOfWords = 30;
        StringBuilder buffer = new StringBuilder();
        if (this.getBody() != null && this.getBody().getContent() != null) {
            String[] words = this.getBody().getContent().split(" ");
            for (int i = 0; i < words.length && i <= numberOfWords; i++) {
                buffer.append(words[i]).append(" ");
            }
            if (words.length > numberOfWords) {
                buffer.append("...");
            }
        }

        return buffer.toString();

    }

    public String getShortBody() {
        return this.isExcerptEmpty() ? this.getBodyDigest() : this.getExcerpt().getContent();
    }

    public boolean wasModifiedSinceCreation() {
        return this.getCreationDate().getMillis() != this.getLastModification().getMillis();
    }

    public boolean isOriginalVersion() {
        return !wasModifiedSinceCreation();
    }

    public boolean isActive() {
        return this.isActiveIn(new DateTime());
    }

    public boolean isActiveIn(DateTime time) {
        boolean result = true;
        result = result && (this.getCreationDate().isBefore(time));
        result = result && (this.getPublicationEnd() == null || this.getPublicationEnd().isAfter(time));
        result = result && (this.getPublicationBegin() == null || this.getPublicationBegin().isBefore(time));

        return result;
    }

    // public boolean isActiveIn(int month, int year) {
    // boolean result = true;
    // result = result
    // && (this.getCreationDate().getYear() < year ||
    // (this.getCreationDate().getYear() == year && this
    // .getCreationDate().getMonthOfYear() <= month));
    // result = result
    // && (this.getPublicationEnd() == null ||
    // this.getPublicationEnd().getYear() > year || (this
    // .getPublicationEnd().getYear() == year && this.getPublicationEnd()
    // .getMonthOfYear() >= month));
    // result = result
    // && (this.getPublicationBegin() == null ||
    // this.getPublicationBegin().getYear() < year || (this
    // .getPublicationBegin().getYear() == year &&
    // this.getPublicationBegin()
    // .getMonthOfYear() <= month));
    //
    // return result;
    // }

    public boolean isActiveIn(int year, int month) {
        if (hasPublicationInterval()) {
            return checkPublicationDate(year, month);
        } else {
            return checkCreationDate(year, month);
        }
    }

    private boolean hasPublicationInterval() {
        return hasPublicationBegin() || hasPublicationEnd();
    }

    private boolean hasPublicationBegin() {
        return getPublicationBegin() != null;
    }

    private boolean hasPublicationEnd() {
        return getPublicationEnd() != null;
    }

    private boolean checkCreationDate(int year, int month) {
        return getCreationDate().getYear() == year && getCreationDate().getMonthOfYear() == month;
    }

    private boolean checkPublicationDate(int year, int month) {
        if (!hasPublicationBegin() && !hasPublicationEnd()) {
            return true;
        }

        int endYear = year;
        int endMonth = month + 1;

        if (month == 12) {
            endYear = year + 1;
            endMonth = 1;
        }

        DateTime beginDateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
        DateTime endDateTime = new DateTime(endYear, endMonth, 1, 0, 0, 0, 0).minusSeconds(1);

        boolean result = true;
        if (hasPublicationBegin()) {
            result = result && !endDateTime.isBefore(getPublicationBegin());
        }

        if (hasPublicationEnd()) {
            result = result && !beginDateTime.isAfter(getPublicationEnd());
        }

        return result;
    }

    public boolean hasCreationDateFor(int year, int monthOfYear) {
        return getCreationDate().getYear() == year && getCreationDate().getMonthOfYear() == monthOfYear;
    }

    @Override
    public void setAnnouncementBoard(final AnnouncementBoard announcementBoard) {
        if (announcementBoard != null) {
            if (getAnnouncementBoard() != null) {
                announcementBoard.logEdit(this);
            } else {
                announcementBoard.logCreate(this);
            }
            super.setApproved(announcementBoard.getInitialAnnouncementsApprovedState());
        }
        super.setAnnouncementBoard(announcementBoard);
    }

    public MultiLanguageString getName() {
        return getSubject();
    }

    public boolean isCanApproveUser() {
        return !getApproved() && getAnnouncementBoard().isCurrentUserApprover();
    }

    public boolean isCanNotApproveUser() {
        return getApproved() && getAnnouncementBoard().isCurrentUserApprover();
    }

    public void setCategories(java.util.List<AnnouncementCategory> categories) {
        this.getCategories().clear();

        for (AnnouncementCategory category : categories) {
            this.addCategories(category);
        }
    }

    @Atomic
    public static Announcement createAnnouncement(AnnouncementBoard board, String authorName, String authorEmail,
            MultiLanguageString body, Space campus, List<AnnouncementCategory> categories, String place,
            DateTime publicationBeginDate, DateTime publicationEndDate, DateTime referedSubjectBeginDate,
            DateTime referedSubjectEndDate, MultiLanguageString subject, MultiLanguageString excerpt, Boolean isVisible,
            String editorNotes, String fileName, byte[] fileContents) {
        Announcement announcement = new Announcement();

        announcement.setAnnouncementBoard(board);
        announcement.setAuthor(authorName);
        announcement.setAuthorEmail(authorEmail);
        announcement.setBody(body);
        announcement.setCampus(campus);
        announcement.setCategories(categories);
        announcement.setCreationDate(new DateTime());
        announcement.setCreator(null);
        announcement.setExcerpt(excerpt);
        announcement.setPhotoUrl(null);
        announcement.setPlace(place);
        announcement.setPublicationBegin(publicationBeginDate);
        announcement.setPublicationEnd(publicationEndDate);
        announcement.setReferedSubjectBegin(referedSubjectBeginDate);
        announcement.setReferedSubjectEnd(referedSubjectEndDate);
        announcement.setSubject(subject);
        announcement.setVisible(isVisible);
        announcement.setEditorNotes(editorNotes);

        if (fileContents != null) {
            File file = board.addFileToBoard(fileName, fileContents, authorName);
            announcement.setPhotoUrl(file.getDownloadUrl());
        }

        return announcement;

    }

    public void delete() {
        AnnouncementBoard ab = getAnnouncementBoard();
        if (ab instanceof ExecutionCourseAnnouncementBoard) {
            ExecutionCourseAnnouncementBoard ecab = (ExecutionCourseAnnouncementBoard) ab;
            ContentManagementLog.createLog(ecab.getExecutionCourse(), Bundle.MESSAGING,
                    "log.executionCourse.content.announcement.removed", getName().getContent(), ecab.getExecutionCourse()
                            .getNome(), ecab.getExecutionCourse().getDegreePresentationString());
        }

        for (final AnnouncementCategory category : getCategories()) {
            removeCategories(category);
        }
        setCampus(null);
        setCreator(null);
        setAnnouncementBoard(null);
        deleteDomainObject();
    }

    public boolean isInPublicationPeriod() {
        DateTime beginPublication = getPublicationBegin();
        DateTime endPublication = getPublicationEnd();

        return (beginPublication == null || beginPublication.isBeforeNow())
                && (endPublication == null || endPublication.isAfterNow());
    }

    @Atomic
    public void swap(AnnouncementBoard source, AnnouncementBoard destination) {
        source.removeAnnouncement(this);
        setAnnouncementBoard(destination);
    }

    @Atomic
    public void updatePriority(Integer priority) {
        setPriority(priority);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkStickyAndPublicationBegin() {
        return (getSticky() == true && getPublicationBegin() != null) || getSticky() == false || getSticky() == null;
    }

    private Integer getMaxPriority() {
        Integer maxPriority = 0;
        for (Announcement announcement : this.getAnnouncementBoard().getAnnouncementSet()) {
            if (announcement.getSticky()) {
                if (!announcement.equals(this) && announcement.superGetPriority() > maxPriority) {
                    maxPriority = announcement.getPriority();
                }
            }
        }
        return maxPriority;
    }

    @Override
    @Atomic
    public void setPriority(Integer priority) {
        super.setPriority(priority);
    }

    private int ensureOrder(AnnouncementBoard board, int maxPriority) {
        if (board != null) {
            List<Announcement> stickyAnnouncements = new ArrayList<Announcement>();

            for (Announcement announcement : board.getAnnouncementSet()) {
                if (announcement.getSticky() != null && announcement.getSticky()) {
                    stickyAnnouncements.add(announcement);
                }

            }
            Collections.sort(stickyAnnouncements, new Comparator<Announcement>() {
                @Override
                public int compare(Announcement a1, Announcement a2) {
                    return (a1.getPriority() <= a2.getPriority() ? -1 : 1);
                };
            });
            int count = 1;
            for (Announcement announcement : stickyAnnouncements) {
                announcement.setPriority(count);
                count++;
            }
            return count;
        }
        return maxPriority;
    }

    @Override
    public Integer getPriority() {
        if (getSticky() && super.getPriority() == null) {
            Integer maxPriority = getMaxPriority() + 1;
            setPriority(maxPriority);
            return ensureOrder(this.getAnnouncementBoard(), maxPriority);
        } else {
            return superGetPriority();
        }
    }

    private Integer superGetPriority() {
        return (super.getPriority() == null ? -1 : super.getPriority());
    }

    @Override
    public Boolean getSticky() {
        return (super.getSticky() == null ? false : super.getSticky());
    }

    @Override
    public void setSticky(Boolean sticky) {
        if (!sticky && getSticky() == true && this.getAnnouncementBoard() != null) {
            updateOtherAnnouncementPriorities(getPriority(), this);
            setPriority(-1);
        } else if (this.getAnnouncementBoard() != null && getSticky() == false) {
            Integer maxPriority = getMaxPriority() + 1;
            setPriority(ensureOrder(this.getAnnouncementBoard(), maxPriority));
        }
        super.setSticky(sticky);
    }

    private void updateOtherAnnouncementPriorities(int priority, Announcement targetAnnouncement) {
        for (Announcement announcement : this.getAnnouncementBoard().getAnnouncementSet()) {
            if (!announcement.equals(targetAnnouncement) && announcement.getSticky() && announcement.getPriority() > priority) {
                announcement.setPriority(announcement.getPriority() - 1);
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.messaging.AnnouncementCategory> getCategories() {
        return getCategoriesSet();
    }

    @Deprecated
    public boolean hasAnyCategories() {
        return !getCategoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasSticky() {
        return getSticky() != null;
    }

    @Deprecated
    public boolean hasPlace() {
        return getPlace() != null;
    }

    @Deprecated
    public boolean hasReferedSubjectBegin() {
        return getReferedSubjectBegin() != null;
    }

    @Deprecated
    public boolean hasReferedSubjectEnd() {
        return getReferedSubjectEnd() != null;
    }

    @Deprecated
    public boolean hasVisible() {
        return getVisible() != null;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasApproved() {
        return getApproved() != null;
    }

    @Deprecated
    public boolean hasExcerpt() {
        return getExcerpt() != null;
    }

    @Deprecated
    public boolean hasAuthor() {
        return getAuthor() != null;
    }

    @Deprecated
    public boolean hasCampus() {
        return getCampus() != null;
    }

    @Deprecated
    public boolean hasPressRelease() {
        return getPressRelease() != null;
    }

    @Deprecated
    public boolean hasEditorNotes() {
        return getEditorNotes() != null;
    }

    @Deprecated
    public boolean hasPriority() {
        return getPriority() != null;
    }

    @Deprecated
    public boolean hasAuthorEmail() {
        return getAuthorEmail() != null;
    }

    @Deprecated
    public boolean hasPublication() {
        return getPublication() != null;
    }

    @Deprecated
    public boolean hasLastModification() {
        return getLastModification() != null;
    }

    @Deprecated
    public boolean hasPhotoUrl() {
        return getPhotoUrl() != null;
    }

    @Deprecated
    public boolean hasKeywords() {
        return getKeywords() != null;
    }

}
