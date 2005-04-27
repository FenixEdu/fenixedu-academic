package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRole extends PersonRole_Base implements IPersonRole {

    public boolean equals(Object obj) {
        return ((obj instanceof PersonRole) && (((PersonRole) obj).getRole().equals(getRole())) && (((PersonRole) obj)
                .getPerson().equals(getPerson())));
    }

}