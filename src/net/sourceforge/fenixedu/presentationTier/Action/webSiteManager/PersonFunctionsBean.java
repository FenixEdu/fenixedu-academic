package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class PersonFunctionsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Person> person;
    private DomainReference<Unit> unit;
    private DomainListReference<Function> functions;

    public PersonFunctionsBean(Person person, Unit unit) {
	super();

	this.person = new DomainReference<Person>(person);
	this.unit = new DomainReference<Unit>(unit);

	initFunctions(person);
    }

    private void initFunctions(Person person) {
	this.functions = new DomainListReference<Function>();
	for (PersonFunction pf : person.getAllActivePersonFunctions(getUnit())) {
	    this.functions.add(pf.getFunction());
	}

	Collections.sort(this.functions, FUNCTION_COMPARATOR);
    }

    public Person getPerson() {
	return this.person.getObject();
    }

    public Unit getUnit() {
	return this.unit.getObject();
    }

    public List<Function> getFunctions() {
	return this.functions;
    }

    public void setFunctions(List<Function> functions) {
	this.functions = new DomainListReference<Function>(functions);
    }

    private static Comparator<Function> FUNCTION_COMPARATOR = new Comparator<Function>() {

	public int compare(Function f1, Function f2) {
	    Integer level1 = getUnitLevel(f1.getUnit());
	    Integer level2 = getUnitLevel(f2.getUnit());

	    int comparison = level1.compareTo(level2);
	    if (comparison != 0) {
		return comparison;
	    } else {
		Integer order1 = getOrder(f1);
		Integer order2 = getOrder(f2);

		comparison = order1.compareTo(order2);
		if (comparison != 0) {
		    return comparison;
		} else {
		    return f1.getIdInternal().compareTo(f2.getIdInternal());
		}
	    }
	}

	private Integer getUnitLevel(Unit unit) {
	    return unit.getUnitDepth();
	}

	private Integer getOrder(Function f1) {
	    Integer order = f1.getFunctionOrder();
	    return order == null ? Integer.MAX_VALUE : order;
	}
    };

}
