package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TemplatedSectionInstance extends TemplatedSectionInstance_Base {

    public TemplatedSectionInstance(TemplatedSection template, Site site) {
        super();
        setSite(site);
        setSectionTemplate(template);
        setOrder(site.getAssociatedSectionSet().size());
    }

    public TemplatedSectionInstance(TemplatedSection template, Section parent) {
        super();
        setParent(parent);
        setSectionTemplate(template);
        setOrder(parent.getChildSet().size());
    }

    @Override
    public MultiLanguageString getName() {
        return getSectionTemplate().getName();
    }

    @Override
    public void setName(MultiLanguageString name) {
        throw new UnsupportedOperationException("Cannot edit templated section name!");
    }

    @Override
    public void delete() {
        setSectionTemplate(null);
        super.delete();
    }

}
