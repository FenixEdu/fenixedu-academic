/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.ajax.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.lang.StringUtils;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TeacherBridge {

    public static String readNameByTeacherNumber(String number) throws FenixFilterException,
            FenixServiceException {

        if (number == null || number.length() == 0 || !StringUtils.isNumeric(number)) {
            return "";
        }

        Object[] args = { Integer.valueOf(number) };
        InfoTeacher teacher = (InfoTeacher) ServiceUtils.executeService(null, "ReadTeacherByNumber",
                args);

        return (teacher != null && teacher.getInfoPerson() != null) ? teacher.getInfoPerson().getNome()
                : "";
    }

    public static Collection readNonAffiliatedTeachersByName(String name) throws FenixFilterException,
            FenixServiceException {

        
        if (name == null || name.length() == 0) {
            return new ArrayList();
        }

        IUserView userView = SessionUtils.getUserView(ExecutionContext.get().getHttpServletRequest());        
        Object[] args = { name };
        List nonAffiliatedTeachers = (List) ServiceUtils.executeService(userView, "ReadNonAffiliatedTeachersByName", args);
        return nonAffiliatedTeachers;
//        return CollectionUtils.collect(nonAffiliatedTeachers, new Transformer() {
//        
//            public Object transform(Object arg0) {
//                InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = (InfoNonAffiliatedTeacher) arg0;
//                return infoNonAffiliatedTeacher.getName();
//            }
//        
//        });

        
    }

}
