package DataBeans.Seminaries;

import java.util.List;

import org.apache.commons.collections.Transformer;

import Dominio.Seminaries.ICourseEquivalency;
import Dominio.Seminaries.ISeminary;

import commons.CollectionUtils;

/**
 * @author Fernanda Quitério
 * 
 * 
 * Created at 25/Jun/2004
 *  
 */
public class InfoSeminaryWithEquivalenciesWithAll extends InfoSeminary {
    public void copyFromDomain(ISeminary seminary) {
        super.copyFromDomain(seminary);
        if (seminary != null && seminary.getEquivalencies() != null
                && !seminary.getEquivalencies().isEmpty()) {

            setEquivalencies((List) CollectionUtils.collect(seminary.getEquivalencies(),
                    new Transformer() {

                        public Object transform(Object arg0) {
                            return InfoEquivalencyWithAll.newInfoFromDomain((ICourseEquivalency) arg0);
                        }
                    }));
        }
    }

    public static InfoSeminary newInfoFromDomain(ISeminary seminary) {
        InfoSeminaryWithEquivalenciesWithAll infoSeminary = null;
        if (seminary != null) {
            infoSeminary = new InfoSeminaryWithEquivalenciesWithAll();
            infoSeminary.copyFromDomain(seminary);
        }
        return infoSeminary;
    }

}