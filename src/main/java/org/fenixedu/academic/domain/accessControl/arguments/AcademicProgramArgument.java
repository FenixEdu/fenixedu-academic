package org.fenixedu.academic.domain.accessControl.arguments;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AcademicProgramArgument extends DomainObjectArgumentParser<AcademicProgram> {
    @Override
    public Class<AcademicProgram> type() {
        return AcademicProgram.class;
    }
}
