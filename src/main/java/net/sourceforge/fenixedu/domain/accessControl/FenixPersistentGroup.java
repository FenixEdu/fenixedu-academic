package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.util.BundleUtil;

public abstract class FenixPersistentGroup extends FenixPersistentGroup_Base {
    public FenixPersistentGroup() {
        super();
    }

    @Override
    public String getPresentationName() {
        final String name =
                BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), getPresentationNameKey(),
                        getPresentationNameKeyArgs());
        return name != null ? name : expression();
    }

    public String getPresentationNameBundle() {
        return "resources.GroupNameResources";
    }

    public String getPresentationNameKey() {
        return "label.name." + getClass().getSimpleName();
    }

    public String[] getPresentationNameKeyArgs() {
        return new String[0];
    }
}
