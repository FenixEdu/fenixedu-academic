package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.TreeSet;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum CycleType {

    FIRST_CYCLE(1, 180d),

    SECOND_CYCLE(2, 120d, FIRST_CYCLE),

    THIRD_CYCLE(3, 300d);

    static final public Comparator<CycleType> COMPARATOR_BY_LESS_WEIGHT = new Comparator<CycleType>() {
	public int compare(CycleType o1, CycleType o2) {
	    return o1.getWeight().compareTo(o2.getWeight());
	}
    };

    static final public Comparator<CycleType> COMPARATOR_BY_GREATER_WEIGHT = new Comparator<CycleType>() {
	public int compare(CycleType o1, CycleType o2) {
	    return -COMPARATOR_BY_LESS_WEIGHT.compare(o1, o2);
	}
    };

    private Integer weight;

    private Double ectsCredits;

    private CycleType sourceCycleAffinity;

    private CycleType(Integer weight, Double ectsCredits) {
	this(weight, ectsCredits, null);
    }

    private CycleType(Integer weight, Double ectsCredits, CycleType sourceCycleAffinity) {
	this.weight = weight;
	this.ectsCredits = ectsCredits;
	this.sourceCycleAffinity = sourceCycleAffinity;
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
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(getQualifiedName());
    }

    static final public Collection<CycleType> getSortedValues() {
	final Collection<CycleType> result = new TreeSet<CycleType>(CycleType.COMPARATOR_BY_LESS_WEIGHT);
	result.addAll(Arrays.asList(values()));
	return result;
    }

    public boolean isBeforeOrEquals(final CycleType cycleType) {
	return COMPARATOR_BY_LESS_WEIGHT.compare(this, cycleType) <= 0;
    }

    public boolean hasSourceCycleAffinity() {
	return this.sourceCycleAffinity != null;
    }

    public CycleType getSourceCycleAffinity() {
	return this.sourceCycleAffinity;
    }

    public CycleType getNext() {
	final Iterator<CycleType> iterator = getSortedValues().iterator();

	for (CycleType cycleType = iterator.next(); iterator.hasNext(); cycleType = iterator.next()) {
	    if (cycleType == this) {
		return iterator.next();
	    }

	    continue;
	}

	return null;
    }

    public boolean hasNext() {
	return getNext() != null;
    }

    public CycleType getPrevious() {
	final List<CycleType> sortedValues = new ArrayList<CycleType>(getSortedValues());
	final ListIterator<CycleType> listIterator = sortedValues.listIterator(sortedValues.size() + 1);

	for (CycleType cycleType = listIterator.previous(); listIterator.hasPrevious(); cycleType = listIterator.previous()) {
	    if (cycleType == this) {
		return listIterator.previous();
	    }

	    continue;
	}

	return null;
    }

    public boolean hasPrevious() {
	return getPrevious() != null;
    }

}
