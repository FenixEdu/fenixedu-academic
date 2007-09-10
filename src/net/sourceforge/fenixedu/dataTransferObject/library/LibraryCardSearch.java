package net.sourceforge.fenixedu.dataTransferObject.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class LibraryCardSearch implements Serializable {

    private RoleType category;

    private String userName;

    private Integer number;

    public LibraryCardSearch() {
    }

    public List<LibraryCardDTO> getSearch() {
        List<LibraryCardDTO> libraryCardDTOList = new ArrayList<LibraryCardDTO>();
        for (Employee employee : RootDomainObject.getInstance().getEmployees()) {
            if (employee.getPerson().hasRole(RoleType.RESEARCHER)
                    && !employee.getPerson().hasRole(RoleType.TEACHER)
                    && satisfiesNumber(employee.getEmployeeNumber())) {
                if (satisfiesCategory(RoleType.RESEARCHER)
                        && satisfiesUserName(employee.getPerson().getName())) {
                    if (employee.getCurrentWorkingContract() != null) {
                        LibraryCardDTO libraryCardDTO = null;
                        if (employee.getPerson().getLibraryCard() != null) {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson().getLibraryCard(),
                                    employee.getEmployeeNumber());
                        } else {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson(),
                                    RoleType.RESEARCHER, employee.getPerson().getNickname(), employee
                                            .getLastWorkingPlace(), employee.getEmployeeNumber());
                        }

                        libraryCardDTOList.add(libraryCardDTO);
                    }
                }
            } else if (employee.getPerson().hasRole(RoleType.TEACHER)) {
                if (satisfiesCategory(RoleType.TEACHER)
                        && satisfiesUserName(employee.getPerson().getName())
                        && satisfiesNumber(employee.getEmployeeNumber())) {
                    Teacher teacher = employee.getPerson().getTeacher();
                    if (!teacher.isInactive(ExecutionPeriod.readActualExecutionPeriod())) {
                        LibraryCardDTO libraryCardDTO = null;
                        if (employee.getPerson().getLibraryCard() != null) {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson().getLibraryCard(),
                                    employee.getEmployeeNumber());
                        } else {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson(), RoleType.TEACHER,
                                    employee.getPerson().getNickname(), teacher.getLastWorkingUnit(),
                                    teacher.getTeacherNumber());
                        }
                        libraryCardDTOList.add(libraryCardDTO);
                    }
                }
            } else if (employee.getPerson().hasRole(RoleType.EMPLOYEE)) {
                if (satisfiesCategory(RoleType.EMPLOYEE)
                        && satisfiesUserName(employee.getPerson().getName())
                        && satisfiesNumber(employee.getEmployeeNumber())) {
                    if (employee.getCurrentWorkingContract() != null) {
                        LibraryCardDTO libraryCardDTO = null;
                        if (employee.getPerson().getLibraryCard() != null) {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson().getLibraryCard(),
                                    employee.getEmployeeNumber());
                        } else {
                            libraryCardDTO = new LibraryCardDTO(employee.getPerson(), RoleType.EMPLOYEE,
                                    employee.getPerson().getNickname(), employee.getLastWorkingPlace(),
                                    employee.getEmployeeNumber());
                        }
                        libraryCardDTOList.add(libraryCardDTO);
                    }
                }
            }
        }
        return libraryCardDTOList;
    }

    private boolean satisfiesUserName(String name) {
        return StringUtils.isEmpty(getUserName())
                || net.sourceforge.fenixedu.util.StringUtils.verifyContainsWithEquality(name,
                        getUserName());
    }

    private boolean satisfiesCategory(RoleType roleType) {
        return getCategory() == null || getCategory().equals(roleType);
    }

    private boolean satisfiesNumber(Integer number) {
        return getNumber() == null || getNumber().equals(number);
    }

    public RoleType getCategory() {
        return category;
    }

    public void setCategory(RoleType category) {
        this.category = category;
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

}
