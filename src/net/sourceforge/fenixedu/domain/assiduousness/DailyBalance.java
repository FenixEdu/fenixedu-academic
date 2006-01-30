package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class DailyBalance extends DailyBalance_Base {
    
    public DailyBalance() {
        super();
    }

    // Nao tem horario associado neste dia (possivelmente fim-de-semana
    public DailyBalance(YearMonthDay date) {
        setWorkSchedule(null);
        setDate(date);
        setFixedPeriodAbsence(Duration.ZERO);
        setNormalWorkPeriod1Balance(Duration.ZERO);
        setNormalWorkPeriod2Balance(Duration.ZERO);
        setLunchBreak(Duration.ZERO);
    }
    
    
    public DailyBalance(WorkSchedule workSchedule, YearMonthDay date) {
        setWorkSchedule(workSchedule);
        setDate(date);
        setFixedPeriodAbsence(Duration.ZERO);
        setNormalWorkPeriod1Balance(Duration.ZERO);
        setNormalWorkPeriod2Balance(Duration.ZERO);
        setLunchBreak(Duration.ZERO);
    }
    
    public String toString() {
        return new String(getDate().toString() + " " + getFixedPeriodAbsence() + " " + getNormalWorkPeriod1Balance() + " " + getNormalWorkPeriod2Balance() + " " + getLunchBreak());
    }
    
}
