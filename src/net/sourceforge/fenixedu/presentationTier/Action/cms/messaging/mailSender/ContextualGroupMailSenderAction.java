/**
 * 
 */

package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.mailSender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.presentationTier.Action.cms.messaging.SendMailForm;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts.action.ActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:02:27,23/Mar/2006
 * @version $Id: ContextualGroupMailSenderAction.java,v 1.1 2006/03/29 17:15:55
 *          gedl Exp $
 */
public abstract class ContextualGroupMailSenderAction extends MailSenderAction {

    protected IGroup[] serializeGroups(ActionForm form, HttpServletRequest request) throws FenixFilterException, FenixServiceException, FenixActionException {
        SendMailForm sendMailForm = (SendMailForm) form;

        byte[] decodedForm = Base64.decodeBase64(sendMailForm.getGroup().getBytes());

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
                decodedForm);
        try {
            ObjectInputStream stream = new ObjectInputStream(byteInputStream);
            IGroup group = (IGroup) stream.readObject();
            IGroup[] groups = new IGroup[1];
            groups[0] = group;
            return groups;
        } catch (IOException e) {
            throw new FenixActionException(e);
        } catch (ClassNotFoundException e) {
            throw new FenixActionException(e);
        }
    }

    protected List<IGroup> loadPersonalGroupsToChooseFrom(
            HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        return null; // we don't want to display person's personal groups
    }

    protected String serializeGroup(IGroup group) throws FenixActionException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream dos = new ObjectOutputStream(baos);
            dos.writeObject(group);
        } catch (IOException e) {
            throw new FenixActionException(e);
        }
        byte[] groupArray = baos.toByteArray();
        String groupString = new String(Base64.encodeBase64(groupArray));
        return groupString;
    }
}
