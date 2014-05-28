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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryProgramAssessment;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;
import net.sourceforge.fenixedu.domain.TutorshipSummarySatisfaction;

public class CreateSummaryBean implements Serializable {

    private static final long serialVersionUID = 161580136110944806L;

    private TutorshipSummary tutorshipSummary;

    private final Degree degree;
    private final Teacher teacher;
    private final ExecutionSemester executionSemester;
    private List<TutorshipSummaryRelationBean> tutorshipRelations;

    public CreateSummaryBean(TutorshipSummary tutorshipSummary) {
        this.tutorshipSummary = tutorshipSummary;

        this.degree = tutorshipSummary.getDegree();
        this.teacher = tutorshipSummary.getTeacher();
        this.executionSemester = tutorshipSummary.getSemester();

        this.howManyContactsEmail = tutorshipSummary.getHowManyContactsEmail();
        this.howManyContactsPhone = tutorshipSummary.getHowManyContactsPhone();
        this.howManyReunionsGroup = tutorshipSummary.getHowManyReunionsGroup();
        this.howManyReunionsIndividual = tutorshipSummary.getHowManyReunionsIndividual();

        this.tutorshipSummarySatisfaction = tutorshipSummary.getTutorshipSummarySatisfaction();
        this.tutorshipSummaryProgramAssessment = tutorshipSummary.getTutorshipSummaryProgramAssessment();
        this.difficulties = tutorshipSummary.getDifficulties();
        this.gains = tutorshipSummary.getGains();
        this.suggestions = tutorshipSummary.getSuggestions();

        this.problemsR1 = tutorshipSummary.getProblemsR1();
        this.problemsR2 = tutorshipSummary.getProblemsR2();
        this.problemsR3 = tutorshipSummary.getProblemsR3();
        this.problemsR4 = tutorshipSummary.getProblemsR4();
        this.problemsR5 = tutorshipSummary.getProblemsR5();
        this.problemsR6 = tutorshipSummary.getProblemsR6();
        this.problemsR7 = tutorshipSummary.getProblemsR7();
        this.problemsR8 = tutorshipSummary.getProblemsR8();
        this.problemsR9 = tutorshipSummary.getProblemsR9();
        this.problemsR10 = tutorshipSummary.getProblemsR10();
        this.problemsOther = tutorshipSummary.getProblemsOther();

        this.gainsR1 = tutorshipSummary.getGainsR1();
        this.gainsR2 = tutorshipSummary.getGainsR2();
        this.gainsR3 = tutorshipSummary.getGainsR3();
        this.gainsR4 = tutorshipSummary.getGainsR4();
        this.gainsR5 = tutorshipSummary.getGainsR5();
        this.gainsR6 = tutorshipSummary.getGainsR6();
        this.gainsR7 = tutorshipSummary.getGainsR7();
        this.gainsR8 = tutorshipSummary.getGainsR8();
        this.gainsR9 = tutorshipSummary.getGainsR9();
        this.gainsR10 = tutorshipSummary.getGainsR10();
        this.gainsOther = tutorshipSummary.getGainsOther();

        feedTutorshipRelations(tutorshipSummary);
    }

    public CreateSummaryBean(Teacher teacher, ExecutionSemester executionSemester, Degree degree) {
        this.degree = degree;
        this.teacher = teacher;
        this.executionSemester = executionSemester;

        createTutorshipRelations();
    }

    private void feedTutorshipRelations(TutorshipSummary tutorshipSummary) {
        List<TutorshipSummaryRelationBean> tutorshipRelations = new ArrayList<TutorshipSummaryRelationBean>();

        for (TutorshipSummaryRelation tsr : tutorshipSummary.getTutorshipSummaryRelations()) {
            tutorshipRelations.add(new TutorshipSummaryRelationBean(tsr));
        }

        this.tutorshipRelations = tutorshipRelations;
    }

