package net.sourceforge.fenixedu.domain.assiduousness;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class DailyBalance extends DailyBalance_Base {
    
//    protected DailyBalance(Body body) {
//        super(body);
//    }
    
    public DailyBalance() {
        super();
    }

    // Nao tem horario associado neste dia (possivelmente fim-de-semana
    public DailyBalance(YearMonthDay date) {
        this.setWorkSchedule(null);
        this.setDate(date);
        this.setFixedPeriodAbsence(Duration.ZERO);
        this.setNormalWorkPeriod1Balance(Duration.ZERO);
        this.setNormalWorkPeriod2Balance(Duration.ZERO);
        this.setLunchBreak(Duration.ZERO);
    }
    
    
    public DailyBalance(WorkSchedule workSchedule, YearMonthDay date) {
        this.setWorkSchedule(workSchedule);
        this.setDate(date);
        this.setFixedPeriodAbsence(Duration.ZERO);
        this.setNormalWorkPeriod1Balance(Duration.ZERO);
        this.setNormalWorkPeriod2Balance(Duration.ZERO);
        this.setLunchBreak(Duration.ZERO);
    }
    
    public String toString() {
        return new String(this.getDate().toString() + " " + this.getFixedPeriodAbsence() + " " + this.getNormalWorkPeriod1Balance() + " " + this.getNormalWorkPeriod2Balance() + " " + this.getLunchBreak());
    }
    
}
