package net.sourceforge.fenixedu.presentationTier.TagLib.phd;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;

import org.apache.struts.taglib.logic.ConditionalTagBase;

public class AccessTypeAvailableTag extends ConditionalTagBase {

    static private final long serialVersionUID = 1L;

    private PhdIndividualProgramProcess mainProcess;
    private PhdProcessAccessType accessType;

    public PhdIndividualProgramProcess getMainProcess() {
        return mainProcess;
    }

    public void setMainProcess(PhdIndividualProgramProcess mainProcess) {
        this.mainProcess = mainProcess;
    }

    public PhdProcessAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(PhdProcessAccessType accessType) {
        this.accessType = accessType;
    }

    @Override
    protected boolean condition() throws JspException {
        return !getAccessType().hasAcceptedTypes() || hasProcessNecessaryStateTypes();
    }

    private boolean hasProcessNecessaryStateTypes() {
        return !CollectionUtils.intersection(getAccessType().getAcceptedTypes(), getMainProcess().getAllPhdProcessStateTypes())
                .isEmpty();
    }
}
