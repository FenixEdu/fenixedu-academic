package net.sourceforge.fenixedu.domain;

import org.apache.commons.codec.digest.DigestUtils;

public class ExternalUser extends ExternalUser_Base {

	public ExternalUser() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public ExternalUser(final String username, final String password) {
		this();
		setUsername(username);
		final String hash = hash(password);
		setPasswordHash(hash);
	}

	private String hash(final String password) {
		return DigestUtils.shaHex(password);
	}

	public boolean verify(final String username, final String password) {
		return username.equals(getUsername()) && hash(password).equals(getPasswordHash());
	}

}
