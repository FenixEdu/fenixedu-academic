package Dominio;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRole extends DomainObject implements IPersonRole {
    private Integer keyPerson;

    private Integer keyRole;

    private IPerson person;

    private IRole role;

    public PersonRole() {
    }

    public boolean equals(Object obj) {
        return ((obj instanceof PersonRole) && (((PersonRole) obj).getRole().equals(getRole())) && (((PersonRole) obj)
                .getPerson().equals(getPerson())));
    }

    /**
     * @return
     */
    public Integer getKeyPerson() {
        return keyPerson;
    }

    /**
     * @return
     */
    public Integer getKeyRole() {
        return keyRole;
    }

    /**
     * @return
     */
    public IPerson getPerson() {
        return person;
    }

    /**
     * @return
     */
    public IRole getRole() {
        return role;
    }

    /**
     * @param integer
     */
    public void setKeyPerson(Integer integer) {
        keyPerson = integer;
    }

    /**
     * @param integer
     */
    public void setKeyRole(Integer integer) {
        keyRole = integer;
    }

    /**
     * @param pessoa
     */
    public void setPerson(IPerson pessoa) {
        person = pessoa;
    }

    /**
     * @param role
     */
    public void setRole(IRole role) {
        this.role = role;
    }

}