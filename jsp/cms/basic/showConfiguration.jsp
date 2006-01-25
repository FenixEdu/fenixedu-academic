<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define name="cmsExists" id="cmsPresent"/>
<logic:equal name="cmsPresent" value="true">
	<html:form action="/cmsConfigurationManagement">
		<html:hidden property="method" value="write"/>
		<table class="style1" width="100%">
			<tr>
				<td width="35%" class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.configurationParameter.label"/>
				</td>
				<td width="65%" class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.configurationValue.label"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.smtpServerAddress.label"/>	
				</td>
				<td class="listClasses">
					<html:text property="smtpServerAddress" style="width:100%"/>
				</td>
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.filterNonTextualAttachments.label"/>				
				</td>
				<td class="listClasses" style="text-align:left"%>
					<html:checkbox property="filterNonTextualAttachments" />
				</td>					
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.mailingListsHost.label"/>	
				</td>
				<td class="listClasses">
					<html:text property="mailingListHost" style="width:100%"/>
				</td>						
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.conversationReplyMarkers.label"/>	
				</td>
				<td class="listClasses">
					<html:text property="conversationReplyMarkers" style="width:100%"/>
				</td>
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.maxAttachmentSize.label"/>	
				</td>
				<td class="listClasses">
					<html:text property="maxAttachmentSize" style="width:100%"/>
				</td>										
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.maxMessageSize.label"/>	
				</td>
				<td class="listClasses">
					<html:text property="maxMessageSize" style="width:100%"/>
				</td>										
			</tr>
		</table>
		<html:submit> 
			<bean:message  key="cms.save.button" bundle="CMS_RESOURCES"/>
		</html:submit>
		
		<html:link module="/cms" action="/cmsConfigurationManagement.do?method=deleteCms">
			<bean:message bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.deleteCms.label"/>
		</html:link>
</html:form>
<br />
</logic:equal>
<logic:notPresent name="cmsExists">
		<html:link module="/cms" action="/cmsConfigurationManagement.do?method=createCms">
			<bean:message bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.createCms.label"/>
		</html:link>	
</logic:notPresent>
<logic:equal name="cmsExists" value="false">
		<html:link module="/cms" action="/cmsConfigurationManagement.do?method=createCms">
			<bean:message bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.createCms.label"/>
		</html:link>	
</logic:equal>