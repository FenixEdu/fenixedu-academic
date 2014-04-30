package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.CurricularYear;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class CurricularYearArgument implements ArgumentParser<CurricularYear> {
    @Override
    public CurricularYear parse(String argument) {
        return CurricularYear.readByYear(Integer.valueOf(argument));
    }

    @Override
    public String serialize(CurricularYear argument) {
        return argument.getYear().toString();
    }

    @Override
    public Class<CurricularYear> type() {
        return CurricularYear.class;
    }
}
