package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DayType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class InfoOccupation extends InfoShowOccupation implements GanttDiagramEvent {

    Interval interval;
    Occupation occupation;

    public InfoOccupation(Occupation occupation, Interval interval) {
        this.occupation = occupation;
        this.interval = interval;
    }

    @Override
    public InfoShift getInfoShift() {
        return null;
    }

    @Override
    public ShiftType getTipo() {
        return null;
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
        return null;
    }

    @Override
    public DiaSemana getDiaSemana() {
        return new DiaSemana(interval.getStart().getDayOfWeek());
    }

    @Override
    public Calendar getInicio() {
        return interval.getStart().toCalendar(I18N.getLocale());
    }

    @Override
    public Calendar getFim() {
        return interval.getEnd().toCalendar(I18N.getLocale());
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
        return occupation.getIntervals();
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
        return new MultiLanguageString(occupation.getSubject());
    }

    @Override
    public int getGanttDiagramEventOffset() {
        return 0;
    }

    @Override
    public String getGanttDiagramEventPeriod() {
        return occupation.getSummary();
    }

    @Override
    public String getGanttDiagramEventObservations() {
        return Joiner.on(" ").join(FluentIterable.from(occupation.getSpaceSet()).filter(new Predicate<Space>() {

            @Override
            public boolean apply(Space input) {
                return input.isActive();
            }
        }).transform(new Function<Space, String>() {
            @Override
            public String apply(Space input) {
                return input.getName();
            }

        }).toSet());
    }

    @Override
    public String getGanttDiagramEventIdentifier() {
        return occupation.getRequest().getExternalId();
    }

    @Override
    public Integer getGanttDiagramEventMonth() {
        return interval.getStart().getMonthOfYear();
    }

    @Override
    public String getGanttDiagramEventUrlAddOns() {
        return null;
    }

    @Override
    public boolean isGanttDiagramEventIntervalsLongerThanOneDay() {
        return false;
    }

    @Override
    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays() {
        return false;
    }

    @Override
    public DayType getGanttDiagramEventDayType(Interval interval) {
        return null;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public String getTitle() {
        return occupation.getSubject();
    }

    public String getDescription() {
        return occupation.getDescription();
    }
}