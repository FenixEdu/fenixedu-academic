
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="infoSiteStudentGroup">

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.student.removeStudentInGroup.description" />
			</td>
		</tr>
	</table>
<br>

	<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	</logic:empty> 
	
	<h2><span class="error"><html:errors/></span></h2>
	
	<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">

	<html:form action="/removeGroupEnrolment" method="get">
	<table width="50%" cellpadding="0" border="0">
	<h2><bean:message key="title.RemoveEnrolment"/></h2>
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
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">			
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
	<br>
	<br>
	<bean:message key="label.confirmGroupStudentUnrolment"/>
	<br>
	<br>
	<br>
	<table>
		<tr>
			<td>
				<html:submit styleClass="inputbutton"><bean:message key="button.remove"/>                    		         	
				</html:submit> 
			
				<html:hidden property="method" value="remove"/>
				<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
				<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
				<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode")%>"/>	
				<logic:present name="shiftCode">
				<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
				</logic:present>
				</html:form>

			</td>
			
			<td>
				<html:form action="/viewStudentGroupInformation" method="get">
	
				<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
				</html:cancel>
	
				<html:hidden property="method" value="execute"/>
				<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
				<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
				<logic:present name="shiftCode">
				<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
				</logic:present>				
				<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode")%>"/>	
			
			
				</html:form>
			</td>
		</tr>
	</table>
	
	</logic:notEmpty> 


</logic:present>

<logic:notPresent name="infoSiteStudentGroup">
<h4>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h4>
</logic:notPresent>