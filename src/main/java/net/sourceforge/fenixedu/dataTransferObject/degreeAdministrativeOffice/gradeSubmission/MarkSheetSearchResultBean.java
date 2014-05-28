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
package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;

public class MarkSheetSearchResultBean {

    private boolean showStatistics;

    private int totalNumberOfStudents;

    private List<MarkSheet> markSheets;

    public MarkSheetSearchResultBean() {
        markSheets = new ArrayList<MarkSheet>();
    }

    public List<MarkSheet> getMarkSheets() {
        return markSheets;
    }

    public Collection<MarkSheet> getMarkSheetsSortedByEvaluationDate() {
        Collections.sort(getMarkSheets(), MarkSheet.COMPARATOR_BY_EVALUATION_DATE_AND_CREATION_DATE_AND_ID);
        return getMarkSheets();
    }

    public void setMarkSheets(List<MarkSheet> markSheets) {
        this.markSheets = markSheets;
    }

    public void addMarkSheet(MarkSheet markSheet) {
        getMarkSheets().add(markSheet);
    }

    public int getNumberOfEnroledStudents() {
        int numberOfEnroledStudents = 0;
        for (MarkSheet markSheet : getMarkSheets()) {
            if (!isRectificationMarkSheet(markSheet.getMarkSheetState())) {
                numberOfEnroledStudents += markSheet.getEnrolmentEvaluations().size();
            }
        }
        return numberOfEnroledStudents;
    }

    private boolean isRectificationMarkSheet(MarkSheetState markSheetState) {
        return (markSheetState == MarkSheetState.RECTIFICATION || markSheetState == MarkSheetState.RECTIFICATION_NOT_CONFIRMED);
    }

    public int getTotalNumberOfStudents() {
        return totalNumberOfStudents;
    }

    public void setTotalNumberOfStudents(int totalNumberStudents) {
        this.totalNumberOfStudents = totalNumberStudents;
    }

    public boolean isShowStatistics() {
        return showStatistics;
    }

    public void setShowStatistics(boolean showStatistics) {
        this.showStatistics = showStatistics;
    }

}
