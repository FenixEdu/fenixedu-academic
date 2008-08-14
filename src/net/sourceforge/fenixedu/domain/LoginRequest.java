package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.codec.digest.DigestUtils;

public class LoginRequest extends LoginRequest_Base {

    public LoginRequest(User user) {
	if (user.getLoginRequest() != null) {
	    throw new DomainException("error.user.already.has.loginRequest");
	}

	setRootDomainObject(RootDomainObject.getInstance());
	super.setUser(user);
	super.setHash(generateHash());
    }

    public void delete() {
	super.setUser(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    private String generateHash() {
	String seed = System.currentTimeMillis() + " " + this.getUser().getIdInternal();
	return DigestUtils.md5Hex(seed);
    }

    @Override
    public void setHash(String hash) {
	throw new DomainException("error.cannot.change.hash");
    }

    @Override
    public void setUser(User user) {
	throw new DomainException("error.cannot.change.user");
    }

    public static LoginRequest getLoginRequestWithHash(String hash) {
	for (LoginRequest request : RootDomainObject.getInstance().getLoginRequests()) {
	    if (request.getHash().equals(hash)) {
		return request;
	    }
	}
	return null;
    }

}
