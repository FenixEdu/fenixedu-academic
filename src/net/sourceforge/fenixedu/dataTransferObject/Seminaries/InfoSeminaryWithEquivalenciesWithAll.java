package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

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
        if (seminary != null && seminary.getEquivalencies() != null
                && !seminary.getEquivalencies().isEmpty()) {

            setEquivalencies((List) CollectionUtils.collect(seminary.getEquivalencies(),
                    new Transformer() {

                        public Object transform(Object arg0) {
                            return InfoEquivalencyWithAll.newInfoFromDomain((CourseEquivalency) arg0);
                        }
                    }));
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