package org.fenixedu.academic.domain.accessControl.arguments;

import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.annotation.GroupArgumentParser;

@GroupArgumentParser
public class AdministrativeOfficeArgument extends DomainObjectArgumentParser<AdministrativeOffice> {
    @Override
    public Class<AdministrativeOffice> type() {
        return AdministrativeOffice.class;
    }
}
