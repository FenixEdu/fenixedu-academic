package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Comparator;

import pt.ist.bennu.core.domain.Bennu;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;

public class ExecutedAction extends ExecutedAction_Base {

    public static final Comparator<ExecutedAction> WHEN_OCCURED_COMPARATOR = new Comparator<ExecutedAction>() {

        @Override
        public int compare(ExecutedAction o1, ExecutedAction o2) {
            return o1.getWhenOccured().compareTo(o2.getWhenOccured());
        }
    };

    protected ExecutedAction() {
        super();
        setRootDomainObject(Bennu.getInstance());

        setWhoMade(AccessControl.getPerson());
        setWhenOccured(new DateTime());
    }

    public ExecutedAction(ExecutedActionType type) {
        this();
        init(type);
    }

    protected void init(ExecutedActionType type) {
        if (type == null) {
            throw new DomainException("error.erasmus.executed.action.type.is.null");
        }

        setType(type);
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasWhoMade() {
        return getWhoMade() != null;
    }

    @Deprecated
    public boolean hasWhenOccured() {
        return getWhenOccured() != null;
    }

}
