package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;

/**
 * @author Fernanda Quitério
 * 
 * 
 * Created at 25/Jun/2004
 *  
 */
public class InfoSeminaryWithEquivalenciesWithAll extends InfoSeminaryWithEquivalencies {
    public void copyFromDomain(Seminary seminary) {
        super.copyFromDomain(seminary);
        final List list = new ArrayList();
        setEquivalencies(list);
        if (seminary != null && seminary.getEquivalencies() != null) {
            for (final CourseEquivalency courseEquivalency : seminary.getEquivalenciesSet()) {
        	list.add(InfoEquivalencyWithAll.newInfoFromDomain(courseEquivalency));
            }
        }
    }

    public static InfoSeminaryWithEquivalencies newInfoFromDomain(Seminary seminary) {
        InfoSeminaryWithEquivalenciesWithAll infoSeminary = null;
        if (seminary != null) {
            infoSeminary = new InfoSeminaryWithEquivalenciesWithAll();
            infoSeminary.copyFromDomain(seminary);
        }
        return infoSeminary;
    }

}