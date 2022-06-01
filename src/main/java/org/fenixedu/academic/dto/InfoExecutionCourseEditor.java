/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto;

import java.util.List;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.commons.i18n.LocalizedString;

public class InfoExecutionCourseEditor extends InfoObject {

    protected String _nome;
    
    protected String _nomePT;
    
    protected String _nomeEN;
    
    protected LocalizedString _nameI18N;
    
    protected String _sigla;

    protected String _programa;

    private Double _theoreticalHours;

    private Double _praticalHours;

    private Double _theoPratHours;

    private Double _labHours;

    private Double _seminaryHours;

    private Double _problemsHours;

    private Double _fieldWorkHours;

    private Double _trainingPeriodHours;

    private Double _tutorialOrientationHours;

    private Boolean availableGradeSubmission;

    protected String comment;

    protected InfoExecutionPeriod infoExecutionPeriod;

    protected List associatedInfoCurricularCourses;

    private EntryPhase entryPhase;

    public InfoExecutionCourseEditor() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoExecutionCourseEditor) {
            InfoExecutionCourseEditor infoExecutionCourse = (InfoExecutionCourseEditor) obj;
            resultado =
                    (getExternalId() != null && infoExecutionCourse.getExternalId() != null && getExternalId().equals(
                            infoExecutionCourse.getExternalId()))
                            || (getSigla().equals(infoExecutionCourse.getSigla()) && getInfoExecutionPeriod().equals(
                                    infoExecutionCourse.getInfoExecutionPeriod()));
        }
        return resultado;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public String getName() {
        return _nomePT;
    }

    public void setName(String _nomePT) {
        this._nomePT = _nomePT;
    }

    public String getNameEn() {
        return _nomeEN;
    }

    public void setNameEn(String _nomeEN) {
        this._nomeEN = _nomeEN;
    }

    public LocalizedString getNameI18N() {
        return _nameI18N;
    }

    public void setNameI18(LocalizedString _nameI18N) {
        this._nameI18N = _nameI18N;
    }

    public String getSigla() {
        return _sigla;
    }

    public void setSigla(String sigla) {
        _sigla = sigla;
    }

    public String getPrograma() {
        return _programa;
    }

    public void setPrograma(String programa) {
        _programa = programa;
    }

    public Double getTheoreticalHours() {
        return _theoreticalHours;
    }

    public void setTheoreticalHours(Double theoreticalHours) {
        _theoreticalHours = theoreticalHours;
    }

    public Double getPraticalHours() {
        return _praticalHours;
    }

    public void setPraticalHours(Double praticalHours) {
        _praticalHours = praticalHours;
    }

    public Double getTheoPratHours() {
        return _theoPratHours;
    }

    public void setTheoPratHours(Double theoPratHours) {
        _theoPratHours = theoPratHours;
    }

    public Double getLabHours() {
        return _labHours;
    }

    public void setLabHours(Double labHours) {
        _labHours = labHours;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String string) {
        comment = string;
    }

    public Double getFieldWorkHours() {
        return _fieldWorkHours;
    }

    public void setFieldWorkHours(Double workHours) {
        _fieldWorkHours = workHours;
    }

    public Double getProblemsHours() {
        return _problemsHours;
    }

    public void setProblemsHours(Double hours) {
        _problemsHours = hours;
    }

    public Double getSeminaryHours() {
        return _seminaryHours;
    }

    public void setSeminaryHours(Double hours) {
        _seminaryHours = hours;
    }

    public Double getTrainingPeriodHours() {
        return _trainingPeriodHours;
    }

    public void setTrainingPeriodHours(Double periodHours) {
        _trainingPeriodHours = periodHours;
    }

    public Double getTutorialOrientationHours() {
        return _tutorialOrientationHours;
    }

    public void setTutorialOrientationHours(Double orientationHours) {
        _tutorialOrientationHours = orientationHours;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    public List getAssociatedInfoCurricularCourses() {
        return associatedInfoCurricularCourses;
    }

    public void setAssociatedInfoCurricularCourses(List list) {
        associatedInfoCurricularCourses = list;
    }

    public Boolean getAvailableGradeSubmission() {
        return availableGradeSubmission;
    }

    public void setAvailableGradeSubmission(Boolean availableGradeSubmission) {
        this.availableGradeSubmission = availableGradeSubmission;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

}
