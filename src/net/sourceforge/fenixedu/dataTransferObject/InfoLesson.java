package net.sourceforge.fenixedu.dataTransferObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Lesson;
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
    
    private Lesson lesson;
    
    private InfoRoom infoSala;
    private InfoShift infoShift;
    private InfoRoomOccupation infoRoomOccupation;

    public InfoLesson(Lesson lesson) {
    	super.copyFromDomain(lesson);
    	this.lesson = lesson;
    }

    public DiaSemana getDiaSemana() {
        return lesson.getDiaSemana();
    }

    public Calendar getFim() {
        return lesson.getFim();
    }

    public Calendar getInicio() {
        return lesson.getInicio();
    }

    public ShiftType getTipo() {
        return lesson.getTipo();
    }

    public Integer getFrequency() {        
        return lesson.getFrequency();
    }
    
    public Integer getWeekOfQuinzenalStart() {
        return lesson.getWeekOfQuinzenalStart();
    }
    
    public String getWeekDay() {
        final String result = getDiaSemana().getDiaSemana().toString();
        if (result != null && result.equals("7")) {
            return "S";
        }
        if (result != null && result.equals("1")) {
            return "D";
        }
        return result;
    }

    public String getLessonDuration() {
        int hours = this.getFim().get(Calendar.HOUR_OF_DAY) - this.getInicio().get(Calendar.HOUR_OF_DAY);
        int minutes = this.getFim().get(Calendar.MINUTE) - this.getInicio().get(Calendar.MINUTE);

        if (minutes < 0) {
            minutes *= -1;
            hours = hours - 1;
        }

        return hours + ":" + minutes;
    }
    
    public InfoRoom getInfoSala() {
        return (infoSala == null) ? infoSala = InfoRoom.newInfoFromDomain(lesson.getSala()) : infoSala;
    }

    public InfoShift getInfoShift() {
        return (infoShift == null) ? infoShift = InfoShiftWithInfoExecutionCourse.newInfoFromDomain(lesson.getShift()) : infoShift;
    }

    public InfoRoomOccupation getInfoRoomOccupation() {
    	if (infoRoomOccupation == null) {
    		infoRoomOccupation = InfoRoomOccupationWithInfoRoomAndInfoPeriod.newInfoFromDomain(lesson.getRoomOccupation());
    	}
    	return infoRoomOccupation;
	}

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
    	return (lesson != null) ? new InfoLesson(lesson) : null;
    }
    
    public int compareTo(InfoLesson arg0) {
        return INFO_LESSON_COMPARATOR_CHAIN.compare(this, arg0);
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
                    && ((getInfoSala() == null && infoAula.getInfoSala() == null) || 
                            (getInfoSala() != null && getInfoSala().equals(infoAula.getInfoSala())))
                    && ((getInfoRoomOccupation() == null && infoAula.getInfoRoomOccupation() == null) 
                            || (getInfoRoomOccupation() != null && getInfoRoomOccupation().equals(infoAula.getInfoRoomOccupation())));
        }
        return resultado;
    }

    public String toSmsText() {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm");
        final String beginTime = simpleDateFormat.format(getInicio().getTime());
        final String endTime = simpleDateFormat.format(getFim().getTime());

        String result = "";
        result += getDiaSemana().toString() + "\n";
        result += getInfoShift().getInfoDisciplinaExecucao().getSigla() + " ("
                + getTipo().getSiglaTipoAula() + ")";
        result += "\n" + beginTime;
        result += "-" + endTime;
        result += "\nSala=" + getInfoSala().getNome();
        result += "\n\n";

        return result;
    }

}