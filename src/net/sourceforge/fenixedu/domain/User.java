package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.sourceforge.fenixedu.domain.assiduousness.IdentificationCard;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.ByteArray;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author mrsp
 * @author shezad
 */

public class User extends User_Base {

    private User() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public User(Person person) {
	this();
	if (person == null) {
	    throw new DomainException("error.no.person");
	}
	super.setPerson(person);
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.impossible.to.edit.person");
    }

    public static User readUserByUserUId(final String userUId) {
	final Login login = Login.readLoginByUsername(userUId);
	return login == null ? null : login.getUser();
    }

    public Login readUserLoginIdentification() {
	for (Identification identification : getIdentifications()) {
	    if (identification.isLogin()) {
		return (Login) identification;
	    }
	}
	return null;
    }

    public List<IdentificationCard> getIdentificationCards() {
	List<IdentificationCard> cards = new ArrayList<IdentificationCard>();
	for (Identification identification : this.getIdentifications()) {
	    if (identification instanceof IdentificationCard) {
		cards.add((IdentificationCard) identification);
	    }
	}
	return cards;
    }

    public void delete() {
	for (; !getIdentifications().isEmpty(); getIdentifications().get(0).delete())
	    ;
	if (hasLoginRequest()) {
	    getLoginRequest().delete();
	}
	super.setPerson(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public String getAliass() {
	final StringBuilder aliass = new StringBuilder();
	for (final LoginAlias loginAlias : readUserLoginIdentification().getLoginAliasOrderByImportance()) {
	    if (aliass.length() > 0) {
		aliass.append(", ");
	    }
	    aliass.append(loginAlias.getAlias());
	}
	return aliass.toString();
    }

    @Service
    public void logout() {
	setLogoutDateTime(new DateTime());
    }

    @Service
    public void generateNewKey() throws Exception {
	KeyGenerator kgen = KeyGenerator.getInstance("AES");
	kgen.init(128);
	SecretKey skey = kgen.generateKey();
	byte[] raw = skey.getEncoded();

	setPrivateKey(new ByteArray(raw));
	setPrivateKeyCreation(new DateTime());
	setPrivateKeyValidity(getPrivateKeyCreation().plusYears(1));
    }

    public static String getRemoteUserEmail(String istUsername) {
	final User user = User.readUserByUserUId(istUsername);
	return user != null ? user.getPerson().getDefaultEmailAddressValue() : null;
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final String username = getUserUId();
	return username == null || ((username.length() == 8 || username.length() == 9) && username.startsWith("ist"));
    }


	@Deprecated
	public java.util.Date getCurrentLoginDateTime(){
		org.joda.time.DateTime dt = getCurrentLoginDateTimeDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setCurrentLoginDateTime(java.util.Date date){
		if(date == null) setCurrentLoginDateTimeDateTime(null);
		else setCurrentLoginDateTimeDateTime(new org.joda.time.DateTime(date.getTime()));
	}

	@Deprecated
	public java.util.Date getLastLoginDateTime(){
		org.joda.time.DateTime dt = getLastLoginDateTimeDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setLastLoginDateTime(java.util.Date date){
		if(date == null) setLastLoginDateTimeDateTime(null);
		else setLastLoginDateTimeDateTime(new org.joda.time.DateTime(date.getTime()));
	}

	@Deprecated
	public java.util.Date getLogout(){
		org.joda.time.DateTime dt = getLogoutDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setLogout(java.util.Date date){
		if(date == null) setLogoutDateTime(null);
		else setLogoutDateTime(new org.joda.time.DateTime(date.getTime()));
	}


}
