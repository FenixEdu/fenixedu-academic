package net.sourceforge.fenixedu.domain.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class DomainExceptionWithLabelFormatter extends DomainException {

    private LabelFormatter[] labelFormatterArgs;

    public DomainExceptionWithLabelFormatter(String key, String[] args) {
        super(key, args);
    }

    public DomainExceptionWithLabelFormatter(String key, LabelFormatter ...labelFormaterArgs) {
        super(key);
        this.labelFormatterArgs = labelFormaterArgs;
    }

    public LabelFormatter[] getLabelFormatterArgs() {
        return labelFormatterArgs;
    }

}
