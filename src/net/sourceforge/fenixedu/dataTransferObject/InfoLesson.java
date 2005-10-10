/*
 * InfoLesson.java
 * 
 * Created on 31 de Outubro de 2002, 11:35
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class InfoLesson extends InfoShowOccupation implements ISmsDTO, Comparable<InfoLesson> {

    private final static ComparatorChain INFO_LESSON_COMPARATOR_CHAIN = new ComparatorChain();
    static {
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("diaSemana.diaSemana"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("inicio"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("fim"));
        INFO_LESSON_COMPARATOR_CHAIN.addComparator(new BeanComparator("infoSala.nome"));
    }
    
    protected DiaSemana _diaSemana;

    protected Calendar _fim;

    protected InfoRoom _infoSala;

    protected Calendar _inicio;

    protected ShiftType _tipo;

    protected InfoShift _infoShift;

    protected InfoRoomOccupation infoRoomOccupation;

    private List infoShiftList = new ArrayList();

    public InfoLesson() {
    }

    public InfoLesson(DiaSemana diaSemana, Calendar inicio, Calendar fim, ShiftType tipo,
            InfoRoom infoSala, InfoRoomOccupation infoRoomOccupation, InfoShift shift) {
        setDiaSemana(diaSemana);
        setInicio(inicio);
        setFim(fim);
        setTipo(tipo);
        setInfoSala(infoSala);
        setInfoRoomOccupation(infoRoomOccupation);
        setInfoShift(shift);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoLesson) {
            InfoLesson infoAula = (InfoLesson) obj;
            resultado = (getDiaSemana().equals(infoAula.getDiaSemana()))
                    && (getInicio().get(Calendar.HOUR_OF_DAY) == infoAula.getInicio().get(
                            Calendar.HOUR_OF_DAY))
                    && (getInicio().get(Calendar.MINUTE) == infoAula.getInicio().get(Calendar.MINUTE))
                    && (getFim().get(Calendar.HOUR_OF_DAY) == infoAula.getFim()
                            .get(Calendar.HOUR_OF_DAY))
                    && (getFim().get(Calendar.MINUTE) == infoAula.getFim().get(Calendar.MINUTE))
                    && (getInfoSala().equals(infoAula.getInfoSala()))
                    && (getInfoRoomOccupation().equals(infoAula.getInfoRoomOccupation()));
        }
        return resultado;
    }

    public DiaSemana getDiaSemana() {
        return _diaSemana;
    }

    public Calendar getFim() {
        return _fim;
    }

    public InfoRoom getInfoSala() {
        return _infoSala;
    }

    public List getInfoShiftList() {
        return infoShiftList;
    }

    public Calendar getInicio() {
        return _inicio;
    }

    public ShiftType getTipo() {
        return _tipo;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        _diaSemana = diaSemana;
    }

    public void setFim(Calendar fim) {
        _fim = fim;
    }

    public void setInfoSala(InfoRoom infoSala) {
        _infoSala = infoSala;
    }

    public void setInfoShiftList(List infoShiftList) {
        this.infoShiftList = infoShiftList;
    }

    public void setInicio(Calendar inicio) {
        _inicio = inicio;
    }

    public void setTipo(ShiftType tipo) {
        _tipo = tipo;
    }

    public String getWeekDay() {
        String result = getDiaSemana().getDiaSemana().toString();
        if (result != null && result.equals("7")) {
            result = "S";
        }
        if (result != null && result.equals("1")) {
            result = "D";
        }
        return result;
    }

    public String getLessonDuration() {
        int hours = this._fim.get(Calendar.HOUR_OF_DAY) - this._inicio.get(Calendar.HOUR_OF_DAY);
        int minutes = this._fim.get(Calendar.MINUTE) - this._inicio.get(Calendar.MINUTE);

        if (minutes < 0) {
            minutes *= -1;
            hours = hours - 1;
        }

        return hours + ":" + minutes;
    }

    public String toString() {
        String result = "[INFOAULA";
        result += ", diaSemana=" + _diaSemana;
        result += ", tipo=" + _tipo;
        result += ", sala=" + _infoSala;
        result += ", shift=" + _infoShift;
        result += "]";
        return result;
    }

    public InfoShift getInfoShift() {
        return _infoShift;
    }

    public void setInfoShift(InfoShift shift) {
        _infoShift = shift;
    }

    public InfoRoomOccupation getInfoRoomOccupation() {
        return infoRoomOccupation;
    }

    public void setInfoRoomOccupation(InfoRoomOccupation infoRoomOccupation) {
        this.infoRoomOccupation = infoRoomOccupation;
    }

    public void copyFromDomain(ILesson lesson) {
        super.copyFromDomain(lesson);
        if (lesson != null) {
            setDiaSemana(lesson.getDiaSemana());
            setFim(lesson.getFim());
            setInicio(lesson.getInicio());
            setTipo(lesson.getTipo());
        }
    }

    public static InfoLesson newInfoFromDomain(ILesson lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

    public String toSmsText() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm");
        String beginTime = simpleDateFormat.format(_inicio.getTime());
        String endTime = simpleDateFormat.format(_fim.getTime());

        String result = "";
        result += _diaSemana.toString() + "\n";
        result += getInfoShift().getInfoDisciplinaExecucao().getSigla() + " ("
                + _tipo.getSiglaTipoAula() + ")";
        result += "\n" + beginTime;
        result += "-" + endTime;
        result += "\nSala=" + _infoSala.getNome();
        result += "\n\n";

        return result;
    }

    public int compareTo(InfoLesson arg0) {
        return INFO_LESSON_COMPARATOR_CHAIN.compare(this, arg0);
    }

    public static InfoSupportLesson newInfoFromDomain(ISupportLesson supportLesson) {
        InfoSupportLesson infoSupportLesson = new InfoSupportLesson();
        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(supportLesson.getProfessorship());

        infoSupportLesson.setEndTime(supportLesson.getEndTime());
        infoSupportLesson.setIdInternal(supportLesson.getIdInternal());
        infoSupportLesson.setPlace(supportLesson.getPlace());
        infoSupportLesson.setStartTime(supportLesson.getStartTime());
        infoSupportLesson.setWeekDay(supportLesson.getWeekDay());

        infoSupportLesson.setInfoProfessorship(infoProfessorship);

        return infoSupportLesson;
    }

}