package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.beanutils.BeanComparator;

public class Homepage extends Homepage_Base {

    public static final long MB = 1024 * 1024;
    
    public static final long REGULAR_QUOTA = 10*MB;
    public static final long TEACHER_QUOTA = 50*MB;
    
    public static final Comparator HOMEPAGE_COMPARATOR_BY_NAME = new BeanComparator("name", Collator.getInstance());

    public Homepage() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
        
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

    public String getName() {
        return getPerson().getNickname();
    }

    public void setName(String name) {
        getPerson().setNickname(name);
    }

    @Override
    public IGroup getOwner() {
        return getPerson().getPersonGroup();
    }

    public static List<Homepage> getAllHomepages() {
        List<Homepage> result = new ArrayList<Homepage>();
        
        for (Site site : RootDomainObject.getInstance().getSites()) {
            if (site instanceof Homepage) {
                result.add((Homepage) site);
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
        Person person = getPerson();

        if (person.hasTeacher()) {
            return TEACHER_QUOTA;
        }
        else {
            return REGULAR_QUOTA;
        }
    }

    @Override
    protected void deleteRelations() {
        super.deleteRelations();
        
        removePerson()
    }
    
}
