package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DomainReference;

/**
 * @author Nuno Nunes & Luis Cruz
 * 
 * /2003/08/26
 */

public class InfoAdvisory extends InfoObject {

    private final DomainReference<Advisory> advisoryDomainReference;

    public InfoAdvisory(final Advisory advisory) {
    	advisoryDomainReference = new DomainReference<Advisory>(advisory);
    }

    public Advisory getAdvisory() {
        return advisoryDomainReference == null ? null : advisoryDomainReference.getObject();
    }

    public String toString() {
    	return getAdvisory().toString();
    }

    public Date getCreated() {
        return getAdvisory().getCreated();
    }

    public Date getExpires() {
        return getAdvisory().getExpires();
    }

    public String getMessage() {
        return getAdvisory().getMessage();
    }

    public String getSubject() {
        return getAdvisory().getSubject();
    }

    public String getSender() {
        return getAdvisory().getSender();
    }

    public static InfoAdvisory newInfoFromDomain(Advisory advisory) {
    	return advisory == null ? null : new InfoAdvisory(advisory);
    }

	@Override
	public Integer getIdInternal() {
		return getAdvisory().getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}