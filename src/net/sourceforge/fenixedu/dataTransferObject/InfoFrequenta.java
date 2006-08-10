/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Attends;

/**
 * 
 * @author tfc130
 */
public class InfoFrequenta extends InfoObject {
    protected InfoStudent _aluno;

    protected InfoExecutionCourse _disciplinaExecucao;

    protected InfoEnrolment infoEnrolment;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    // errr..... os objectos info nao vao para a base de dados, por isso nao
    // precisam deste construtor ;) ai esse copy/paste ...:P
    // ass: BlueBus
    public InfoFrequenta() {
    }

    public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao) {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
    }

    public InfoFrequenta(InfoStudent aluno, InfoExecutionCourse disciplinaExecucao,
            InfoEnrolment enrolment) {
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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoFrequenta) {
            InfoFrequenta frequenta = (InfoFrequenta) obj;
            resultado =
            getAluno().equals(frequenta.getAluno())
                    && getDisciplinaExecucao().equals(frequenta.getDisciplinaExecucao());
        }
        return resultado;
    }

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

    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
		this.setAluno(InfoStudent.newInfoFromDomain(frequenta.getAluno()));
		this.setDisciplinaExecucao(InfoExecutionCourse.newInfoFromDomain(frequenta.getDisciplinaExecucao()));
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