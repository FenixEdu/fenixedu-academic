package net.sourceforge.fenixedu.domain.homepage;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;

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

}
