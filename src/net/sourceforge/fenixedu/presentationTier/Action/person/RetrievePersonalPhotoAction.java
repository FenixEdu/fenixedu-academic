/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.FileEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RetrievePersonalPhotoAction extends FenixDispatchAction {

    public ActionForward retrieveOwnPhoto(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final IUserView userView = UserView.getUser();
        final FileEntry personalPhoto = userView.getPerson().getPersonalPhoto();

        if (personalPhoto != null) {
            writePhoto(response, personalPhoto);
        }

        return null;

    }

    public ActionForward retrieveByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        final Integer personID = new Integer(request.getParameter("personCode"));

        final Person person = (Person) rootDomainObject.readPartyByOID(personID);
        final FileEntry personalPhoto = person.getPersonalPhoto();

        if (personalPhoto != null) {
            final IUserView userView = UserView.getUser();
            final Person requester = userView.getPerson();
            if (requester != person && !person.getAvailablePhoto()) {
                if (!(person.hasRole(RoleType.STUDENT) && (requester.hasRole(RoleType.TEACHER) || requester
                        .hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)))) {
                    return null;
                }
            }

            writePhoto(response, personalPhoto);
        }

        return null;

    }

    protected void writePhoto(final HttpServletResponse response, final FileEntry personalPhoto) {
        try {
            response.setContentType(personalPhoto.getContentType().getMimeType());
            final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(personalPhoto.getContents());
            dos.close();
        } catch (IOException e) {
        }
    }

}
