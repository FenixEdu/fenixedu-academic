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
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;

import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author tfc130
 */
public class InfoCurricularCourse extends InfoObject implements Comparable, ISiteComponent {

    static final public Comparator<InfoCurricularCourse> COMPARATOR_BY_NAME_AND_ID = new Comparator<InfoCurricularCourse>() {
        @Override
        public int compare(InfoCurricularCourse o1, InfoCurricularCourse o2) {
            int result = Collator.getInstance().compare(o1.getName(), o2.getName());
            return (result == 0) ? o1.getExternalId().compareTo(o2.getExternalId()) : result;
        }
    };

    private final CurricularCourse curricularCourse;

    private final boolean showEnVersion = I18N.getLocale().equals(MultiLanguageString.en);

    private List<InfoCurricularCourseScope> infoScopes;

    private List infoAssociatedExecutionCourses;

    private InfoUniversity infoUniversity;

    private String chosen;

    public InfoCurricularCourse(final CurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public Boolean getBasic() {
        return getCurricularCourse().getBasic();
    }

    public String getOwnershipType() {
        return getBasic() == null ? "" : getBasic().booleanValue() ? "Básica" : "Não Básica";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCurricularCourse && getCurricularCourse() == ((InfoCurricularCourse) obj).getCurricularCourse();
    }

    @Override
    public String toString() {
        return getCurricularCourse().toString();
    }

    public String getCode() {
        return getCurricularCourse().getCode();
    }

    public Double getCredits() {
        return getCurricularCourse().getCredits();
    }

    public Double getLabHours() {
        return getCurricularCourse().getLabHours();
    }

    public Double getPraticalHours() {
        return getCurricularCourse().getPraticalHours();
    }

    public Double getTheoPratHours() {
        return getCurricularCourse().getTheoPratHours();
    }

    public Double getTheoreticalHours() {
        return getCurricularCourse().getTheoreticalHours();
    }

    public Double getFieldWorkHours() {
        return getCurricularCourse().getFieldWorkHours();
    }

    public Double getProblemsHours() {
        return getCurricularCourse().getProblemsHours();
    }

    public Double getSeminaryHours() {
        return getCurricularCourse().getSeminaryHours();
    }

    public Double getTrainingPeriodHours() {
        return getCurricularCourse().getTrainingPeriodHours();
    }

    public Double getTutorialOrientationHours() {
        return getCurricularCourse().getTutorialOrientationHours();
    }

    public Double getTotalLabHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.LABORATORIAL, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalPraticalHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.PRATICA, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalTheoPratHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TEORICO_PRATICA, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalTheoreticalHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TEORICA, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalFieldWorkHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.FIELD_WORK, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalProblemsHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.PROBLEMS, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalSeminaryHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.SEMINARY, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalTrainingPeriodHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TRAINING_PERIOD, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public Double getTotalTutorialOrientationHours() {
        BigDecimal totalHours = getCurricularCourse().getTotalHoursByShiftType(ShiftType.TUTORIAL_ORIENTATION, null);
        return totalHours != null ? totalHours.doubleValue() : 0d;
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return InfoDegreeCurricularPlan.newInfoFromDomain(getCurricularCourse().getDegreeCurricularPlan());
    }

    public List<InfoCurricularCourseScope> getInfoScopes() {
        return infoScopes;
    }

    public void setInfoScopes(List<InfoCurricularCourseScope> infoScopes) {
        this.infoScopes = infoScopes;
    }

    public CurricularCourseType getType() {
        return getCurricularCourse().getType();
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
        return getCurricularCourse().getCurricularCourseExecutionScope();
    }

    public Boolean getMandatory() {
        return getCurricularCourse().getMandatory();
    }

    public boolean infoCurricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }

    public InfoCurricularCourseScope getInfoCurricularCourseScope(InfoBranch infoBranch, Integer semester) {
        InfoCurricularCourseScope infoCurricularCourseScope = null;
        Iterator iterator = this.getInfoScopes().iterator();
        while (iterator.hasNext()) {
            InfoCurricularCourseScope infoCurricularCourseScope2 = (InfoCurricularCourseScope) iterator.next();
            if (infoCurricularCourseScope2.getInfoBranch().equals(infoBranch)
                    && infoCurricularCourseScope2.getInfoCurricularSemester().getSemester().equals(semester)) {
                infoCurricularCourseScope = infoCurricularCourseScope2;
                break;
            }
        }
        return infoCurricularCourseScope;
    }

    public InfoUniversity getInfoUniversity() {
        return infoUniversity;
    }

    public void setInfoUniversity(InfoUniversity university) {
        this.infoUniversity = university;
    }

    @Override
    public int compareTo(Object arg0) {
        int result = 0;
        if (getMinScope() < ((InfoCurricularCourse) arg0).getMinScope()) {
            result = -1;
        } else if (getMinScope() > ((InfoCurricularCourse) arg0).getMinScope()) {
            return 1;
        }
        return result;
    }

    private int getMinScope() {
        int minScope = 0;
        List scopes = getInfoScopes();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            InfoCurricularCourseScope infoScope = (InfoCurricularCourseScope) iter.next();
            if (minScope == 0 || minScope > infoScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().intValue()) {
                minScope = infoScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().intValue();
            }
        }

        return minScope;
    }

    public List getInfoAssociatedExecutionCourses() {
        return infoAssociatedExecutionCourses;
    }

    public void setInfoAssociatedExecutionCourses(List infoAssociatedExecutionCourses) {
        this.infoAssociatedExecutionCourses = infoAssociatedExecutionCourses;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public Double getEctsCredits() {
        return getCurricularCourse().getEctsCredits();
    }

    public Integer getEnrollmentWeigth() {
        return getCurricularCourse().getEnrollmentWeigth();
    }

    public Integer getMaximumValueForAcumulatedEnrollments() {
        return getCurricularCourse().getMaximumValueForAcumulatedEnrollments();
    }

    public Integer getMinimumValueForAcumulatedEnrollments() {
        return getCurricularCourse().getMinimumValueForAcumulatedEnrollments();
    }

    public Double getWeigth() {
        return getCurricularCourse().getWeigth();
    }

    public Boolean getMandatoryEnrollment() {
        return getCurricularCourse().getMandatoryEnrollment();
    }

    public Boolean getEnrollmentAllowed() {
        return getCurricularCourse().getEnrollmentAllowed();
    }

    public String getAcronym() {
        return getCurricularCourse().getAcronym();
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getName() {
        return showEnVersion && getCurricularCourse().getNameEn() != null && getCurricularCourse().getNameEn().length() > 0 ? getCurricularCourse()
                .getNameEn() : getCurricularCourse().getName();
    }

    public String getNameEn() {
        return getCurricularCourse().getNameEn();
    }

    public String getNameAndCode() {
        return getCode() + " - " + getName();
    }

    public GradeScale getGradeScale() {
        return getCurricularCourse().getGradeScale();
    }

    @Override
    public String getExternalId() {
        return getCurricularCourse().getExternalId();
    }

    public RegimeType getRegimeType() {
        return getCurricularCourse().getRegimeType();
    }

}
