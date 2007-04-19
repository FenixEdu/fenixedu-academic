package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

    public Guide() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    deleteDomainObject();
	} else {
	    throw new DomainException("guide.cannot.be.deleted");
	}
    }

    public boolean canBeDeleted() {
	return !(hasAnyGuideEntries() || hasAnyGuideSituations() || (getVersion() == 1));
    }

    public final static Comparator<Guide> yearAndNumberComparator = new Comparator<Guide>() {
	public int compare(Guide g1, Guide g2) {
	    Integer yearComparation = g1.getYear().compareTo(g2.getYear());
	    if (yearComparation == 0) {
		return g1.getNumber().compareTo(g2.getNumber());
	    }
	    return yearComparation;
	}
    };

    public GuideSituation getActiveSituation() {
	if (this.getGuideSituations() != null) {
	    Iterator iterator = this.getGuideSituations().iterator();
	    while (iterator.hasNext()) {
		GuideSituation guideSituation = (GuideSituation) iterator.next();
		if (guideSituation.getState().getState().equals(State.ACTIVE)) {
		    return guideSituation;
		}
	    }
	}
	return null;
    }

    public void updateTotalValue() {

	BigDecimal total = BigDecimal.ZERO;

	for (final GuideEntry guideEntry : getGuideEntries()) {
	    total = total.add(guideEntry.getPriceBigDecimal().multiply(
		    BigDecimal.valueOf(guideEntry.getQuantity())));
	}

	total.setScale(2, RoundingMode.HALF_EVEN);

	setTotalBigDecimal(total);

    }

    public GuideEntry getEntry(GraduationType graduationType, DocumentType documentType,
	    String description) {
	for (GuideEntry entry : getGuideEntries()) {
	    if (graduationType == null || !graduationType.equals(entry.getGraduationType())) {
		continue;
	    }

	    if (documentType == null || !documentType.equals(entry.getDocumentType())) {
		continue;
	    }

	    if (description == null || !description.equals(entry.getDescription())) {
		continue;
	    }

	    return entry;
	}

	return null;
    }

    public static Integer generateGuideNumber() {
	return Collections
		.max(RootDomainObject.getInstance().getGuides(), Guide.yearAndNumberComparator)
		.getNumber() + 1;

    }

    public static Guide readByNumberAndYearAndVersion(Integer number, Integer year, Integer version) {
	for (Guide guide : RootDomainObject.getInstance().getGuides()) {
	    if (guide.getNumber().equals(number) && guide.getYear().equals(year)
		    && guide.getVersion().equals(version)) {
		return guide;
	    }
	}
	return null;
    }

    public static List<Guide> readByNumberAndYear(Integer number, Integer year) {
	List<Guide> guides = new ArrayList<Guide>();
	for (Guide guide : RootDomainObject.getInstance().getGuides()) {
	    if (guide.getYear().equals(year) && guide.getNumber().equals(number)) {
		guides.add(guide);
	    }
	}
	Collections.sort(guides, new BeanComparator("version"));
	return guides;
    }

    public static List<Guide> readByYear(Integer year) {
	List<Guide> guides = new ArrayList<Guide>();
	for (Guide guide : RootDomainObject.getInstance().getGuides()) {
	    if (guide.getYear().equals(year)) {
		guides.add(guide);
	    }
	}
	return guides;
    }

    public static List<Guide> readByYearAndState(Integer guideYear, GuideState situationOfGuide) {

	List<Guide> result = new ArrayList<Guide>();
	for (Guide guide : RootDomainObject.getInstance().getGuides()) {
	    GuideSituation activeSituation = guide.getActiveSituation();
	    if (activeSituation != null && activeSituation.getSituation().equals(situationOfGuide)) {
		if (guideYear == null || (guideYear != null && guide.getYear().equals(guideYear))) {
		    result.add(guide);
		}
	    }
	}
	return result;

    }

    @Deprecated
    public void setTotal(Double total) {
	if(total != null){
	    setTotalBigDecimal(BigDecimal.valueOf(total));
	}
	else{
	    setTotalBigDecimal(null);
	}
    }

    @Deprecated
    public Double getTotal() {
	return getTotalBigDecimal().doubleValue();
    }

}
