package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.State;

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
    
    public void updateTotalValue(){
        
        double total = 0;

        for (GuideEntry guideEntry : getGuideEntries()) {
            total += guideEntry.getPrice() * guideEntry.getQuantity();
        }
        
        setTotal(NumberUtils.formatNumber(total, 2));
        
    }

    public GuideEntry getEntry(GraduationType graduationType, DocumentType documentType, String description) {
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

}
