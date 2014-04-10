package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.fenixedu.bennu.core.groups.CustomGroup;

public abstract class FenixGroup extends CustomGroup {
    private static final long serialVersionUID = 4626331181392986508L;

    @Override
    public String getPresentationName() {
        final String name =
                BundleUtil.getStringFromResourceBundle(getPresentationNameBundle(), getPresentationNameKey(),
                        getPresentationNameKeyArgs());
        return name != null ? name : getExpression();
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
