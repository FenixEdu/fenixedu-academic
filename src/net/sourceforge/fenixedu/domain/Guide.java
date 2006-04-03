package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

    public Guide() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public GuideSituation getActiveSituation() {
        if (this.getGuideSituations() != null) {
            Iterator iterator = this.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                GuideSituation guideSituation = (GuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    return guideSituation;
                }
            }
        }
        return null;
    }

    public void updateTotalValue() {

        double total = 0;

        for (GuideEntry guideEntry : getGuideEntries()) {
            total += guideEntry.getPrice() * guideEntry.getQuantity();
        }

        setTotal(NumberUtils.formatNumber(total, 2));

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

    public static Integer generateGuideNumber(Integer year) {
        Integer guideNumber = Integer.valueOf(0);
        List<Guide> guides = new ArrayList();
        for (Guide guide : RootDomainObject.getInstance().getGuides()) {
            if (guide.getYear().equals(year)) {
                guides.add(guide);
            }
        }
        if (!guides.isEmpty()) {
            Collections.sort(guides, new BeanComparator("number"));
            guideNumber = guides.get(0).getNumber();
        }
        return Integer.valueOf(guideNumber.intValue() + 1);
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
        List<Guide> guides = new ArrayList();
        for (Guide guide : RootDomainObject.getInstance().getGuides()) {
            if (guide.getYear().equals(year) && guide.getNumber().equals(number)) {
                guides.add(guide);
            }
        }
        Collections.sort(guides, new ReverseComparator(new BeanComparator("version")));
        return guides;
    }

    public static List<Guide> readByYear(Integer year) {
        List<Guide> guides = new ArrayList();
        for (Guide guide : RootDomainObject.getInstance().getGuides()) {
            if (guide.getYear().equals(year)) {
                guides.add(guide);
            }
        }
        return guides;
    }

    public static List<Guide> readByYearAndState(Integer guideYear, GuideState situationOfGuide) {
        Set<Guide> guides = new HashSet();
        boolean toInsert = false;
        for (Guide guide : RootDomainObject.getInstance().getGuides()) {
            if (guideYear != null && guide.getYear().equals(guideYear)) {
                toInsert = true;
            }
            for (GuideSituation guideSituation : guide.getGuideSituations()) {
                if (guideSituation.getState().equals(State.ACTIVE)
                        && guideSituation.getSituation().equals(situationOfGuide)) {
                    toInsert = true;
                }
            }
            if (toInsert) {
                guides.add(guide);
                toInsert = false;
            }            
        }
        return new ArrayList(guides);
    }
}
