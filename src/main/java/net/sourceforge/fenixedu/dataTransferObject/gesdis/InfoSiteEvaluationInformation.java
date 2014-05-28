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
/*
 * Created on Feb 12, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class InfoSiteEvaluationInformation extends DataTranferObject {
    private InfoCurricularCourse infoCurricularCourse;

    private InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    private List infoSiteEvaluationHistory;

    /**
     *  
     */
    public InfoSiteEvaluationInformation() {
        super();
    }

    /**
     * @return Returns the infoSiteEvaluationHistory.
     */
    public List getInfoSiteEvaluationHistory() {
        return infoSiteEvaluationHistory;
    }

    /**
     * @param infoSiteEvaluationHistory
     *            The infoSiteEvaluationHistory to set.
     */
    public void setInfoSiteEvaluationHistory(List infoSiteEvaluationHistory) {
        this.infoSiteEvaluationHistory = infoSiteEvaluationHistory;
    }

    /**
     * @return Returns the infoSiteEvaluationStatistics.
     */
    public InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics() {
        return infoSiteEvaluationStatistics;
    }

    /**
     * @param infoSiteEvaluationStatistics
     *            The infoSiteEvaluationStatistics to set.
     */
    public void setInfoSiteEvaluationStatistics(InfoSiteEvaluationStatistics infoSiteEvaluationStatistics) {
        this.infoSiteEvaluationStatistics = infoSiteEvaluationStatistics;
    }

    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     *            The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

}