/*
 * Frequenta.java Created on 20 de Outubro de 2002, 14:42
 */

package DataBeans;

import java.io.Serializable;

/**
 * @author João Mota
 */
public class InfoAttendWithEnrollment extends InfoObject implements
        Serializable
{
    protected InfoStudent _aluno;

    protected InfoExecutionCourse _disciplinaExecucao;

    protected Integer enrollments;
    
    protected InfoEnrolment infoEnrolment;

   
    public InfoAttendWithEnrollment()
    {
    }

    public InfoAttendWithEnrollment(InfoStudent aluno,
            InfoExecutionCourse disciplinaExecucao)
    {
        setAluno(aluno);
        setDisciplinaExecucao(disciplinaExecucao);
    }

    public InfoStudent getAluno()
    {
        return _aluno;
    }

    public void setAluno(InfoStudent aluno)
    {
        _aluno = aluno;
    }

    public InfoExecutionCourse getDisciplinaExecucao()
    {
        return _disciplinaExecucao;
    }

    public void setDisciplinaExecucao(InfoExecutionCourse disciplinaExecucao)
    {
        _disciplinaExecucao = disciplinaExecucao;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoFrequenta)
        {
            InfoFrequenta frequenta = (InfoFrequenta) obj;
            resultado = //getCodigoInterno().equals(((Frequenta)obj).getCodigoInterno());
            getAluno().equals(frequenta.getAluno())
                    && getDisciplinaExecucao().equals(getDisciplinaExecucao());
        }
        return resultado;
    }

    /**
     * @return Returns the enrollments.
     */
    public Integer getEnrollments()
    {
        return enrollments;
    }

    /**
     * @param enrollments
     *            The enrollments to set.
     */
    public void setEnrollments(Integer enrollments)
    {
        this.enrollments = enrollments;
    }
    /**
     * @return Returns the infoEnrolment.
     */
    public InfoEnrolment getInfoEnrolment()
    {
        return infoEnrolment;
    }
    /**
     * @param infoEnrolment The infoEnrolment to set.
     */
    public void setInfoEnrolment(InfoEnrolment infoEnrolment)
    {
        this.infoEnrolment = infoEnrolment;
    }
}
