package net.sourceforge.fenixedu.domain.cms;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class TemplatedSection extends TemplatedSection_Base {

    public TemplatedSection(SiteTemplate template, MultiLanguageString name, String customPath, boolean visibleInMenus) {
        setTemplate(template);
        setCustomPath(customPath);
        setName(name);
        setOrder(template.getTemplatedSectionSet().size());
        setVisible(visibleInMenus);
    }

    @Override
    public String getCustomPath() {
        return super.getCustomPath();
    }

    @Override
    public void delete() {
        setTemplate(null);
        for (TemplatedSectionInstance instance : getInstanceSet()) {
            instance.delete();
        }
        super.delete();
    }

}
