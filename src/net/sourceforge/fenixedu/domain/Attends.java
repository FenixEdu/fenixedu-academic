/*
 * Attends.java
 *
 * Created on 20 de Outubro de 2002, 14:42
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  tfc130
 */
public class Attends extends DomainObject implements IAttends {
    protected IStudent _aluno;

    protected IExecutionCourse _disciplinaExecucao;

    protected IEnrollment _enrolment;

    // códigos internos da base de dados
    private Integer _chaveAluno;

    private Integer _chaveDisciplinaExecucao;

    private Integer _keyEnrolment;

	private List _attendInAttendsSet;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Attends() {
    }

	public Attends(IStudent aluno, IExecutionCourse disciplinaExecucao) {
		setAluno(aluno);
		setDisciplinaExecucao(disciplinaExecucao);
	}

	public Attends(
		IStudent aluno,
		IExecutionCourse disciplinaExecucao,
		IEnrollment enrolment) {
		setAluno(aluno);
		setDisciplinaExecucao(disciplinaExecucao);
		setEnrolment(enrolment);
	}

	public IStudent getAluno() {
		return _aluno;
	}

	public void setAluno(IStudent aluno) {
		_aluno = aluno;
	}

	public Integer getChaveAluno() {
		return _chaveAluno;
	}

	public void setChaveAluno(Integer chaveAluno) {
		_chaveAluno = chaveAluno;
	}

	public IExecutionCourse getDisciplinaExecucao() {
		return _disciplinaExecucao;
	}

	public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao) {
		_disciplinaExecucao = disciplinaExecucao;
	}

	public Integer getChaveDisciplinaExecucao() {
		return _chaveDisciplinaExecucao;
	}

	public void setChaveDisciplinaExecucao(Integer chaveDisciplinaExecucao) {
		_chaveDisciplinaExecucao = chaveDisciplinaExecucao;
	}

	public Integer getKeyEnrolment() {
		return _keyEnrolment;
	}

	public void setKeyEnrolment(Integer integer) {
		_keyEnrolment = integer;
	}

	public IEnrollment getEnrolment() {
		return _enrolment;
	}

	public void setEnrolment(IEnrollment enrolment) {
		this._enrolment = enrolment;
	}

	
	public List getAttendInAttendsSet() {
		return _attendInAttendsSet;
	}

	public void setAttendInAttendsSet(List attendInAttendsSet) {
		this._attendInAttendsSet = attendInAttendsSet;
	}
	
	public List getAttendsSets() {
		List attendsSets = new ArrayList();
		Iterator iterAttendInAttendsSet = 
			getAttendInAttendsSet().iterator();
		IAttendInAttendsSet attendInAttendsSet = null;
		while(iterAttendInAttendsSet.hasNext()){
			attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
			attendsSets.add(attendInAttendsSet.getAttendsSet());
		}
		return attendsSets;
	}
		
	public void addAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
		if( _attendInAttendsSet==null){
			_attendInAttendsSet=new ArrayList();
			_attendInAttendsSet.add(attendInAttendsSet);
		}
		else{
			_attendInAttendsSet.add(attendInAttendsSet);	
		}
		
	}
	
	public void removeAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
			_attendInAttendsSet.remove(attendInAttendsSet);
	}
	
	public boolean existsAttendInAttendsSet(IAttendInAttendsSet attendInAttendsSet) {
		return _attendInAttendsSet.contains(attendInAttendsSet);
	}

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IAttends) {
            IAttends frequenta = (IAttends) obj;
			resultado = getIdInternal().equals(frequenta.getIdInternal());
        }
        return resultado;
    }

    public String toString() {
        String result = "[ATTEND";
        result += ", codigoInterno=" + getIdInternal();
        result += ", Student=" + _aluno;
        result += ", ExecutionCourse=" + _disciplinaExecucao;
        result += ", Enrolment=" + _enrolment;
        result += "]";
        return result;
    }
}
