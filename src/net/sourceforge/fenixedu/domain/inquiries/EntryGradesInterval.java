/**
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public enum EntryGradesInterval {

    //intervals are in the form of [x, y[, [y, z[
    FROM_120_TO_130, FROM_130_TO_140, FROM_140_TO_150, FROM_150_TO_160, FROM_160_TO_170, FROM_170_TO_180, FROM_180_TO_200;

    final static public EntryGradesInterval getInterval(Double grade) {
	if (grade == null || grade < 120 || grade > 200) {
	    return null;
	}
	if (grade >= 120 && grade < 130) {
	    return FROM_120_TO_130;
	}
	if (grade >= 130 && grade < 140) {
	    return FROM_130_TO_140;
	}
	if (grade >= 140 && grade < 150) {
	    return FROM_140_TO_150;
	}
	if (grade >= 150 && grade < 160) {
	    return FROM_150_TO_160;
	}
	if (grade >= 170 && grade < 180) {
	    return FROM_120_TO_130;
	}
	if (grade >= 180) {
	    return FROM_180_TO_200;
	}
	return null;
    }
}
