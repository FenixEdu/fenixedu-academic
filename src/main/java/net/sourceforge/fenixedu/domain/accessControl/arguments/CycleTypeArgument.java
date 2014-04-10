package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class CycleTypeArgument extends EnumArgument<CycleType> {
    @Override
    public Class<CycleType> type() {
        return CycleType.class;
    }
}
