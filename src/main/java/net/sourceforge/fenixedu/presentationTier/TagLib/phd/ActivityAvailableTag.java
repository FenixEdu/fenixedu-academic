package net.sourceforge.fenixedu.presentationTier.TagLib.phd;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.taglib.logic.ConditionalTagBase;

import pt.ist.bennu.core.security.Authenticate;

public class ActivityAvailableTag extends ConditionalTagBase {

    static private final long serialVersionUID = 1L;

    private Process process;
    private Class<? extends Activity> activity;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setProcess(Object object) {
        setProcess((Process) object);
    }

    public Class<? extends Activity> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = (Class<? extends Activity>) activity;
    }

    @Override
    protected boolean condition() throws JspException {
        final Activity activity = getProcess().getActivity(getActivity());

        if (activity == null) {
            throw new JspException("ActivityAvailableTag: activity not found");
        }

        try {
            activity.checkPreConditions(getProcess(), Authenticate.getUser());
            return true;
        } catch (final PreConditionNotValidException e) {
            return false;
        }
    }
}
