/*
 * InfoShift.java
 * 
 * Created on 31 de Outubro de 2002, 12:35
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.NumberUtils;

public class InfoShift extends InfoObject {

	private final Shift shift;

	public InfoShift(final Shift shift) {
		this.shift = shift;
    }

    public Integer getSize() {
    	return Integer.valueOf(shift.getAssociatedClasses().size());
    }

    public String getNome() {
        return shift.getNome();
    }

    public InfoExecutionCourse getInfoDisciplinaExecucao() {
        return InfoExecutionCourse.newInfoFromDomain(shift.getDisciplinaExecucao());
    }

    public ShiftType getTipo() {
        return shift.getTipo();
    }

    public Integer getLotacao() {
        return shift.getLotacao();
    }

    public Integer getOcupation() {
        return shift.getStudentsCount();
    }

    public Double getPercentage() {
    	return NumberUtils.formatNumber(Double.valueOf(getOcupation().floatValue() * 100 / getLotacao().floatValue()), 1);
    }

    public boolean equals(Object obj) {
    	return obj != null && shift == ((InfoShift) obj).shift;
    }

    public String toString() {
    	return shift.toString();
    }

    public String getLessons() {
        final StringBuilder stringBuilder = new StringBuilder();

        final List<InfoLesson> infoLessonsList = getInfoLessons();
        if (infoLessonsList != null) {
            int index = 0;
            for (final InfoLesson infoLesson : infoLessonsList) {
                index = index + 1;
                stringBuilder.append(infoLesson.getDiaSemana().toString());          
                stringBuilder.append(" (");
                stringBuilder.append(DateFormatUtil.format("HH:mm", infoLesson.getInicio().getTime()));
                stringBuilder.append("-");
                stringBuilder.append(DateFormatUtil.format("HH:mm", infoLesson.getFim().getTime()));
                stringBuilder.append(") ");
                if(infoLesson.getInfoSala() != null) {
                    stringBuilder.append(infoLesson.getInfoSala().getNome().toString());
                }

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
        return shift.getAvailabilityFinal();
    }

    public List<InfoLesson> getInfoLessons() {
    	final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
    	for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
    		infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
    	}
        return infoLessons;
    }

    public List getInfoClasses() {
    	final List<InfoClass> infoClasses = new ArrayList<InfoClass>();
    	for (final SchoolClass schoolClass : shift.getAssociatedClassesSet()) {
    		infoClasses.add(InfoClass.newInfoFromDomain(schoolClass));
    	}
        return infoClasses;
    }

	@Override
	public Integer getIdInternal() {
		return shift.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

	public static InfoShift newInfoFromDomain(final Shift shift) {
		return shift == null ? null : new InfoShift(shift);
	}

}
