package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;


import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class UpdateVigilantGroup {

    @Atomic
    public static void run(VigilantGroup vigilantGroup, String name, String convokeStrategy, String contactEmail,
            String emailPrefix, String rulesLink, DateTime beginFirst, DateTime endFirst, DateTime beginSecond, DateTime endSecond) {

        vigilantGroup.setName(name);
        vigilantGroup.setConvokeStrategy(convokeStrategy);
        vigilantGroup.setContactEmail(contactEmail);
        vigilantGroup.setEmailSubjectPrefix(emailPrefix);
        vigilantGroup.setRulesLink(rulesLink);
        vigilantGroup.setBeginOfFirstPeriodForUnavailablePeriods(beginFirst);
        vigilantGroup.setEndOfFirstPeriodForUnavailablePeriods(endFirst);
        vigilantGroup.setBeginOfSecondPeriodForUnavailablePeriods(beginSecond);
        vigilantGroup.setEndOfSecondPeriodForUnavailablePeriods(endSecond);

    }

}