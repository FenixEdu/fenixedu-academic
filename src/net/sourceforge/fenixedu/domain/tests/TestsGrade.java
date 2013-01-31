package net.sourceforge.fenixedu.domain.tests;

import java.io.Serializable;

public class TestsGrade implements Serializable {

	private final double value;

	private final double scale;

	public TestsGrade(double value, double scale) {
		this.value = value;
		this.scale = scale;
	}

	public double getScale() {
		return scale;
	}

	public double getValue() {
		return value;
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
