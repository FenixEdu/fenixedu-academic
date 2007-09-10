package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.YearMonthDay;

public class LibraryCardDTO implements Serializable {

    private DomainReference<LibraryCard> libraryCard;

    private DomainReference<Person> person;

    private DomainReference<Unit> unit;

    private RoleType roleType;

    private String userName;

    private String unitName;

    private String chosenUnitName;

    private Integer number;

    private String phone;

    private String mobile;

    private Integer pin;

    private Boolean unlimitedCard;

    private YearMonthDay validUntil;

    public LibraryCardDTO(Person person, RoleType roleType, String userName, Unit unit, Integer number) {
        setPerson(person);
        setRoleType(roleType);
        setUserName(userName);
        setUnit(unit);
        setUnitName(unit != null ? unit.getName() : "");
        setNumber(number);
        setPhone(person.getPhone());
        setMobile(person.getMobile());
    }

    public LibraryCardDTO(LibraryCard libraryCard, Integer number) {
        setLibraryCard(libraryCard);
        setPin(libraryCard.getPin());
        setPerson(libraryCard.getPerson());
        setRoleType(libraryCard.getRole());
        setUserName(libraryCard.getUserName());
        setUnitName(libraryCard.getUnitName());
        setNumber(number);
        setPhone(libraryCard.getPerson().getPhone());
        setMobile(libraryCard.getPerson().getMobile());
    }

    public LibraryCardDTO(LibraryCard libraryCard) {
        setLibraryCard(libraryCard);
        setPin(libraryCard.getPin());
        setPerson(libraryCard.getPerson());
        setRoleType(libraryCard.getRole());
        setUserName(libraryCard.getUserName());
        setUnitName(libraryCard.getUnitName());
        setNumber(libraryCard.getPerson().getEmployee().getEmployeeNumber());
        setValidUntil(libraryCard.getValidUntil());
        setPhone(libraryCard.getPerson().getPhone());
        setMobile(libraryCard.getPerson().getMobile());
    }

    public Integer getLibraryCardID() {
        return getLibraryCard() != null ? getLibraryCard().getIdInternal() : 0;
    }

    public boolean getIsToGenerate() {
        return getLibraryCard() == null;
    }

    public boolean getIsToViewDetails() {
        return !getIsToGenerate();
    }

    public LibraryCard getLibraryCard() {
        return libraryCard != null ? libraryCard.getObject() : null;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard != null ? new DomainReference<LibraryCard>(libraryCard) : null;
    }

    public Person getPerson() {
        return person != null ? person.getObject() : null;
    }

    public void setPerson(Person person) {
        this.person = person != null ? new DomainReference<Person>(person) : null;
    }

    public Unit getUnit() {
        return unit != null ? unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
        this.unit = unit != null ? new DomainReference<Unit>(unit) : null;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getCategory() {
        final ResourceBundle enumerationBundle = ResourceBundle.getBundle(
                "resources.EnumerationResources", LanguageUtils.getLocale());
        return enumerationBundle.getString(getRoleType().name());
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile != null ? mobile : "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone : "";
    }

    public String getPinToShow() {
        if (getPin() != null) {
            return getPin().toString();
        }
        return "";
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDay() {
        if (getValidUntil() != null) {
            int day = getValidUntil().getDayOfMonth();
            if (day < 10) {
                return "0" + day;
            }
            return new Integer(day).toString();
        }
        return "";
    }

    public String getMonth() {
        if (getValidUntil() != null) {
            int month = getValidUntil().getMonthOfYear();
            if (month < 10) {
                return "0" + month;
            }
            return new Integer(month).toString();
        }
        return "";
    }

    public String getYear() {
        if (getValidUntil() != null) {
            String a = new Integer(getValidUntil().getYear()).toString();
            return a.substring(2);
        }
        return "";
    }

    public Boolean getUnlimitedCard() {
        return unlimitedCard;
    }

    public void setUnlimitedCard(Boolean unlimitedCard) {
        this.unlimitedCard = unlimitedCard;
    }

    public YearMonthDay getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(YearMonthDay validUntil) {
        this.validUntil = validUntil;
    }

    public String getChosenUnitName() {
        return chosenUnitName;
    }

    public void setChosenUnitName(String chosenUnitName) {        
        if (chosenUnitName != null) {
            int position = chosenUnitName.indexOf("-");
            this.chosenUnitName = chosenUnitName.substring(position + 2);
        }
    }
}
