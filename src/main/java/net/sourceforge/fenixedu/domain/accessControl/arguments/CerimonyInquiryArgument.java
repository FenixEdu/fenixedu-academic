package net.sourceforge.fenixedu.domain.accessControl.arguments;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;

import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class CerimonyInquiryArgument extends DomainObjectArgumentParser<CerimonyInquiry> {
    @Override
    public Class<CerimonyInquiry> type() {
        return CerimonyInquiry.class;
    }
}