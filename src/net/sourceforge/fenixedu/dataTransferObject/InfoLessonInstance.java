package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InfoLessonInstance extends InfoShowOccupation {

    private DomainReference<LessonInstance> lessonInstanceReference;

    public InfoLessonInstance(LessonInstance lessonInstance) {
	this.lessonInstanceReference = new DomainReference<LessonInstance>(lessonInstance);
    }

    public LessonInstance getLessonInstance() {
	return lessonInstanceReference == null ? null : lessonInstanceReference.getObject();
    }

    @Override
    public DiaSemana getDiaSemana() {
	return getLessonInstance().getDayOfweek();
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
	return getLessonInstance().hasLessonInstanceSpaceOccupation() ? InfoRoomOccupation.newInfoFromDomain(getLessonInstance()
		.getLessonInstanceSpaceOccupation()) : null;
    }

    @Override
    public Integer getIdInternal() {
	return getLessonInstance().getIdInternal();
    }

    @Override
    public Calendar getInicio() {
	return getLessonInstance().getBeginDateTime().toCalendar(Language.getLocale());
    }

    @Override
    public Calendar getFim() {
	return getLessonInstance().getEndDateTime().toCalendar(Language.getLocale());
    }

    @Override
    public InfoShift getInfoShift() {
	return getLessonInstance().getLesson().hasShift() ? InfoShift.newInfoFromDomain(getLesson().getShift()) : null;
    }

    @Override
    public ShiftType getTipo() {
	return null;
    }

    public String getShiftTypeCodesPrettyPrint() {
	if (getLessonInstance().hasCourseLoad()) {
	    return getLessonInstance().getCourseLoad().getType().getSiglaTipoAula();
	} else {
	    return getLessonInstance().getLesson().getShift().getShiftTypesCodePrettyPrint();
	}
    }

    public String getShiftTypesPrettyPrint() {
	if (getLessonInstance().hasCourseLoad()) {
	    return Shift.enumerationResourcesBundle.getString(getLessonInstance().getCourseLoad().getType().getName());
	} else {
	    return getLessonInstance().getLesson().getShift().getShiftTypesPrettyPrint();
	}
    }

    private Lesson getLesson() {
	return getLessonInstance().getLesson();
    }
}
