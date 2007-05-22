package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Comparator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public enum CycleType {

    FIRST_CYCLE(1),

    SECOND_CYCLE(2),

    THIRD_CYCLE(3);

    public static final Comparator<CycleType> CYCLE_TYPE_COMPARATOR = new Comparator<CycleType>() {
	public int compare(CycleType o1, CycleType o2) {
	    return o1.getWeight().compareTo(o2.getWeight());
	}
    };

    private Integer weight;

    private CycleType(Integer weight) {
	this.weight = weight;
    }

    public Integer getWeight() {
	return weight;
    }

    public String getQualifiedName() {
	return this.getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
	return ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale())
		.getString(name());
    }

}
