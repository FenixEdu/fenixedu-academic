package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.beanutils.BeanComparator;

public class Homepage extends Homepage_Base {

    public static final Comparator HOMEPAGE_COMPARATOR_BY_NAME = new BeanComparator("name", Collator.getInstance());

    public Homepage() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public String getName() {
        return getPerson().getNickname();
    }

    public void setName(String name) {
        getPerson().setNickname(name);
    }

    @Override
    public IGroup getOwner() {
        return null;
        //return getPerson().getPersonGroup();
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
}
