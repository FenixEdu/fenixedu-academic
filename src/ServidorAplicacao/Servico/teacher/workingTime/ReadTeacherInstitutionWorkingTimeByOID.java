/*
 * Created on Nov 25, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.workingTime;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import Dominio.teacher.workTime.TeacherInstitutionWorkTime;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class ReadTeacherInstitutionWorkingTimeByOID extends ReadDomainObjectService {
    private static ReadTeacherInstitutionWorkingTimeByOID service = new ReadTeacherInstitutionWorkingTimeByOID();

    /**
     * The singleton access method of this class.
     */
    public static ReadTeacherInstitutionWorkingTimeByOID getService() {
        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass() {
        return TeacherInstitutionWorkTime.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        return sp.getIPersistentTeacherInstitutionWorkingTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
     */
    protected InfoObject clone2InfoObject(IDomainObject domainObject) {
        return Cloner
                .copyITeacherInstitutionWorkingTime2InfoTeacherInstitutionWorkTime((ITeacherInstitutionWorkTime) domainObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadTeacherInstitutionWorkingTimeByOID";
    }

}