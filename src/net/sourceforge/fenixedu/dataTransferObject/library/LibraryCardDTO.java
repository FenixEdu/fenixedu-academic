package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class LibraryCardDTO implements Serializable {

    private DomainReference<Person> person;

    private DomainReference<Unit> unit;

    private RoleType roleType;

    private String userName;

    private String unitName;

    private Integer number;

    private String phone;

    private String mobile;

    private Integer pin;

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

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDay() {
        return "12";
    }

    public String getMonth() {
        return "12";
    }

    public String getYear() {
        return "07";
    }
}
