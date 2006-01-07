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
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;

/**
 * @author jpvl
 */
public class FindTeachersService extends SearchService {

    @Override
    protected InfoObject newInfoFromDomain(DomainObject object) {
        return InfoTeacher.newInfoFromDomain((Teacher) object);
    }

    @Override
    protected List doSearch(HashMap searchParameters, ISuportePersistente sp)
            throws ExcepcaoPersistencia {

        Teacher teacher = null;
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
