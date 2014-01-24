package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class SantanderCardInformation extends SantanderCardInformation_Base {

    public SantanderCardInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SantanderCardInformation(Person person, String dchpline) {
        this();
        setPerson(person);
        setDchpRegisteLine(dchpline);
    }

    public void delete() {
        setDchpRegisteLine(null);
        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static String getRegisterType(String dchpline) {
        return dchpline.substring(0, 1);
    }

    public static DateTime getProductionDateTime(String dchpline) {
        int year = Integer.parseInt(dchpline.substring(1, 5));
        int monthOfYear = Integer.parseInt(dchpline.substring(5, 7));
        int dayOfMonth = Integer.parseInt(dchpline.substring(7, 9));
        int hourOfDay = Integer.parseInt(dchpline.substring(9, 11));
        int minuteOfHour = Integer.parseInt(dchpline.substring(11, 13));
        int secondOfMinute = Integer.parseInt(dchpline.substring(13, 15));
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
    }

    public static String getProductionContract(String dchpline) {
        return dchpline.substring(15, 20);
    }

    public static String getProductionSequence(String dchpline) {
        return dchpline.substring(20, 24);
    }

    public static String getIdentificationCardNumber(String dchpline) {
        int id_dim = Integer.parseInt(dchpline.substring(24, 26));
        return dchpline.substring(26, 26 + id_dim).trim();
    }

    public static String getIdentificationCardInformation(String dchpline) {
        int data_dim = Integer.parseInt(dchpline.substring(66, 68));
        return dchpline.substring(68, 68 + data_dim).trim();
    }

    public static String getIdentificationCardChipInformation(String dchpline) {
        int chip_dim = Integer.parseInt(dchpline.substring(167, 170));
        return dchpline.substring(170, 170 + chip_dim).trim();
    }
}
