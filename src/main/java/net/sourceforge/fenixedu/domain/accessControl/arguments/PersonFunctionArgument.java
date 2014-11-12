package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class PersonFunctionArgument extends DomainObjectArgumentParser<PersonFunction> {
    @Override
    public Class<PersonFunction> type() {
        return PersonFunction.class;
    }
}
