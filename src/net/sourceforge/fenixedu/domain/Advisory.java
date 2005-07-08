package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Nunes & Luis Cruz
 * 
 * /2003/08/26
 */
public class Advisory extends Advisory_Base {

    public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		stringBuilder.append(getClass().getName());
		stringBuilder.append(": ");
		stringBuilder.append("idInternal = ");
		stringBuilder.append(getIdInternal());
		stringBuilder.append("; ");
		stringBuilder.append("subject = ");
		stringBuilder.append(getSubject());
		stringBuilder.append("; ");
		stringBuilder.append("sender = ");
		stringBuilder.append(getSender());
		stringBuilder.append("\n");
		stringBuilder.append("message = ");
		stringBuilder.append(getMessage());
		stringBuilder.append("\n");
		stringBuilder.append("created = ");
		stringBuilder.append(getCreated());
		stringBuilder.append("\n");
		stringBuilder.append("expires = ");
		stringBuilder.append(getExpires());
		stringBuilder.append("\n");
		stringBuilder.append("onlyShowOnce = ");
		stringBuilder.append(getOnlyShowOnce());
		stringBuilder.append("]\n");
		
        return stringBuilder.toString();
    }

}