/*
 * Created on 4/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteStudentInformation extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentInformation> COMPARATOR_BY_NUMBER =
            new Comparator<InfoSiteStudentInformation>() {

                @Override
                public int compare(InfoSiteStudentInformation o1, InfoSiteStudentInformation o2) {
                    return o1.getNumber().compareTo(o2.getNumber());
                }

            };

    private String name;
    private Integer number;
    private String email;
    private String username;
    private String personID;

    public InfoSiteStudentInformation() {
    }

    public InfoSiteStudentInformation(String name, String email, String username, Integer number) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoSiteStudentInformation) {
            result =
                    (getName().equals(((InfoSiteStudentInformation) arg0).getName()))
                            && (getNumber().equals(((InfoSiteStudentInformation) arg0).getNumber()))
                            && (getEmail().equals(((InfoSiteStudentInformation) arg0).getEmail()))
                            && (getUsername().equals(((InfoSiteStudentInformation) arg0).getUsername()));
        }
        return result;
    }
}