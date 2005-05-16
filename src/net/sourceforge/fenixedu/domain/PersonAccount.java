/*
 * Created on Jul 22, 2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */
public class PersonAccount extends PersonAccount_Base {

    public PersonAccount() {
    }

    public PersonAccount(IPerson person) {
        setPerson(person);
        setBalance(new Double(0));

    }

}
