<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentGroupInformation.description" />
				</td>
			</tr>
		</table>
	<br>
		<br>
		
	
	
	<table width="50%" cellpadding="0" border="0">
	<tbody>
	
	<logic:empty name="component" property="infoSiteStudentInformationList">
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	</logic:empty> 

	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
		
	
	<tr>
		<td class="listClasses-header"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.emailWord" />
		</td>
	</tr>
	
	<%Map sendMailParameters = new TreeMap(request.getParameterMap());
          sendMailParameters.put("method","prepare");
		  request.setAttribute("sendMailParameters",sendMailParameters);%>
	<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
	   <html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
	   </html:link>
	<bean:define id="mailingList" value=""/>	
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
		<tr>		
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td class="listClasses">
				<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
				<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
			</td>
		</tr>
		
			<bean:define id="aux" name="mailingList"/>
			<logic:lessThan name="aux" value="1">
				<bean:define id="mailingList" value="<%= mail.toString() %>"/>	
			</logic:lessThan>
			<logic:greaterThan name="aux" value="0">
				<bean:define id="mailingList" value="<%= aux + ";"+ mail  %>"/>	
			</logic:greaterThan>
				
	 </logic:iterate>
	 

	


	</logic:notEmpty> 
</tbody>
</table>

	<html:form action="/viewStudentGroups" method="get">
	
		<html:submit styleClass="inputbutton"><bean:message key="button.back"/>                    		         	
		</html:submit>
	
		<html:hidden property="method" value="viewStudentGroups"/>
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
		<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
	</html:form>

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h2>
</logic:notPresent>
