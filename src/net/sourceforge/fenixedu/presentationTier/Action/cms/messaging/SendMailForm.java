/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.messaging;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:40:59,14/Fev/2006
 * @version $Id$
 */
public class SendMailForm extends ValidatorForm
{
	private static final long serialVersionUID = -1157986221367409679L;

	private boolean copyToSender;
	
	private String subject;

	private String message;

	private String method;

	private String fromAddress;

	private String fromPersonalName;

	private String returnURL;
	
	private String copyTo;
	
	private String group;
	
	private String sendMailActioName;
	
	private String previousRequestParameters;

	private Collection<IGroup> groupsToChooseFrom;

	private Integer[] selectedPersonalGroupsIds;

	public Integer[] getSelectedPersonalGroupsIds()
	{
		return selectedPersonalGroupsIds;
	}

	public void setSelectedPersonalGroupsIds(Integer[] selectedPersonalGroupsIds)
	{
		this.selectedPersonalGroupsIds = selectedPersonalGroupsIds;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getReturnURL()
	{
		return returnURL;
	}

	public void setReturnURL(String returnURL)
	{
		this.returnURL = returnURL;
	}

	public Collection<IGroup> getGroupsToChooseFrom()
	{
		return groupsToChooseFrom;
	}

	public void setGroupsToChooseFrom(Collection<IGroup> groups)
	{
		this.groupsToChooseFrom = groups;
	}

	public String getFromAddress()
	{
		return fromAddress;
	}

	public void setFromAddress(String fromAddress)
	{
		this.fromAddress = fromAddress;
	}

	public String getFromPersonalName()
	{
		return fromPersonalName;
	}

	public void setFromPersonalName(String fromPersonalName)
	{
		this.fromPersonalName = fromPersonalName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}



	public String getPreviousRequestParameters() {
		return previousRequestParameters;
	}

	public void setPreviousRequestParameters(String previousRequestParameters) {
		this.previousRequestParameters = previousRequestParameters;
	}

	public String getSendMailActioName() {
		return sendMailActioName;
	}

	public void setSendMailActioName(String sendMailActioName) {
		this.sendMailActioName = sendMailActioName;
	}

	public String getCopyTo() {
		return copyTo;
	}

	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}

	public boolean getCopyToSender() {
		return copyToSender;
	}

	public void setCopyToSender(boolean copyToSender) {
		this.copyToSender = copyToSender;
	}
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.copyToSender=false;
	}
}
