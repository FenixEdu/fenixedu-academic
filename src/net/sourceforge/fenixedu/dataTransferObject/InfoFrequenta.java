package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

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
            resultado = getAluno().equals(frequenta.getAluno()) && getDisciplinaExecucao().equals(frequenta.getDisciplinaExecucao());
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

    /*
     * Temporary solution to create InfoEnrolment to an Enrolment wrapper
     *  - after create InfoFrequenta wrapper this should be delegated to attends
     */
    private EnrolmentEvaluationType enrolmentEvaluationType;
    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return enrolmentEvaluationType;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType enrolmentEvaluationType) {
        this.enrolmentEvaluationType = enrolmentEvaluationType;
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