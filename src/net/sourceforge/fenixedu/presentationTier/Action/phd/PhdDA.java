package net.sourceforge.fenixedu.presentationTier.Action.phd;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

public class PhdDA extends FenixDispatchAction {

    protected void addErrorMessage(HttpServletRequest request, String key, String... args) {
        addActionMessage("error", request, key, args);
    }

    protected void addSuccessMessage(HttpServletRequest request, String key, String... args) {
        addActionMessage("success", request, key, args);
    }

    protected void addWarningMessage(HttpServletRequest request, String key, String... args) {
        addActionMessage("warning", request, key, args);
    }

}
