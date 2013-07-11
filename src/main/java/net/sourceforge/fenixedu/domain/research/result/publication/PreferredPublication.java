package net.sourceforge.fenixedu.domain.research.result.publication;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PreferredPublication extends PreferredPublication_Base {
    public static class PreferredComparator implements Comparator<ResearchResultPublication> {
        private final Person person;

        public PreferredComparator(Person person) {
            this.person = person;
        }

        @Override
        public int compare(ResearchResultPublication p1, ResearchResultPublication p2) {
            if (p1.getPreferredLevel(person).compareTo(p2.getPreferredLevel(person)) != 0) {
                return -p1.getPreferredLevel(person).compareTo(p2.getPreferredLevel(person));
            }
            if (p1.getYear() != null && p2.getYear() == null) {
                return 1;
            } else if (p1.getYear() == null && p2.getYear() != null) {
                return -1;
            } else if (p1.getYear() != null && p2.getYear() != null && p1.getYear().compareTo(p2.getYear()) != 0) {
                return -p1.getYear().compareTo(p2.getYear());
            }
            int score1 = score(p1);
            int score2 = score(p2);
            if (score1 != score2) {
                return score1 > score2 ? -1 : 1;
            }
            return p1.getExternalId().compareTo(p2.getExternalId());
        }

        private int score(ResearchResultPublication publication) {
            if (publication instanceof Article) {
                if (((Article) publication).getScope() == ScopeType.INTERNATIONAL) {
                    return 6;
                }
                return 2;
            }
            if (publication instanceof Book) {
                return 5;
            }
            if (publication instanceof BookPart) {
                return 4;
            }
            if (publication instanceof Inproceedings) {
                if (((Inproceedings) publication).getScope() == ScopeType.INTERNATIONAL) {
                    return 3;
                }
                return 1;
            }
            return 0;
        }
    }

    public PreferredPublication(Person person, ResearchResultPublication preferredPublication,
            PreferredPublicationPriority priority) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPersonThatPrefers(person);
        setPreferredPublication(preferredPublication);
        setPriority(priority);
    }

    public void delete() {
        setPersonThatPrefers(null);
        setPreferredPublication(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }
}
