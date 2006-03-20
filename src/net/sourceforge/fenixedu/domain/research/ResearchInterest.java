package net.sourceforge.fenixedu.domain.research;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;


public class ResearchInterest extends ResearchInterest_Base {

    static {
        PartyInterest.addListener(new ResearchInterestListener());
    }
    
    public ResearchInterest() {
        super();
    }
    
    public void delete() {
        removeParty();
        
        deleteDomainObject();
    }
    
    private static class ResearchInterestListener extends dml.runtime.RelationAdapter<ResearchInterest, Party> {
        
        /*
         * This method is responsible for, after removing a ResearchInterest from a Party, having all 
         * the others researchInterests associated with the same party have their order rearranged.
         * @param publicationAuthorship the authorship being removed from the publication
         * @param publication the publication from whom the authorship will be removed
         * @see relations.PublicationAuthorship_Base#remove(net.sourceforge.fenixedu.domain.publication.Authorship, net.sourceforge.fenixedu.domain.publication.Publication)
         */
            @Override
            public void afterRemove(ResearchInterest removedResearchInterest, Party party) {
                if ((removedResearchInterest != null) && (party != null)) {
                    int removedOrder = removedResearchInterest.getOrder();
                    for(ResearchInterest researchInterest : party.getResearchInterests()) {
                        if (researchInterest.getOrder() > removedOrder) {
                            researchInterest.setOrder(researchInterest.getOrder()-1);
                        }
                    }
                }
            }
    }
}
