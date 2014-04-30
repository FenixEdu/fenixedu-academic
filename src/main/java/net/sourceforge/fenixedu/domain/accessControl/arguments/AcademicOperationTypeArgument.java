package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AcademicOperationTypeArgument extends EnumArgument<AcademicOperationType> {
    @Override
    public Class<AcademicOperationType> type() {
        return AcademicOperationType.class;
    }
}
