/*
 * InfoLesson.java
 * 
 * Created on 31 de Outubro de 2002, 11:35
 */

package DataBeans;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Util.DiaSemana;
import Util.TipoAula;

public class InfoLesson extends InfoObject
{
    protected DiaSemana _diaSemana;
    protected Calendar _fim;
    protected InfoExecutionCourse _infoDisciplinaExecucao;
    protected InfoRoom _infoSala;
    protected Calendar _inicio;
    protected TipoAula _tipo;

    private List infoShiftList = new ArrayList();

    public InfoLesson()
    {
    }
    /**
	 * @param diaSemana
	 * @param inicio
	 * @param fim
	 * @param tipo
	 * @param infoSala
	 * @param infoDisciplinaExecucao
	 */
    public InfoLesson(
        DiaSemana diaSemana,
        Calendar inicio,
        Calendar fim,
        TipoAula tipo,
        InfoRoom infoSala,
        InfoExecutionCourse infoDisciplinaExecucao)
    {
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setInfoSala(infoSala);
        setInfoDisciplinaExecucao(infoDisciplinaExecucao);
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoLesson)
        {
            InfoLesson infoAula = (InfoLesson) obj;
            resultado =
                (getDiaSemana().equals(infoAula.getDiaSemana()))
                    && (getInicio().get(Calendar.HOUR_OF_DAY)
                        == infoAula.getInicio().get(Calendar.HOUR_OF_DAY))
                    && (getInicio().get(Calendar.MINUTE) == infoAula.getInicio().get(Calendar.MINUTE))
                    && (getFim().get(Calendar.HOUR_OF_DAY) == infoAula.getFim().get(Calendar.HOUR_OF_DAY))
                    && (getFim().get(Calendar.MINUTE) == infoAula.getFim().get(Calendar.MINUTE))
                    && (getInfoSala().equals(infoAula.getInfoSala()));
        }

        return resultado;
    }

    public DiaSemana getDiaSemana()
    {
        return _diaSemana;
    }

    public Calendar getFim()
    {
        return _fim;
    }

    public InfoExecutionCourse getInfoDisciplinaExecucao()
    {
        return _infoDisciplinaExecucao;
    }

    public InfoRoom getInfoSala()
    {
        return _infoSala;
    }

    /**
	 * @return List
	 */
    public List getInfoShiftList()
    {
        return infoShiftList;
    }

    public Calendar getInicio()
    {
        return _inicio;
    }

    public TipoAula getTipo()
    {
        return _tipo;
    }

    public void setDiaSemana(DiaSemana diaSemana)
    {
        _diaSemana = diaSemana;
    }

    public void setFim(Calendar fim)
    {
        _fim = fim;
    }

    public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao)
    {
        _infoDisciplinaExecucao = infoDisciplinaExecucao;
    }

    public void setInfoSala(InfoRoom infoSala)
    {
        _infoSala = infoSala;
    }

    /**
	 * Sets the infoShiftList.
	 * 
	 * @param infoShiftList
	 *            The infoShiftList to set
	 */
    public void setInfoShiftList(List infoShiftList)
    {
        this.infoShiftList = infoShiftList;
    }

    public void setInicio(Calendar inicio)
    {
        _inicio = inicio;
    }

    public void setTipo(TipoAula tipo)
    {
        _tipo = tipo;
    }

    public String getWeekDay()
    {
        String result = getDiaSemana().getDiaSemana().toString();
        if (result != null && result.equals("7"))
        {
            result = "S";
        }
        if (result != null && result.equals("1"))
        {
            result = "D";
        }
        return result;
    }

    public String getLessonDurationString()
    {
        int hours = this._fim.get(Calendar.HOUR_OF_DAY) - this._inicio.get(Calendar.HOUR_OF_DAY);
        int minutes = this._fim.get(Calendar.MINUTE) - this._inicio.get(Calendar.MINUTE);

        if (minutes < 0)
        {
            minutes *= -1;
        }

        return hours + ":" + minutes;
    }

    public Double getLessonDuration()
    {
        int hours = this._fim.get(Calendar.HOUR_OF_DAY) - this._inicio.get(Calendar.HOUR_OF_DAY);
        int minutes = this._fim.get(Calendar.MINUTE) - this._inicio.get(Calendar.MINUTE);

        if (minutes < 0)
        {
            minutes *= -1;
        }

        return new Double(hours + (minutes/60));
    }

    public String toString()
    {
        String result = "[INFOAULA";
        result += ", diaSemana=" + _diaSemana;
        result += ", inicio=" + _inicio;
        result += ", fim=" + _fim;
        result += ", tipo=" + _tipo;
        result += ", sala=" + _infoSala;
        result += ", disciplinaExecucao=" + _infoDisciplinaExecucao;
        result += "]";
        return result;
    }

}