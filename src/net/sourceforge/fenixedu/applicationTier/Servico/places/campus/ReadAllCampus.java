/*
 * Created on Dec 5, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICampus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadAllCampus implements IServico {
    private static ReadAllCampus service = new ReadAllCampus();

    /**
     * The singleton access method of this class.
     */
    public static ReadAllCampus getService() {
        return service;
    }

    /**
     * The actor of this class.
     */
    private ReadAllCampus() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAllCampus";
    }

    public List run() throws FenixServiceException {
        List infoCampusList;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCampus campus = sp.getIPersistentCampus();
            List campusList = campus.readAll();
            infoCampusList = (List) CollectionUtils.collect(campusList, new Transformer() {

                public Object transform(Object input) {
                    ICampus campus = (ICampus) input;
                    InfoCampus infoCampus = Cloner.copyICampus2InfoCampus(campus);
                    return infoCampus;
                }
            });
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException("Problems on database!", e);
        }
        return infoCampusList;

    }
}