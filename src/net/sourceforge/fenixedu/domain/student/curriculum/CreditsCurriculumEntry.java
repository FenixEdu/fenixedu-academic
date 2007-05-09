package net.sourceforge.fenixedu.domain.student.curriculum;


public abstract class CreditsCurriculumEntry extends CurriculumEntry {

    @Override
    public double getWeigth() {
	throw new RuntimeException();
    }

    @Override
    public Double getWeigthTimesClassification() {
	return null;
    }

}
