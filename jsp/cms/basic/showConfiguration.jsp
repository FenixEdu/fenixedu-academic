<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define name="cmsExists" id="cmsPresent"/>
<logic:equal name="cmsPresent" value="true">
	<html:form action="/cmsConfigurationManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="write"/>
		<table class="style1" width="100%">
			<tr>
				<th width="35%" class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.configurationParameter.label"/>
				</th>
				<th width="65%" class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.configurationValue.label"/>
				</th>
			</tr>
			<tr>
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.smtpServerAddress.label"/>	
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.smtpServerAddress" property="smtpServerAddress" style="width:100%"/>
				</td>
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.filterNonTextualAttachments.label"/>				
				</td>
				<td class="listClasses" style="text-align:left"%>
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.filterNonTextualAttachments" property="filterNonTextualAttachments" />
				</td>					
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.mailingListsHost.label"/>	
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.mailingListHost" property="mailingListHost" style="width:100%"/>
				</td>						
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.conversationReplyMarkers.label"/>	
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.conversationReplyMarkers" property="conversationReplyMarkers" style="width:100%"/>
				</td>
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.maxAttachmentSize.label"/>	
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.maxAttachmentSize" property="maxAttachmentSize" style="width:100%"/>
				</td>										
			</tr>
			<tr>		
				<td class="listClasses">
					<bean:message  bundle="CMS_RESOURCES" key="cms.configuration.viewConfiguration.maxMessageSize.label"/>	
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.maxMessageSize" property="maxMessageSize" style="width:100%"/>
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