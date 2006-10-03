package net.sourceforge.fenixedu.domain.tests;

import java.io.Serializable;

public class Grade implements Serializable {

	double value;

	double scale;

	public Grade() {
		super();
	}

	public Grade(double value, double scale) {
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

	public Grade add(Grade gradeToAdd) {
		double convertedValue = gradeToAdd.getValue() * this.getScale() / gradeToAdd.getScale();

		return new Grade(convertedValue + this.getValue(), this.getScale());
	}

	public Grade multiply(Double factor) {
		return new Grade(factor * this.getValue(), this.getScale());
	}
}
