package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Grade;

public abstract class CreditsCurriculumEntry extends CurriculumEntry {

    public BigDecimal getWeigthForCurriculum() {
	throw new RuntimeException();
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
	return null;
    }

    @Override
    public Grade getGrade() {
	return null;
    }

}
