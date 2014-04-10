package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AcademicOperationTypeScopeArgument extends EnumArgument<Scope> {
    @Override
    public Class<Scope> type() {
        return Scope.class;
    }
}
