/*
 * IAula.java
 *
 * Created on 17 de Outubro de 2002, 20:44
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
import java.io.Serializable;
import java.util.Calendar;

import Util.DiaSemana;
import Util.TipoAula;

public interface IAula extends Serializable, IDomainObject {
    public DiaSemana getDiaSemana();

    public Calendar getInicio();

    public Calendar getFim();

    public TipoAula getTipo();

    public ISala getSala();

    public ITurno getShift();

    public IExecutionPeriod getExecutionPeriod();

    public IRoomOccupation getRoomOccupation();

    //  public IExecutionCourse getDisciplinaExecucao();

    public void setDiaSemana(DiaSemana diaSemana);

    public void setInicio(Calendar inicio);

    public void setFim(Calendar fim);

    public void setTipo(TipoAula tipo);

    public void setSala(ISala sala);

    public void setShift(ITurno turno);

    public void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setRoomOccupation(IRoomOccupation roomOccupation);

    //  public void setDisciplinaExecucao(IExecutionCourse disciplinaExecucao);

    public double hours();
}