package net.sourceforge.fenixedu.domain.research;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.domain.Bennu;

public class Researcher extends Researcher_Base {
    public Researcher(Person person) {
        super();
        setPerson(person);
        setAllowsToBeSearched(Boolean.FALSE);
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    private String normalizeKeywords(String keywordList) {
        String[] keys = keywordList.split(",");

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String[] dtd = key.split(" ");

            for (String eee : dtd) {
                if (eee.trim().length() > 0) {
                    sb.append(eee.trim()).append(" ");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(",");
        }

        return sb.substring(0, sb.length() - 1);
    }

    public boolean isActiveContractedResearcher() {
        PersonContractSituation currentResearcherContractSituation = getCurrentContractedResearcherContractSituation();
        return currentResearcherContractSituation != null;
    }

    public PersonContractSituation getCurrentContractedResearcherContractSituation() {
        return getPerson().getPersonProfessionalData() != null ? getPerson().getPersonProfessionalData()
                .getCurrentPersonContractSituationByCategoryType(CategoryType.RESEARCHER) : null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasAllowsToBeSearched() {
        return getAllowsToBeSearched() != null;
    }

}
