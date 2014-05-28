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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.io.Serializable;

public class ArchiveOptions implements Serializable {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    private boolean announcements = true;
    private boolean planning = true;
    private boolean schedule = true;
    private boolean shifts = true;
    private boolean groupings = true;
    private boolean evaluations = true;
    private boolean files = false;

    public boolean isAnnouncements() {
        return this.announcements;
    }

    public void setAnnouncements(boolean announcements) {
        this.announcements = announcements;
    }

    public boolean isEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(boolean evaluations) {
        this.evaluations = evaluations;
    }

    public boolean isGroupings() {
        return this.groupings;
    }

    public void setGroupings(boolean groupings) {
        this.groupings = groupings;
    }

    public boolean isPlanning() {
        return this.planning;
    }

    public void setPlanning(boolean planning) {
        this.planning = planning;
    }

    public boolean isSchedule() {
        return this.schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public boolean isShifts() {
        return this.shifts;
    }

    public void setShifts(boolean shifts) {
        this.shifts = shifts;
    }

    public boolean isFiles() {
        return this.files;
    }

    public void setFiles(boolean files) {
        this.files = files;
    }

}