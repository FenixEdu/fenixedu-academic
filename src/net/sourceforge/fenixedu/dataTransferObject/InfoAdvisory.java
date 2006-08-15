package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Advisory;

/**
 * @author Nuno Nunes & Luis Cruz
 * 
 * /2003/08/26
 */

public class InfoAdvisory extends InfoObject {

	private final Advisory advisory;

    public InfoAdvisory(final Advisory advisory) {
    	this.advisory = advisory;
    }

    public String toString() {
    	return advisory.toString();
    }

    public Date getCreated() {
        return advisory.getCreated();
    }

    public Date getExpires() {
        return advisory.getExpires();
    }

    public String getMessage() {
        return advisory.getMessage();
    }

    public String getSubject() {
        return advisory.getSubject();
    }

    public String getSender() {
        return advisory.getSender();
    }

    public static InfoAdvisory newInfoFromDomain(Advisory advisory) {
    	return advisory == null ? null : new InfoAdvisory(advisory);
    }

	@Override
	public Integer getIdInternal() {
		return advisory.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}