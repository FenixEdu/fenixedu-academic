package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class PhdIndividualProgramProcessArgument extends DomainObjectArgumentParser<PhdIndividualProgramProcess> {
    @Override
    public Class<PhdIndividualProgramProcess> type() {
        return PhdIndividualProgramProcess.class;
    }
}
