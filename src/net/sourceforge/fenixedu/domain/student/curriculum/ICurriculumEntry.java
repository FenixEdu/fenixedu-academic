package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.collections.comparators.ComparatorChain;

public interface ICurriculumEntry {
    
    static final Comparator<ICurriculumEntry> COMPARATOR_BY_ID = new Comparator<ICurriculumEntry>() {
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
	    return o1.getIdInternal().compareTo(o2.getIdInternal());
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD = new Comparator<ICurriculumEntry>() {
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
    	final ExecutionPeriod e1 = o1.getExecutionPeriod();
    	final ExecutionPeriod e2 = o2.getExecutionPeriod();
    	
    	if (e1 == null && e2 == null) {
    	    return 0;
    	} else if (e1 == null) {
    	    return -1;
    	} else if (e2 == null) {
    	    return 1;
    	} 
    	
    	return e1.compareTo(e2);
        }
    };

    static final public Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_ID = new Comparator<ICurriculumEntry>() {
	final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD);
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME = new Comparator<ICurriculumEntry>() {
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
    	int result = COMPARATOR_BY_EXECUTION_PERIOD.compare(o1, o2);
    	return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID = new Comparator<ICurriculumEntry>() {
	final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<ICurriculumEntry>() {
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
    	final ExecutionYear e1 = o1.getExecutionYear();
    	final ExecutionYear e2 = o2.getExecutionYear();
    	
    	if (e1 == null && e2 == null) {
    	    return 0;
    	} else if (e1 == null) {
    	    return -1;
    	} else if (e2 == null) {
    	    return 1;
    	} 
    	
    	return e1.compareTo(e2);
        }
    };
        
    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR_AND_NAME = new Comparator<ICurriculumEntry>() {
        public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
    	int result = COMPARATOR_BY_EXECUTION_YEAR.compare(o1, o2);
    	return (result == 0) ? o1.getName().compareTo(o2.getName()) : result;
        }
    };
    
    static final Comparator<ICurriculumEntry> COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID = new Comparator<ICurriculumEntry>() {
	final public int compare(ICurriculumEntry o1, ICurriculumEntry o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME);
	    comparatorChain.addComparator(ICurriculumEntry.COMPARATOR_BY_ID);

	    return comparatorChain.compare(o1, o2);
	}
    };

    Integer getIdInternal();

    String getCode();

    MultiLanguageString getName();

    Grade getGrade();

    String getGradeValue();
    
    BigDecimal getWeigthForCurriculum();

    BigDecimal getWeigthTimesGrade();

    BigDecimal getEctsCreditsForCurriculum();

    ExecutionPeriod getExecutionPeriod();

    ExecutionYear getExecutionYear();

}
