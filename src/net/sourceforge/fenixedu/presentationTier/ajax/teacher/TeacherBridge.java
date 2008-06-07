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

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.UserView;

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
        InfoTeacher teacher = (InfoTeacher) ServiceUtils.executeService("ReadTeacherByNumber",
                args);

        return (teacher != null && teacher.getInfoPerson() != null) ? teacher.getInfoPerson().getNome()
                : "";
    }

    public static Collection readNonAffiliatedTeachersByName(String name) throws FenixFilterException,
            FenixServiceException {

        
        if (name == null || name.length() == 0) {
            return new ArrayList();
        }

        IUserView userView = UserView.getUser();
        Object[] args = { name };
        List nonAffiliatedTeachers = (List) ServiceUtils.executeService("ReadNonAffiliatedTeachersByName", args);
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
