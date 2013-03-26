<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteStudentInformationList">
		<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	</logic:empty> 

	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
		<bean:define id="infoGroup" name="component" property="infoStudentGroup" />
		<h2>
			<bean:message key="label.group"/>:
			<bean:write name="infoGroup" property="groupNumber" />
		</h2>
		<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
			<tr>
				<th><bean:message key="label.number.abbr" /></th>
				<th><bean:message key="label.nameWord" /></th>
			</tr>
	
			<bean:define id="mailingList" value=""/>	
			<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
			<tr>		
				<td><bean:write name="infoSiteStudentInformation" property="number"/></td>	
				<td><bean:write name="infoSiteStudentInformation" property="name"/></td>		
			</tr>			
			</logic:iterate>
		</table>
	</logic:notEmpty> 
</logic:present>

<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
</logic:notPresent>