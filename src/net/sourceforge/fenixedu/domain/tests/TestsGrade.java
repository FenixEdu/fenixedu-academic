package net.sourceforge.fenixedu.domain.tests;

import java.io.Serializable;

public class TestsGrade implements Serializable {

    double value;

    double scale;

    public TestsGrade() {
	super();
    }

    public TestsGrade(double value, double scale) {
	this();

	this.setValue(value);
	this.setScale(scale);
    }

    public double getScale() {
	return scale;
    }

    private void setScale(double scale) {
	this.scale = scale;
    }

    public double getValue() {
	return value;
    }

    private void setValue(double value) {
	this.value = value;
    }

    public double getPercentage() {
	return this.getValue() / this.getScale() * 100;
    }

    public TestsGrade add(TestsGrade gradeToAdd) {
	double convertedValue = gradeToAdd.getValue() * this.getScale() / gradeToAdd.getScale();

	return new TestsGrade(convertedValue + this.getValue(), this.getScale());
    }

    public TestsGrade multiply(Double factor) {
	return new TestsGrade(factor * this.getValue(), this.getScale());
    }
}
