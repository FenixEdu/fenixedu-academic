/*
 * InfoShift.java
 * 
 * Created on 31 de Outubro de 2002, 12:35
 */

package DataBeans;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.IShift;
import Util.TipoAula;

public class InfoShift extends InfoObject {

    protected String _nome;

    protected TipoAula _tipo;

    protected Integer _lotacao;

    protected Integer ocupation;

    protected Double percentage;

    protected Integer availabilityFinal;

    protected InfoExecutionCourse _infoDisciplinaExecucao;

    protected List infoLessons;

    protected List infoClasses;

    public InfoShift() {
    }

    public InfoShift(String nome, TipoAula tipo, Integer lotacao,
            InfoExecutionCourse infoDisciplinaExecucao) {
        setNome(nome);
        setTipo(tipo);
        setLotacao(lotacao);
        setInfoDisciplinaExecucao(infoDisciplinaExecucao);
        setInfoClasses(new ArrayList());
        setInfoLessons(new ArrayList());
    }

    /**
     * @param shiftIdInternal
     */
    public InfoShift(Integer shiftIdInternal) {
        setIdInternal(shiftIdInternal);
    }

    public Integer getSize() {
        if (infoClasses == null)
            return new Integer(0);
        return new Integer(infoClasses.size());
    }

    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public InfoExecutionCourse getInfoDisciplinaExecucao() {
        return _infoDisciplinaExecucao;
    }

    public void setInfoDisciplinaExecucao(InfoExecutionCourse infoDisciplinaExecucao) {
        _infoDisciplinaExecucao = infoDisciplinaExecucao;
    }

    public TipoAula getTipo() {
        return _tipo;
    }

    public void setTipo(TipoAula tipo) {
        _tipo = tipo;
    }

    public Integer getLotacao() {
        return _lotacao;
    }

    public void setLotacao(Integer lotacao) {
        _lotacao = lotacao;
    }

    public Integer getOcupation() {
        return this.ocupation;
    }

    public Double getPercentage() {
        return this.percentage;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoShift) {
            InfoShift infoTurno = (InfoShift) obj;
            resultado = (getNome().equals(infoTurno.getNome()))
                    && (getInfoDisciplinaExecucao().equals(infoTurno.getInfoDisciplinaExecucao()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFO_TURNO";
        result += ", nome=" + _nome;
        result += ", tipo=" + _tipo;
        result += ", lotacao=" + _lotacao;
        result += ", infoDisciplinaExecucao=" + _infoDisciplinaExecucao;
        result += ", infoLessons=" + infoLessons;
        result += "]";
        return result;

    }

    public String getLessons() {

        String result = new String();

        List infoLessonsList = getInfoLessons();
        if (infoLessonsList == null) {
            return "";
        }
        Iterator itLesson = infoLessonsList.iterator();
        int index = 0;
        while (itLesson.hasNext()) {
            index = index + 1;
            InfoLesson lesson = (InfoLesson) itLesson.next();
            result += lesson.getDiaSemana().toString();          
            result += " (";
            result += lesson.getInicio().get(Calendar.HOUR_OF_DAY);
            result += ":";
            result += minutesFormatter(lesson.getInicio().get(Calendar.MINUTE));            
            result += "-";
            result += lesson.getFim().get(Calendar.HOUR_OF_DAY);          
            result += ":";
            result += minutesFormatter(lesson.getFim().get(Calendar.MINUTE));  
            result += ") ";
            result += lesson.getInfoSala().getNome().toString();
                      
            int last = (infoLessonsList.size());
            if (index != last || (index != 1 && index != last)) {
                result += " , ";
            } else {
                result += " ";
            }
        }
        return result;
    }

    private String minutesFormatter(int minute) {
        String result = "";
        if (minute < 10) {
            result += "0";
        }
        result += minute;

        return result;
    }

    /**
     * @return
     */
    public Integer getAvailabilityFinal() {
        return availabilityFinal;
    }

    /**
     * @param integer
     */
    public void setAvailabilityFinal(Integer integer) {
        availabilityFinal = integer;
    }

    /**
     * @return
     */
    public List getInfoLessons() {
        return infoLessons;
    }

    /**
     * @param list
     */
    public void setInfoLessons(List list) {
        infoLessons = list;
    }

    /**
     * @return
     */
    public List getInfoClasses() {
        return infoClasses;
    }

    /**
     * @param list
     */
    public void setInfoClasses(List list) {
        infoClasses = list;
    }

    /**
     * @param ocupation
     */
    public void setOcupation(Integer ocupation) {
        this.ocupation = ocupation;
    }

    /**
     * @param percentage
     */
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public void copyFromDomain(IShift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setNome(shift.getNome());
            setTipo(shift.getTipo());
            setLotacao(shift.getLotacao());
            setOcupation(shift.getOcupation());
            setPercentage(shift.getPercentage());
        }
    }

    public static InfoShift newInfoFromDomain(IShift shift) {
        InfoShift infoShift = null;
        if (shift != null) {
            infoShift = new InfoShift();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }
}