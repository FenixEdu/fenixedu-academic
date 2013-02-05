package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class EditUnavailablePeriod extends FenixService {

    @Service
    public static void run(UnavailablePeriod unavailablePeriod, DateTime begin, DateTime end, String justification) {

        unavailablePeriod.edit(begin, end, justification);

    }

}