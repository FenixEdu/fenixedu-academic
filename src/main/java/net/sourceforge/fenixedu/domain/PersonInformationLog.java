package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

public class PersonInformationLog extends PersonInformationLog_Base {

    public PersonInformationLog() {
        super();
    }

    public PersonInformationLog(Person personViewed, String description) {
        super();
        if (getPersonViewed() == null) {
            setPersonViewed(personViewed);
        }
        setDescription(description);
    }

    public String getPersonName() {
        return hasPerson() ? getPerson().getName() : StringUtils.EMPTY;
    }

    public String getIstUsername() {
        return hasPerson() ? getPerson().getIstUsername() : StringUtils.EMPTY;
    }

    private static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getStringFromResourceBundle(bundle, key, args).trim();
    }

    public static PersonInformationLog createLog(Person personViewed, String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return new PersonInformationLog(personViewed, label);
    }

    public static String getPersonNameForLogDescription(Person person) {
        String personViewed;
        if ((person.getIstUsername() != null) && !(person.getIstUsername().isEmpty())) {
            personViewed = person.getIstUsername();
        } else if ((person.getPartyName() != null) && !(person.getPartyName().isEmpty())) {
            personViewed = person.getName();
        } else {
            personViewed = "ID:" + person.getExternalId();
        }
        return personViewed;
    }

}
