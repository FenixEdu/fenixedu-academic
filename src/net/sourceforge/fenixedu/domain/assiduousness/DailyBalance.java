package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;

public class DailyBalance extends DailyBalance_Base {
    
    public DailyBalance() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    // Nao tem horario associado neste dia (possivelmente fim-de-semana
    public DailyBalance(YearMonthDay date) {
    	this();
//        setWorkSchedule(null);
        setDate(date);
        setFixedPeriodAbsence(Duration.ZERO);
        setNormalWorkPeriod1Balance(Duration.ZERO);
        setNormalWorkPeriod2Balance(Duration.ZERO);
        setLunchBreak(Duration.ZERO);
    }
    
    
    public DailyBalance(WorkSchedule workSchedule, YearMonthDay date) {
    	this();
//        setWorkSchedule(workSchedule);
        setDate(date);
        setFixedPeriodAbsence(Duration.ZERO);
        setNormalWorkPeriod1Balance(Duration.ZERO);
        setNormalWorkPeriod2Balance(Duration.ZERO);
        setLunchBreak(Duration.ZERO);
    }
    
}