    private void createTutorshipRelations() {
        List<TutorshipSummaryRelationBean> tutorshipRelations = new ArrayList<TutorshipSummaryRelationBean>();

        for (Tutorship t : getTeacher().getActiveTutorships(executionSemester.getAcademicInterval())) {
            if (getDegree().equals(t.getStudent().getDegree())) {
                tutorshipRelations.add(new TutorshipSummaryRelationBean(t, executionSemester));
            }
        }

        this.tutorshipRelations = tutorshipRelations;
    }

    public void save() {
        if (isPersisted()) {
            tutorshipSummary.update(this, true);
        } else {
            TutorshipSummary.create(this);
        }
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Degree getDegree() {
        return degree;
    }

    /*
     * Questionary specific fields
     */

    private Integer howManyReunionsGroup;
    private Integer howManyReunionsIndividual;
    private Integer howManyContactsPhone;
    private Integer howManyContactsEmail;

    private TutorshipSummarySatisfaction tutorshipSummarySatisfaction;
    private TutorshipSummaryProgramAssessment tutorshipSummaryProgramAssessment;
    private String difficulties;
    private String gains;
    private String suggestions;

    private Boolean problemsR1;
    private Boolean problemsR2;
    private Boolean problemsR3;
    private Boolean problemsR4;
    private Boolean problemsR5;
    private Boolean problemsR6;
    private Boolean problemsR7;
    private Boolean problemsR8;
    private Boolean problemsR9;
    private Boolean problemsR10;
    private String problemsOther;

    private Boolean gainsR1;
    private Boolean gainsR2;
    private Boolean gainsR3;
    private Boolean gainsR4;
    private Boolean gainsR5;
    private Boolean gainsR6;
    private Boolean gainsR7;
    private Boolean gainsR8;
    private Boolean gainsR9;
    private Boolean gainsR10;
    private String gainsOther;

    public Integer getHowManyReunionsGroup() {
        return howManyReunionsGroup;
    }

    public void setHowManyReunionsGroup(Integer howManyReunionsGroup) {
        this.howManyReunionsGroup = howManyReunionsGroup;
    }

    public Integer getHowManyReunionsIndividual() {
        return howManyReunionsIndividual;
    }

    public void setHowManyReunionsIndividual(Integer howManyReunionsIndividual) {
        this.howManyReunionsIndividual = howManyReunionsIndividual;
    }

    public Integer getHowManyContactsPhone() {
        return howManyContactsPhone;
    }

    public void setHowManyContactsPhone(Integer howManyContactsPhone) {
        this.howManyContactsPhone = howManyContactsPhone;
    }

    public Integer getHowManyContactsEmail() {
        return howManyContactsEmail;
    }

    public void setHowManyContactsEmail(Integer howManyContactsEmail) {
        this.howManyContactsEmail = howManyContactsEmail;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public List<TutorshipSummaryRelationBean> getTutorshipRelations() {
        return tutorshipRelations;
    }

    public int getTutorshipRelationsSize() {
        return getTutorshipRelations().size();
    }

    public void setTutorshipRelations(List<TutorshipSummaryRelationBean> tutorshipRelations) {
        this.tutorshipRelations = tutorshipRelations;
    }

    public String getExternalId() {
        return tutorshipSummary.getExternalId();
    }

    public boolean isPersisted() {
        return tutorshipSummary != null;
    }

    public TutorshipSummarySatisfaction getTutorshipSummarySatisfaction() {
        return tutorshipSummarySatisfaction;
    }

    public void setTutorshipSummarySatisfaction(TutorshipSummarySatisfaction tutorshipSummarySatisfaction) {
        this.tutorshipSummarySatisfaction = tutorshipSummarySatisfaction;
    }

    public TutorshipSummaryProgramAssessment getTutorshipSummaryProgramAssessment() {
        return tutorshipSummaryProgramAssessment;
    }

    public void setTutorshipSummaryProgramAssessment(TutorshipSummaryProgramAssessment tutorshipSummaryProgramAssessment) {
        this.tutorshipSummaryProgramAssessment = tutorshipSummaryProgramAssessment;
    }

    public String getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(String difficulties) {
        this.difficulties = difficulties;
    }

    public String getGains() {
        return gains;
    }

    public void setGains(String gains) {
        this.gains = gains;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public Boolean getProblemsR1() {
        return problemsR1;
    }

    public void setProblemsR1(Boolean problemsR1) {
        this.problemsR1 = problemsR1;
    }

    public Boolean getProblemsR2() {
        return problemsR2;
    }

    public void setProblemsR2(Boolean problemsR2) {
        this.problemsR2 = problemsR2;
    }

    public Boolean getProblemsR3() {
        return problemsR3;
    }

    public void setProblemsR3(Boolean problemsR3) {
        this.problemsR3 = problemsR3;
    }

    public Boolean getProblemsR4() {
        return problemsR4;
    }

    public void setProblemsR4(Boolean problemsR4) {
        this.problemsR4 = problemsR4;
    }

    public Boolean getProblemsR5() {
        return problemsR5;
    }

    public void setProblemsR5(Boolean problemsR5) {
        this.problemsR5 = problemsR5;
    }

    public Boolean getProblemsR6() {
        return problemsR6;
    }

    public void setProblemsR6(Boolean problemsR6) {
        this.problemsR6 = problemsR6;
    }

    public Boolean getProblemsR7() {
        return problemsR7;
    }

    public void setProblemsR7(Boolean problemsR7) {
        this.problemsR7 = problemsR7;
    }

    public Boolean getProblemsR8() {
        return problemsR8;
    }

    public void setProblemsR8(Boolean problemsR8) {
        this.problemsR8 = problemsR8;
    }

    public Boolean getProblemsR9() {
        return problemsR9;
    }

    public void setProblemsR9(Boolean problemsR9) {
        this.problemsR9 = problemsR9;
    }

    public Boolean getProblemsR10() {
        return problemsR10;
    }

    public void setProblemsR10(Boolean problemsR10) {
        this.problemsR10 = problemsR10;
    }

    public String getProblemsOther() {
        return problemsOther;
    }

    public void setProblemsOther(String problemsOther) {
        this.problemsOther = problemsOther;
    }

    public Boolean getGainsR1() {
        return gainsR1;
    }

    public void setGainsR1(Boolean gainsR1) {
        this.gainsR1 = gainsR1;
    }

    public Boolean getGainsR2() {
        return gainsR2;
    }

    public void setGainsR2(Boolean gainsR2) {
        this.gainsR2 = gainsR2;
    }

    public Boolean getGainsR3() {
        return gainsR3;
    }

    public void setGainsR3(Boolean gainsR3) {
        this.gainsR3 = gainsR3;
    }

    public Boolean getGainsR4() {
        return gainsR4;
    }

    public void setGainsR4(Boolean gainsR4) {
        this.gainsR4 = gainsR4;
    }

    public Boolean getGainsR5() {
        return gainsR5;
    }

    public void setGainsR5(Boolean gainsR5) {
        this.gainsR5 = gainsR5;
    }

    public Boolean getGainsR6() {
        return gainsR6;
    }

    public void setGainsR6(Boolean gainsR6) {
        this.gainsR6 = gainsR6;
    }

    public Boolean getGainsR7() {
        return gainsR7;
    }

    public void setGainsR7(Boolean gainsR7) {
        this.gainsR7 = gainsR7;
    }

    public Boolean getGainsR8() {
        return gainsR8;
    }

    public void setGainsR8(Boolean gainsR8) {
        this.gainsR8 = gainsR8;
    }

    public Boolean getGainsR9() {
        return gainsR9;
    }

    public void setGainsR9(Boolean gainsR9) {
        this.gainsR9 = gainsR9;
    }

    public Boolean getGainsR10() {
        return gainsR10;
    }

    public void setGainsR10(Boolean gainsR10) {
        this.gainsR10 = gainsR10;
    }

    public String getGainsOther() {
        return gainsOther;
    }

    public void setGainsOther(String gainsOther) {
        this.gainsOther = gainsOther;
    }
}