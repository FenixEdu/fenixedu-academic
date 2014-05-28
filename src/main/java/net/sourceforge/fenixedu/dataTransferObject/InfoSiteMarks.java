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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Tânia Pousão
 * 
 * 
 */
public class InfoSiteMarks extends DataTranferObject implements ISiteComponent {
    private InfoEvaluation infoEvaluation;

    private List marksList;

    private List infoAttends;

    private List marksListErrors;

    private List studentsListErrors;

    private HashMap hashMarks;

    @Override
    public boolean equals(Object objectToCompare) {
        boolean result = false;

        if (objectToCompare instanceof InfoSiteMarks
                && (((((InfoSiteMarks) objectToCompare).getInfoEvaluation() != null && this.getInfoEvaluation() != null && ((InfoSiteMarks) objectToCompare)
                        .getInfoEvaluation().equals(this.getInfoEvaluation())) || ((InfoSiteMarks) objectToCompare)
                        .getInfoEvaluation() == null && this.getInfoEvaluation() == null))) {
            result = true;
        }

        if (((InfoSiteMarks) objectToCompare).getMarksList() == null && this.getMarksList() == null && result == true) {
            return true;
        }

        if (((InfoSiteMarks) objectToCompare).getMarksList() == null || this.getMarksList() == null
                || ((InfoSiteMarks) objectToCompare).getMarksList().size() != this.getMarksList().size()) {
            return false;
        }

        ListIterator iter1 = ((InfoSiteMarks) objectToCompare).getMarksList().listIterator();
        ListIterator iter2 = this.getMarksList().listIterator();
        while (result && iter1.hasNext()) {
            InfoMark infoMark1 = (InfoMark) iter1.next();
            InfoMark infoMark2 = (InfoMark) iter2.next();
            if (!infoMark1.equals(infoMark2)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * @return
     */
    public List getMarksList() {
        return marksList;
    }

    /**
     * @param list
     */
    public void setMarksList(List list) {
        marksList = list;
    }

    /**
     * @return
     */
    public InfoEvaluation getInfoEvaluation() {
        return infoEvaluation;
    }

    /**
     * @param exam
     */
    public void setInfoEvaluation(InfoEvaluation evaluation) {
        infoEvaluation = evaluation;
    }

    /**
     * @return
     */
    public List getMarksListErrors() {
        return marksListErrors;
    }

    /**
     * @param marksListErrors
     */
    public void setMarksListErrors(List marksListErrors) {
        this.marksListErrors = marksListErrors;
    }

    /**
     * @return
     */
    public List getStudentsListErrors() {
        return studentsListErrors;
    }

    /**
     * @param list
     */
    public void setStudentsListErrors(List list) {
        studentsListErrors = list;
    }

    /**
     * @return Returns the attendsList.
     */
    public List getInfoAttends() {
        return infoAttends;
    }

    /**
     * @param attendsList
     *            The attendsList to set.
     */
    public void setInfoAttends(List attendsList) {
        this.infoAttends = attendsList;
    }

    /**
     * @return Returns the hashMarks.
     */
    public HashMap getHashMarks() {
        return hashMarks;
    }

    /**
     * @param hashMarks
     *            The hashMarks to set.
     */
    public void setHashMarks(HashMap hashMarks) {
        this.hashMarks = hashMarks;
    }

    public String getMarks(String studentNumber) {
        return (String) hashMarks.get(studentNumber);

    }

}