package net.sourceforge.fenixedu.domain.degreeStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public class EctsComparabilityPercentages implements Serializable {
    private static final long serialVersionUID = 7682260143153322085L;

    private final double[] percentages;

    public EctsComparabilityPercentages(double[] percentages) {
	this.percentages = percentages;
    }

    public EctsComparabilityPercentages(String[] percentages) {
	this.percentages = extractPercentages(percentages);
    }

    private double[] extractPercentages(String[] percentages) {
	double[] perc = new double[11];
	for (int i = 0; i < perc.length; i++) {
	    perc[i] = Double.parseDouble(percentages[i]);
	}
	return perc;
    }

    public double getPercentage(int grade) {
	if (grade < 10 || grade > 20)
	    throw new DomainException("error.degreeStructure.converting.grade.not.in.approval.range");
	return percentages[grade - 10];
    }

    public String getPrintableFormat() {
	return toString();
    }

    @Override
    public String toString() {
	List<String> percents = new ArrayList<String>();
	for (double percentage : percentages) {
	    percents.add(Double.toString(percentage));
	}
	return StringUtils.join(percents, ":");
    }

    public static EctsComparabilityPercentages fromString(String serialized) {
	return new EctsComparabilityPercentages(serialized.split(":"));
    }
}
