/*
 * Created on 31/Jul/2003, 16:59:55
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 31/Jul/2003, 16:59:55
 * 
 */
public class InfoSeminaryWithEquivalencies extends InfoSeminary {

    private List equivalencies;

    /**
     * @return
     */
    public List getEquivalencies() {
	return equivalencies;
    }

    /**
     * @param list
     */
    public void setEquivalencies(List list) {
	equivalencies = list;
    }

    public void copyFromDomain(Seminary seminary) {
	super.copyFromDomain(seminary);
	if (seminary != null) {
	    for (Iterator iter = seminary.getEquivalenciesIterator(); iter.hasNext();) {
		this.equivalencies = new LinkedList();
		this.equivalencies.add((CourseEquivalency) iter.next());

	    }
	}
    }

    public static InfoSeminaryWithEquivalencies newInfoFromDomain(Seminary seminary) {
	InfoSeminaryWithEquivalencies infoSeminary = null;
	if (seminary != null) {
	    infoSeminary = new InfoSeminaryWithEquivalencies();
	    infoSeminary.copyFromDomain(seminary);
	}
	return infoSeminary;
    }

}