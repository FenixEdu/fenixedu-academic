package org.fenixedu.academic.domain.accessControl;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class FenixGroupStrategy extends GroupStrategy {
    private static final long serialVersionUID = 4080161353335180912L;

    @Override
    public String getPresentationName() {
        final String name =
                BundleUtil.getString(getPresentationNameBundle(), getPresentationNameKey(), getPresentationNameKeyArgs());
        return name != null ? name : getExpression();
    }

    public String getPresentationNameBundle() {
        return Bundle.GROUP;
    }

    public String getPresentationNameKey() {
        return "label.name." + getClass().getSimpleName();
    }

    public String[] getPresentationNameKeyArgs() {
        return new String[0];
    }
}
