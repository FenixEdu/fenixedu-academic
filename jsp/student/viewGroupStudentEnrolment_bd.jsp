<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="infoSiteStudentInformationList">

<html:form action="/groupStudentEnrolment" method="get">
	
	<logic:empty name="infoSiteStudentInformationList">
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	</logic:empty> 
	


	<logic:notEmpty name="infoSiteStudentInformationList">
	<table width="50%" cellpadding="0" border="0">
		<h2><bean:message key="title.GroupStudentEnrolment"/></h2>
	
		
		<h2><bean:message key="label.StudentGroup"/></h2>
	<tr>
		<td class="listClasses-header"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header"><bean:message key="label.emailWord" />
		</td>
	</tr>
	
	 <bean:define id="mailingList" value=""/>
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentInformationList">			
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
	 		
	 
	 

	</table>

</logic:notEmpty>
<br>
<br>
<bean:message key="label.confirmGroupStudentEnrolment" />

<br>
<br>
	<table>
		<tr>
			  <td><html:submit styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/>
				</html:submit>

				<html:hidden property="method" value="enrolment"/>
				<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />	
				<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />	

				</html:form>
			  </td>
				
			<td>
			<html:form action="/viewProjectStudentGroups" method="get">
	
			<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
			</html:cancel>
			<html:hidden property="method" value="execute"/>
			<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
			</html:form>
			</td>
		</tr>
	</table>
</logic:present>






<logic:notPresent name="infoSiteStudentInformationList">
<h4>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h4>
</logic:notPresent>