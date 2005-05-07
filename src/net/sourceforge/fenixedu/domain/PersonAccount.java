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

	private IPerson person;

	public PersonAccount() {
	}

	public PersonAccount(IPerson person) {
		this.person = person;
		setBalance(new Double(0));

	}

	public IPerson getPerson() {
		return person;
	}

	public void setPerson(IPerson person) {
		this.person = person;
	}

}