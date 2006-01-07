/*
 * InfoShift.java
 * 
 * Created on 31 de Outubro de 2002, 12:35
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class InfoShift extends InfoObject {

    protected String _nome;

    protected ShiftType _tipo;

    protected Integer _lotacao;

    protected Integer ocupation;

    protected Double percentage;

    protected Integer availabilityFinal;

    protected InfoExecutionCourse _infoDisciplinaExecucao;

    protected List infoLessons;

    protected List infoClasses;

    public InfoShift() {
    }

    public InfoShift(String nome, ShiftType tipo, Integer lotacao,
            InfoExecutionCourse infoDisciplinaExecucao) {
        setNome(nome);
        setTipo(tipo);
        setLotacao(lotacao);
        setInfoDisciplinaExecucao(infoDisciplinaExecucao);
        setInfoClasses(new ArrayList());
        setInfoLessons(new ArrayList());
    }

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

    public ShiftType getTipo() {
        return _tipo;
    }

    public void setTipo(ShiftType tipo) {
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

    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    public String getLessons() {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<InfoLesson> infoLessonsList = getInfoLessons();
        if (infoLessonsList != null) {
            int index = 0;
            for (final InfoLesson infoLesson : infoLessonsList) {
                index = index + 1;
                stringBuilder.append(infoLesson.getDiaSemana().toString());          
                stringBuilder.append(" (");
                stringBuilder.append(hourFormat.format(infoLesson.getInicio().getTime()));
                stringBuilder.append("-");
                stringBuilder.append(hourFormat.format(infoLesson.getFim().getTime()));
                stringBuilder.append(") ");
                stringBuilder.append(infoLesson.getInfoSala().getNome().toString());

                int last = (infoLessonsList.size());
                if (index != last || (index != 1 && index != last)) {
                    stringBuilder.append(" , ");
                } else {
                    stringBuilder.append(" ");
                }
            }
        }

        return stringBuilder.toString();
    }

    public Integer getAvailabilityFinal() {
        return availabilityFinal;
    }

    public void setAvailabilityFinal(Integer integer) {
        availabilityFinal = integer;
    }

    public List getInfoLessons() {
        return infoLessons;
    }

    public void setInfoLessons(List list) {
        infoLessons = list;
    }

    public List getInfoClasses() {
        return infoClasses;
    }

    public void setInfoClasses(List list) {
        infoClasses = list;
    }

    public void setOcupation(Integer ocupation) {
        this.ocupation = ocupation;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public void copyFromDomain(Shift shift) {
        super.copyFromDomain(shift);
        if (shift != null) {
            setNome(shift.getNome());
            setTipo(shift.getTipo());
            setLotacao(shift.getLotacao());           
        }
    }

    public static InfoShift newInfoFromDomain(Shift shift) {
        InfoShift infoShift = null;
        if (shift != null) {
            infoShift = new InfoShift();
            infoShift.copyFromDomain(shift);
        }
        return infoShift;
    }
}