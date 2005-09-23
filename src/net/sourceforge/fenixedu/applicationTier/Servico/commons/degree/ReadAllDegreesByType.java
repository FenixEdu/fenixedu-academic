package net.sourceforge.fenixedu.applicationTier.Servico.commons.degree;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;



public class ReadAllDegreesByType implements IService {
    private static ReadAllDegreesByType service = new ReadAllDegreesByType();

    /**
     * The singleton access method of this class.
     */
    public static ReadAllDegreesByType getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadAllDegreesByType() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAllDegreeByType";
    }

    public List run(String degreeType) throws FenixServiceException {
        List infoDegreesList;
        try {
        	
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentDegreeInfo degrees = sp.getIPersistentDegreeInfo();
            List degreesList = degrees.readDegreeByType(DegreeType.valueOf(degreeType) );
            infoDegreesList = (List) CollectionUtils.collect(degreesList, new Transformer() {

                public Object transform(Object input) {
                    IDegree degree = (IDegree) input;
                    InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                    return infoDegree;
                }
            });
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
        return infoDegreesList;

    }
}