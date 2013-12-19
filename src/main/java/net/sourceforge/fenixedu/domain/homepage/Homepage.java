package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import pt.utl.ist.fenix.tools.util.i18n.Language;
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

        setRootDomainObject(Bennu.getInstance());

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
    public IGroup getOwner() {
        return getPerson().getPersonGroup();
    }

    public static List<Homepage> getAllHomepages() {
        List<Homepage> result = new ArrayList<Homepage>();

        for (Content content : Bennu.getInstance().getContentsSet()) {
            if (content instanceof Homepage) {
                result.add((Homepage) content);
            }
        }
        return result;
    }

    @Override
    public List<IGroup> getContextualPermissionGroups() {
        List<IGroup> groups = super.getContextualPermissionGroups();
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
    protected void disconnect() {
        setPerson(null);
        super.disconnect();
    }

    public boolean isHomepageActivated() {
        return getActivated() != null && getActivated().booleanValue();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString().with(Language.pt, String.valueOf(getPerson().getIstUsername()));
    }

    @Override
    public void setNormalizedName(final MultiLanguageString normalizedName) {
        // unable to optimize because we cannot track changes to name correctly.
        // don't call super.setNormalizedName() !
    }

    @Deprecated
    public boolean hasResearchUnitHomepage() {
        return getResearchUnitHomepage() != null;
    }

    @Deprecated
    public boolean hasShowInterests() {
        return getShowInterests() != null;
    }

    @Deprecated
    public boolean hasShowPublications() {
        return getShowPublications() != null;
    }

    @Deprecated
    public boolean hasShowWorkTelephone() {
        return getShowWorkTelephone() != null;
    }

    @Deprecated
    public boolean hasShowCategory() {
        return getShowCategory() != null;
    }

    @Deprecated
    public boolean hasActivated() {
        return getActivated() != null;
    }

    @Deprecated
    public boolean hasShowPhoto() {
        return getShowPhoto() != null;
    }

    @Deprecated
    public boolean hasShowResearchUnitHomepage() {
        return getShowResearchUnitHomepage() != null;
    }

    @Deprecated
    public boolean hasShowPrizes() {
        return getShowPrizes() != null;
    }

    @Deprecated
    public boolean hasShowUnit() {
        return getShowUnit() != null;
    }

    @Deprecated
    public boolean hasShowTelephone() {
        return getShowTelephone() != null;
    }

    @Deprecated
    public boolean hasShowPatents() {
        return getShowPatents() != null;
    }

    @Deprecated
    public boolean hasShowAlumniDegrees() {
        return getShowAlumniDegrees() != null;
    }

    @Deprecated
    public boolean hasShowCurrentAttendingExecutionCourses() {
        return getShowCurrentAttendingExecutionCourses() != null;
    }

    @Deprecated
    public boolean hasShowAlternativeHomepage() {
        return getShowAlternativeHomepage() != null;
    }

    @Deprecated
    public boolean hasShowCurrentExecutionCourses() {
        return getShowCurrentExecutionCourses() != null;
    }

    @Deprecated
    public boolean hasShowEmail() {
        return getShowEmail() != null;
    }

    @Deprecated
    public boolean hasShowMobileTelephone() {
        return getShowMobileTelephone() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasShowActiveStudentCurricularPlans() {
        return getShowActiveStudentCurricularPlans() != null;
    }

    @Deprecated
    public boolean hasShowParticipations() {
        return getShowParticipations() != null;
    }

    @Deprecated
    public boolean hasResearchUnit() {
        return getResearchUnit() != null;
    }

}
