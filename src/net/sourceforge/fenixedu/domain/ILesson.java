/*
 * ILesson.java
 *
 * Created on 17 de Outubro de 2002, 20:44
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import java.io.Serializable;
import java.util.Calendar;

import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;

public interface ILesson extends Serializable, IDomainObject {
    public DiaSemana getDiaSemana();

    public Calendar getInicio();

    public Calendar getFim();

    public TipoAula getTipo();

    public IRoom getSala();

    public IShift getShift();

    public IExecutionPeriod getExecutionPeriod();

    public IRoomOccupation getRoomOccupation();

    //  public IExecutionCourse getDisciplinaExecucao();

    public void setDiaSemana(DiaSemana diaSemana);

    public void setInicio(Calendar inicio);

    public void setFim(Calendar fim);

    public void setTipo(TipoAula tipo);

    public void setSala(IRoom sala);

    public void setShift(IShift turno);

    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setRoomOccupation(IRoomOccupation roomOccupation);

    //  public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao);

    public double hours();
}