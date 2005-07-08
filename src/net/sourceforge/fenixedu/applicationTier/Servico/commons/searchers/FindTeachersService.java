/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.SearchService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class FindTeachersService extends SearchService {

    static private FindTeachersService serviceInstance = new FindTeachersService();

    private FindTeachersService() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.SearchService#cloneDomainObject(Dominio.IDomainObject)
     */
    protected InfoObject cloneDomainObject(IDomainObject object) {
        ITeacher teacher = (ITeacher) object;
        return Cloner.copyITeacher2InfoTeacher(teacher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.SearchService#doSearch(java.util.HashMap,
     *      ServidorPersistente.ISuportePersistente)
     */
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        ITeacher teacher = null;
        try {
            teacher = teacherDAO.readByNumber(new Integer(searchParameters.get("teacherNumber")
                    .toString()));
        } catch (NumberFormatException e) {
            //ignored
        }
        List returnList = new ArrayList();
        if (teacher != null) {
            returnList.add(teacher);
        }
        return returnList;
    }

    public static FindTeachersService getService() {
        return serviceInstance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "FindTeachersService";
    }
}