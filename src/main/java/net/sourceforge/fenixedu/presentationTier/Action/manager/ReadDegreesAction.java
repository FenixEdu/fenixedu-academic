package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/readDegrees", input = "mainPageInput.do", scope = "session")
@Forwards(value = { @Forward(name = "readDegrees", path = "/manager/readDegrees_bd.jsp") })
public class ReadDegreesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        degrees.addAll(Degree.readAllByDegreeType(DegreeType.MASTER_DEGREE));
        request.setAttribute("degreesList", degrees);
        return mapping.findForward("readDegrees");
    }

}
