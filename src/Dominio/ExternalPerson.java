/*
 * Created on 9/Out/2003
 *
 *
 */
package Dominio;
/**
 * @author:
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public class ExternalPerson extends DomainObject implements IExternalPerson {
	// database internal codes
	private Integer keyPerson;
	//fields
	private IPessoa person;
	private String workLocation;

	/** Creates a new instance of ExternalPerson */
	public ExternalPerson() {
	}
	/** Creates a new instance of ExternalPerson */
	public ExternalPerson(Integer idInternal) {
		setIdInternal(idInternal);
	}
	public void setKeyPerson(Integer keyPerson) {
		this.keyPerson = keyPerson;
	}
	public Integer getKeyPerson() {
		return keyPerson;
	}
	public void setPerson(IPessoa person) {
		this.person = person;
	}
	public IPessoa getPerson() {
		return person;
	}
	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}
	public String getWorkLocation() {
		return workLocation;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IExternalPerson) {
			IExternalPerson externalPerson = (IExternalPerson) obj;
			result = this.getPerson().equals(externalPerson.getPerson());
		}
		return result;
	}
}
