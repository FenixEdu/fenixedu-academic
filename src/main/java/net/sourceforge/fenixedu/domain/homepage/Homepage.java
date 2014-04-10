package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.Group;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Homepage extends Homepage_Base {

    public static final long MB = 1024 * 1024;

    public static final long REGULAR_QUOTA = 10 * MB;
    public static final long TEACHER_QUOTA = 200 * MB;

    public static final Comparator<Homepage> HOMEPAGE_COMPARATOR_BY_NAME = new Comparator<Homepage>() {

        @Override
        public int compare(Homepage o1, Homepage o2) {
            return Collator.getInstance().compare(o1.getName(), o2.getName());
        }

    };

    public Homepage() {
        super();

        setActivated(false);
        setShowUnit(false);
        setShowCategory(false);
        setShowPhoto(false);
        setShowEmail(false);
        setShowTelephone(false);
        setShowWorkTelephone(false);
        setShowMobileTelephone(false);
        setShowAlternativeHomepage(false);
        setShowResearchUnitHomepage(false);
        setShowCurrentExecutionCourses(false);
        setShowActiveStudentCurricularPlans(false);
        setShowAlumniDegrees(false);
        setShowPublications(false);
        setShowPatents(false);
        setShowInterests(false);
        setShowCurrentAttendingExecutionCourses(false);
    }

    public Homepage(Person person) {
        this();

        setPerson(person);
    }

    public String getOwnersName() {
        return getPerson().getNickname();
    }

    public void setOwnersName(String name) {
        getPerson().setNickname(name);
    }

    @Override
    public Group getOwner() {
        return getPerson().getPersonGroup();
    }

    public static List<Homepage> getAllHomepages() {
        List<Homepage> result = new ArrayList<Homepage>();

        for (Site content : Bennu.getInstance().getSiteSet()) {
            if (content instanceof Homepage) {
                result.add((Homepage) content);
            }
        }
        return result;
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = super.getContextualPermissionGroups();
        groups.add(getPerson().getPersonGroup());

        return groups;
    }

    @Override
    public boolean hasQuota() {
        return true;
    }

    @Override
    public long getQuota() {
        final Person person = getPerson();
        return person.hasTeacher() ? TEACHER_QUOTA : REGULAR_QUOTA;
    }

    @Override
    public void delete() {
        setPerson(null);
        super.delete();
    }

    public boolean isHomepageActivated() {
        return getActivated() != null && getActivated().booleanValue();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString().with(MultiLanguageString.pt, String.valueOf(getPerson().getIstUsername()));
    }

    @Override
    public String getReversePath() {
        return super.getReversePath() + "/" + getPerson().getUsername();
    }

}
