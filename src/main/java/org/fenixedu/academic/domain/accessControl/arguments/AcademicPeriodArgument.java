package org.fenixedu.academic.domain.accessControl.arguments;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.bennu.core.annotation.GroupArgumentParser;
import org.fenixedu.bennu.core.groups.ArgumentParser;

@GroupArgumentParser
public class AcademicPeriodArgument implements ArgumentParser<AcademicPeriod> {

    @Override
    public AcademicPeriod parse(String s) {
        return AcademicPeriod.getAcademicPeriodFromString(s);
    }

    @Override
    public String serialize(AcademicPeriod academicPeriod) {
        return academicPeriod.getRepresentationInStringFormat();
    }

    @Override
    public Class<AcademicPeriod> type() {
        return AcademicPeriod.class;
    }
}
