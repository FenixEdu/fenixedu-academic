package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.TreeSet;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum CycleType {

    FIRST_CYCLE(1, 180d),

    SECOND_CYCLE(2, 120d),

    THIRD_CYCLE(3, 300d);

    public static final Comparator<CycleType> CYCLE_TYPE_COMPARATOR = new Comparator<CycleType>() {
	public int compare(CycleType o1, CycleType o2) {
	    return o1.getWeight().compareTo(o2.getWeight());
	}
    };

    private Integer weight;
    
    private Double ectsCredits;

    private CycleType(Integer weight, Double ectsCredits) {
	this.weight = weight;
	this.ectsCredits = ectsCredits;
    }

    public Integer getWeight() {
	return weight;
    }

    public Double getDefaultEcts() {
	return ectsCredits;
    }
    
    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale()).getString(name());
    }

    static final public Collection<CycleType> getSortedValues() {
	final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.CYCLE_TYPE_COMPARATOR);
	result.addAll(Arrays.asList(values()));
	return result;
    }
}
