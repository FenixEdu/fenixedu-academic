package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class DegreeTypeArgument extends EnumArgument<DegreeType> {
    @Override
    public Class<DegreeType> type() {
        return DegreeType.class;
    }
}
