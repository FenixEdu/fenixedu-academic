/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ISupportLesson;
import Dominio.SupportLesson;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadSupportLessonByOID extends ReadDomainObjectService {
    private static ReadSupportLessonByOID service = new ReadSupportLessonByOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadSupportLessonByOID getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return SupportLesson.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentSupportLesson();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return Cloner.copyISupportLesson2InfoSupportLesson((ISupportLesson) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadSupportLessonByOID";
    }

}