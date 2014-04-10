package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class ExecutionYearArgument implements ArgumentParser<ExecutionYear> {
    @Override
    public ExecutionYear parse(String argument) {
        return ExecutionYear.readExecutionYearByName(argument);
    }

    @Override
    public String serialize(ExecutionYear argument) {
        return argument.getName();
    }

    @Override
    public Class<ExecutionYear> type() {
        return ExecutionYear.class;
    }
}
