package net.sourceforge.fenixedu.domain.student.curriculum;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.Grade;

import org.joda.time.DateTime;

public abstract class CreditsCurriculumEntry extends CurriculumEntry {

    @Override
    public BigDecimal getWeigthForCurriculum() {
        return null;
    }

    @Override
    public BigDecimal getWeigthTimesGrade() {
        return null;
    }

    @Override
    public Grade getGrade() {
        return Grade.createEmptyGrade();
    }

    @Override
    public DateTime getCreationDateDateTime() {
        return null;
    }

}
