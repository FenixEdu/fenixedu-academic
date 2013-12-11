package net.sourceforge.fenixedu.domain.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ResearchInterest extends ResearchInterest_Base {
    public static class ResearchInterestComparator implements Comparator<ResearchInterest> {
        @Override
        public int compare(ResearchInterest o1, ResearchInterest o2) {
            return o1.getInterestOrder().compareTo(o2.getInterestOrder());
        }
    }

    static {
        getRelationPartyInterest().addListener(new ResearchInterestListener());
    }

    public ResearchInterest() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setParty(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void addTranslation(ResearchInterestTranslation translation) {
        setInterest(getInterest().with(translation.getLanguage(), translation.getInterest()));
    }

    public void removeTranslation(Language language) {
        if (!this.getInterest().hasLanguage(language)) {
            throw new DomainException("errors.researchInterest.inexistantTranslation");
        }
        if (this.getInterest().getAllLanguages().size() == 1) {
            throw new DomainException("errors.researchInterest.lastTranslation");
        }
        this.setInterest(getInterest().without(language));
    }

    public List<ResearchInterestTranslation> getAllTranslations() {
        List<ResearchInterestTranslation> result = new ArrayList<ResearchInterestTranslation>();
        for (Language language : this.getInterest().getAllLanguages()) {
            result.add(this.getTranslation(language));
        }
        return result;
    }

    /**
     * This method is responsible for creating the logic of what is a
     * translation If the researchInterest hasn't got a given attribute in the
     * wanted language a domainException is thrown.
     * 
     * @param language
     *            the language we want the translation
     * @return the researchInteresttranslation in the given language
     */
    public ResearchInterestTranslation getTranslation(Language language) {

        ResearchInterestTranslation translation = new ResearchInterestTranslation(language);
        if (this.getInterest().hasLanguage(language)) {
            translation.setInterest(this.getInterest().getContent(language));
        } else {
            throw new DomainException("errors.researchInterest.inexistantTranslation");
        }
        return translation;
    }

    public static class ResearchInterestTranslation implements Serializable {
        private Language language;

        private String interest;

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public ResearchInterestTranslation() {
            interest = new String();
        }

        public ResearchInterestTranslation(Language language) {
            this.language = language;
            interest = new String();
        }
    }

    private static class ResearchInterestListener extends RelationAdapter<ResearchInterest, Party> {

        /*
         * This method is responsible for, after removing a ResearchInterest
         * from a Party, having all the others researchInterests associated with
         * the same party have their order rearranged. @param
         * publicationAuthorship the authorship being removed from the
         * publication @param publication the publication from whom the
         * authorship will be removed
         * 
         * @see
         * relations.PublicationAuthorship_Base#remove(net.sourceforge.fenixedu
         * .domain.research.result.Authorship,
         * net.sourceforge.fenixedu.domain.research.result.Publication)
         */
        @Override
        public void afterRemove(ResearchInterest removedResearchInterest, Party party) {
            if ((removedResearchInterest != null) && (party != null)) {
                int removedOrder = removedResearchInterest.getOrder();
                for (ResearchInterest researchInterest : party.getResearchInterests()) {
                    if (researchInterest.getOrder() > removedOrder) {
                        researchInterest.setOrder(researchInterest.getOrder() - 1);
                    }
                }
            }
        }
    }

    @Deprecated
    public Integer getOrder() {
        return super.getInterestOrder();
    }

    @Deprecated
    public void setOrder(Integer order) {
        super.setInterestOrder(order);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInterest() {
        return getInterest() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasInterestOrder() {
        return getInterestOrder() != null;
    }

}
