package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadDegrees implements IService {

    public List run() throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        List degrees = sp.getICursoPersistente().readAll();

        return (List) CollectionUtils.collect(degrees, new Transformer() {

            public Object transform(Object arg0) {
                ICurso degree = (ICurso) arg0;
                InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degree);
                return infoDegree;
            }
        });

    }
}