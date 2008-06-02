package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemption;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExemptionShift;
import net.sourceforge.fenixedu.domain.assiduousness.util.PartialList;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class AssiduousnessExemptionBean implements Serializable, FactoryExecutor {
    private Integer year;

    private String description;

    private List<AssiduousnessExemptionShiftBean> assiduousnessExemptionShifts;

    private DomainReference<AssiduousnessExemption> assiduousnessExemption;

    public class AssiduousnessExemptionShiftBean implements Serializable {
	private YearMonthDay firstShiftYearMonthDay;
	private YearMonthDay secondShiftYearMonthDay;

	public YearMonthDay getFirstShiftYearMonthDay() {
	    return firstShiftYearMonthDay;
	}

	public void setFirstShiftYearMonthDay(YearMonthDay firstShiftYearMonthDay) {
	    this.firstShiftYearMonthDay = firstShiftYearMonthDay;
	}

	public YearMonthDay getSecondShiftYearMonthDay() {
	    return secondShiftYearMonthDay;
	}

	public void setSecondShiftYearMonthDay(YearMonthDay secondShiftYearMonthDay) {
	    this.secondShiftYearMonthDay = secondShiftYearMonthDay;
	}
    }

    public AssiduousnessExemptionBean() {
	super();
	assiduousnessExemptionShifts = new ArrayList<AssiduousnessExemptionShiftBean>();
	assiduousnessExemptionShifts.add(new AssiduousnessExemptionShiftBean());
	assiduousnessExemptionShifts.add(new AssiduousnessExemptionShiftBean());
    }

    public AssiduousnessExemptionBean(AssiduousnessExemption assiduousnessExemption) {
	super();
	setAssiduousnessExemption(assiduousnessExemption);
	assiduousnessExemptionShifts = new ArrayList<AssiduousnessExemptionShiftBean>();
	AssiduousnessExemptionShift assiduousnessExemptionShift = assiduousnessExemption.getAssiduousnessExemptionShifts().get(0);
	for (Partial partial : assiduousnessExemptionShift.getPartialShift().getPartials()) {
	    AssiduousnessExemptionShiftBean assiduousnessExemptionShiftBean = new AssiduousnessExemptionShiftBean();
	    assiduousnessExemptionShiftBean.setFirstShiftYearMonthDay(new YearMonthDay(partial));
	    assiduousnessExemptionShifts.add(assiduousnessExemptionShiftBean);
	}
	assiduousnessExemptionShift = assiduousnessExemption.getAssiduousnessExemptionShifts().get(1);
	for (int i = 0; i < assiduousnessExemptionShift.getPartialShift().getPartials().size(); i++) {
	    Partial partial = assiduousnessExemptionShift.getPartialShift().getPartials().get(i);
	    assiduousnessExemptionShifts.get(i).setSecondShiftYearMonthDay(new YearMonthDay(partial));
	}
	setYear(assiduousnessExemption.getYear());
	setDescription(assiduousnessExemption.getDescription());
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<AssiduousnessExemptionShiftBean> getAssiduousnessExemptionShifts() {
	return assiduousnessExemptionShifts;
    }

    public void setAssiduousnessExemptionShifts(List<AssiduousnessExemptionShiftBean> assiduousnessExemptionShifts) {
	this.assiduousnessExemptionShifts = assiduousnessExemptionShifts;
    }

    public Object execute() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	if (getYear() == null) {
	    return new ActionMessage("errors.required", bundle.getString("label.year"));
	}
	if (getDescription() == null || getDescription().length() == 0) {
	    return new ActionMessage("errors.required", bundle.getString("label.description"));
	}
	AssiduousnessExemption assiduousnessExemption = getAssiduousnessExemption();
	if (alreadyExistsAssiduousnessExemption(assiduousnessExemption)) {
	    return new ActionMessage("error.repeatedYearAndDescription");
	}

	PartialList firstPartialList = new PartialList();
	PartialList secondPartialList = new PartialList();
	for (AssiduousnessExemptionShiftBean assiduousnessExemptionShift : assiduousnessExemptionShifts) {
	    if (assiduousnessExemptionShift.getFirstShiftYearMonthDay() == null
		    || assiduousnessExemptionShift.getSecondShiftYearMonthDay() == null) {
		return new ActionMessage("error.emptyDates");
	    }

	    if ((assiduousnessExemptionShift.getFirstShiftYearMonthDay().getYear() != getYear() && assiduousnessExemptionShift
		    .getFirstShiftYearMonthDay().getYear() != getYear() + 1)
		    || (assiduousnessExemptionShift.getFirstShiftYearMonthDay().getYear() == getYear() + 1 && assiduousnessExemptionShift
			    .getFirstShiftYearMonthDay().getMonthOfYear() != 1)) {
		return new ActionMessage("error.assiduousnessExemption.wrongYear", new Object[] { getYear().toString(),
			new Integer(getYear() + 1).toString() });
	    }

	    if ((assiduousnessExemptionShift.getSecondShiftYearMonthDay().getYear() != getYear() && assiduousnessExemptionShift
		    .getSecondShiftYearMonthDay().getYear() != getYear() + 1)
		    || (assiduousnessExemptionShift.getSecondShiftYearMonthDay().getYear() == getYear() + 1 && assiduousnessExemptionShift
			    .getSecondShiftYearMonthDay().getMonthOfYear() != 1)) {
		return new ActionMessage("error.assiduousnessExemption.wrongYear", new Object[] { getYear().toString(),
			new Integer(getYear() + 1).toString() });
	    }

	    if (!repeatedDates(assiduousnessExemptionShift)) {
		firstPartialList.getPartials().add(new Partial(assiduousnessExemptionShift.getFirstShiftYearMonthDay()));
		secondPartialList.getPartials().add(new Partial(assiduousnessExemptionShift.getSecondShiftYearMonthDay()));
	    } else {
		return new ActionMessage("error.repeatedDates");
	    }
	}
	if (firstPartialList.getPartials().containsAll(secondPartialList.getPartials())) {
	    return new ActionMessage("error.repeatedShifts");
	}

	if (assiduousnessExemption == null) {
	    assiduousnessExemption = new AssiduousnessExemption(getYear(), getDescription());
	} else {
	    assiduousnessExemption.edit(getYear(), getDescription());
	    assiduousnessExemption.deleteShifts();
	}
	new AssiduousnessExemptionShift(assiduousnessExemption, firstPartialList);
	new AssiduousnessExemptionShift(assiduousnessExemption, secondPartialList);

	return null;
    }

    private boolean alreadyExistsAssiduousnessExemption(AssiduousnessExemption newAssiduousnessExemption) {
	for (AssiduousnessExemption assiduousnessExemption : RootDomainObject.getInstance().getAssiduousnessExemptions()) {
	    if (assiduousnessExemption.getYear().equals(getYear())
		    && assiduousnessExemption.getDescription().equals(getDescription())) {
		if (newAssiduousnessExemption == null
			|| (!newAssiduousnessExemption.getIdInternal().equals(assiduousnessExemption.getIdInternal()))) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean repeatedDates(AssiduousnessExemptionShiftBean shift) {
	for (AssiduousnessExemptionShiftBean assiduousnessExemptionShift : assiduousnessExemptionShifts) {
	    if (assiduousnessExemptionShift != shift
		    && (assiduousnessExemptionShift.getFirstShiftYearMonthDay().equals(shift.getFirstShiftYearMonthDay()) || assiduousnessExemptionShift
			    .getSecondShiftYearMonthDay().equals(shift.getSecondShiftYearMonthDay()))) {
		return true;
	    }
	}
	return false;
    }

    public boolean isAssiduousnessExemptionComplete() {
	return year != null && !StringUtils.isEmpty(description);
    }

    public void addAssiduousnessExemptionShifts() {
	assiduousnessExemptionShifts.add(new AssiduousnessExemptionShiftBean());
    }

    public void removeAssiduousnessExemptionShifts(Integer rowIndex) {
	assiduousnessExemptionShifts.remove(rowIndex.intValue());
    }

    public AssiduousnessExemption getAssiduousnessExemption() {
	return assiduousnessExemption == null ? null : assiduousnessExemption.getObject();
    }

    public void setAssiduousnessExemption(AssiduousnessExemption assiduousnessExemption) {
	this.assiduousnessExemption = new DomainReference<AssiduousnessExemption>(assiduousnessExemption);
    }

}
