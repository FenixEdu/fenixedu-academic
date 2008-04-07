package net.sourceforge.fenixedu.renderers.components;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.validators.HtmlChainValidator;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public interface Validatable {
    public String getValue();

    public String[] getValues();

    public void setChainValidator(HtmlChainValidator htmlChainValidator);

    public void setChainValidator(MetaSlot slot);

    public void addValidator(HtmlValidator htmlValidator);

    public HtmlChainValidator getChainValidator();
}
