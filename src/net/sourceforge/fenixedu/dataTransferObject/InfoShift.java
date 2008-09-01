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
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.NumberUtils;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class InfoShift extends InfoObject {

    public static final Comparator<InfoShift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = new Comparator<InfoShift>() {

	@Override
	public int compare(InfoShift o1, InfoShift o2) {
	    final int c1 = o1.getShift().getExecutionCourse().getNome().compareTo(o2.getShift().getExecutionCourse().getNome());
	    if (c1 != 0) {
		return c1;
	    }
	    final int c2 = o1.getShiftTypesIntegerComparator().compareTo(o2.getShiftTypesIntegerComparator());
	    if (c2 != 0) {
		return c2;
	    }
	    final int c3 = o1.getLessonsStringComparator().compareTo(o2.getLessonsStringComparator());
	    return c3 == 0 ? o1.getShift().getIdInternal().compareTo(o2.getShift().getIdInternal()) : c3;
	}
	
    };

    private final DomainReference<Shift> shift;

    public InfoShift(final Shift shift) {
	this.shift = new DomainReference<Shift>(shift);
    }

    public Integer getSize() {
	return Integer.valueOf(getShift().getAssociatedClasses().size());
    }

    public String getNome() {
	return getShift().getNome();
    }

    public InfoExecutionCourse getInfoDisciplinaExecucao() {
	return InfoExecutionCourse.newInfoFromDomain(getShift().getExecutionCourse());
    }

    public Set<ShiftType> getSortedTypes() {
	return getShift().getSortedTypes();
    }

    public Integer getShiftTypesIntegerComparator() {
	return getShift().getShiftTypesIntegerComparator();
    }

    public String getLessonsStringComparator() {
	return getShift().getLessonsStringComparator();
    }

    public String getShiftTypesPrettyPrint() {
	return getShift().getShiftTypesPrettyPrint();
    }

    public String getShiftTypesCodePrettyPrint() {
	return getShift().getShiftTypesCodePrettyPrint();
    }

    public Integer getLotacao() {
	return getShift().getLotacao();
    }

    public Integer getOcupation() {
	return getShift().getStudentsCount();
    }

    public Double getPercentage() {
	return NumberUtils.formatNumber(Double.valueOf(getOcupation().floatValue() * 100 / getLotacao().floatValue()), 1);
    }

    public boolean equals(Object obj) {
	return obj != null && getShift() == ((InfoShift) obj).getShift();
    }

    public String toString() {
	return getShift().toString();
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
		if (infoLesson.getInfoSala() != null) {
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

    public List<InfoLesson> getInfoLessons() {
	final List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
	for (final Lesson lesson : getShift().getLessonsOrderedByWeekDayAndStartTime()) {
	    infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
	}
	return infoLessons;
    }

    public List<InfoClass> getInfoClasses() {
	final List<InfoClass> infoClasses = new ArrayList<InfoClass>();
	for (final SchoolClass schoolClass : getShift().getAssociatedClassesSet()) {
	    infoClasses.add(InfoClass.newInfoFromDomain(schoolClass));
	}
	return infoClasses;
    }

    @Override
    public Integer getIdInternal() {
	return getShift().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    public static InfoShift newInfoFromDomain(final Shift shift) {
	return shift == null ? null : new InfoShift(shift);
    }

    public Shift getShift() {
	return shift == null ? null : shift.getObject();
    }

    public boolean containsType(ShiftType shiftType) {
	return getShift().containsType(shiftType);
    }
}
