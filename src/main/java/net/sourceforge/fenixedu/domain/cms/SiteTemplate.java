package net.sourceforge.fenixedu.domain.cms;

import java.util.List;

import net.sourceforge.fenixedu.domain.Site;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

public class SiteTemplate extends SiteTemplate_Base {

    public SiteTemplate(SiteTemplateController controller) {
        super();
        setController(controller);
        setBennu(Bennu.getInstance());
    }

    public static SiteTemplate getTemplateForSite(Site site) {
        for (SiteTemplate template : Bennu.getInstance().getSiteTemplateSet()) {
            if (template.getController().getControlledClass().equals(site.getClass())) {
                return template;
            }
        }
        return null;
    }

    public List<TemplatedSection> getOrderedSections() {
        return FluentIterable.from(getTemplatedSectionSet()).filter(new Predicate<TemplatedSection>() {
            @Override
            public boolean apply(TemplatedSection input) {
                return input.getVisible();
            }
        }).toSortedList(Ordering.natural());
    }

}
