/*
 * Created on 4/Ago/2003
 *
 */
package DataBeans;

/**
 * @author asnr and scpo
 *  
 */
public class InfoSiteStudentInformation extends DataTranferObject implements ISiteComponent {
    private String name;

    private Integer number;

    private String email;

    private String username;

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoSiteStudentInformation) {
            result = (getName().equals(((InfoSiteStudentInformation) arg0).getName()))
                    && (getNumber().equals(((InfoSiteStudentInformation) arg0).getNumber()))
                    && (getEmail().equals(((InfoSiteStudentInformation) arg0).getEmail()))
                    && (getUsername().equals(((InfoSiteStudentInformation) arg0).getUsername()));
        }
        return result;
    }

}