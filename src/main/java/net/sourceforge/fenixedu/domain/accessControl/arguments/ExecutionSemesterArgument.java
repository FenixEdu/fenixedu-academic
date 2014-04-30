package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class ExecutionSemesterArgument implements ArgumentParser<ExecutionSemester> {
    @Override
    public ExecutionSemester parse(String argument) {
        String[] parts = argument.split(":");
        return ExecutionSemester.readBySemesterAndExecutionYear(Integer.valueOf(parts[0]), parts[1]);
    }

    @Override
    public String serialize(ExecutionSemester argument) {
        return argument.getSemester().toString() + ":" + argument.getYear();
    }

    @Override
    public Class<ExecutionSemester> type() {
        return ExecutionSemester.class;
    }
}
