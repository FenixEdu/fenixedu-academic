/*
 * Created on Dec 5, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.places.campus;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCampus;
import DataBeans.util.Cloner;
import Dominio.ICampus;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.places.campus.IPersistentCampus;

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
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
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
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
        return infoCampusList;

    }
}