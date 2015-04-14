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

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.EvaluationSeason;

public class InfoFrequenta extends InfoObject {
    protected InfoStudent _aluno;

    protected InfoExecutionCourse _disciplinaExecucao;

    protected InfoEnrolment infoEnrolment;

    public InfoFrequenta() {
    }

    public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao) {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
    }

    public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao, InfoEnrolment enrolment) {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
        setInfoEnrolment(enrolment);
    }

    public InfoStudent getAluno() {
        return _aluno;
    }

    public void setAluno(InfoStudent aluno) {
        _aluno = aluno;
    }

    public InfoExecutionCourse getDisciplinaExecucao() {
        return _disciplinaExecucao;
    }

    public void setDisciplinaExecucao(InfoExecutionCourse disciplinaExecucao) {
        _disciplinaExecucao = disciplinaExecucao;
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoFrequenta) {
            InfoFrequenta frequenta = (InfoFrequenta) obj;
            resultado =
                    getAluno().equals(frequenta.getAluno()) && getDisciplinaExecucao().equals(frequenta.getDisciplinaExecucao());
        }
        return resultado;
    }

    @Override
    public String toString() {
        String result = "[ATTEND";
        result += ", Registration=" + _aluno;
        result += ", ExecutionCourse=" + _disciplinaExecucao;
        result += ", Enrolment=" + infoEnrolment;
        result += "]";
        return result;
    }

    public InfoEnrolment getInfoEnrolment() {
        return infoEnrolment;
    }

    public void setInfoEnrolment(InfoEnrolment enrolment) {
        this.infoEnrolment = enrolment;

    }

    /*
     * Temporary solution to create InfoEnrolment to an Enrolment wrapper -
     * after create InfoFrequenta wrapper this should be delegated to attends
     */
    private EvaluationSeason evaluationSeason;

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
        this.setAluno(InfoStudent.newInfoFromDomain(frequenta.getRegistration()));
        this.setDisciplinaExecucao(InfoExecutionCourse.newInfoFromDomain(frequenta.getExecutionCourse()));
        this.setInfoEnrolment(InfoEnrolment.newInfoFromDomain(frequenta.getEnrolment()));
    }

    public static InfoFrequenta newInfoFromDomain(Attends frequenta) {
        InfoFrequenta infoFrequenta = null;
        if (frequenta != null) {
            infoFrequenta = new InfoFrequenta();
            infoFrequenta.copyFromDomain(frequenta);
        }
        return infoFrequenta;
    }

}