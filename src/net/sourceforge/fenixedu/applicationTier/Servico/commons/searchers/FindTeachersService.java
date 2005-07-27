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
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class FindTeachersService extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(IDomainObject object) {
        return Cloner.copyITeacher2InfoTeacher((ITeacher) object);
    }

    @Override
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        ITeacher teacher = null;
        try {
            teacher = sp.getIPersistentTeacher().readByNumber(
                    new Integer(searchParameters.get("teacherNumber").toString()));
        } catch (NumberFormatException e) {
            // ignored
        }

        List returnList = new ArrayList();
        if (teacher != null) {
            returnList.add(teacher);
        }
        return returnList;
    }

}
