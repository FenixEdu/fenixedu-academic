<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="infoSiteStudentGroup">

<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
	<em><bean:message key="title.student.portalTitle"/></em>
	<h2><bean:message key="title.RemoveEnrolment"/></h2>
	<p class="mtop15"><span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span></p>
</logic:empty> 

	<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>
	
	<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">


<div class="dinline forminline">

	<html:form action="/removeGroupEnrolment" method="get" style="margin: 0; padding: 0;">
	
	<em><bean:message key="title.student.portalTitle"/></em>
	<h2><bean:message key="title.RemoveEnrolment"/></h2>

	<div class="infoop2">
		<bean:message key="label.student.removeStudentInGroup.description" />
	</div>

	<p><strong><bean:message key="label.StudentGroup"/></strong></p>

	<table class="tstyle4" width="70%" cellpadding="0" border="0">
	<tr>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>
	
	 <bean:define id="mailingList" value=""/>
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">			
		<tr>		
			<td><bean:write name="infoSiteStudentInformation" property="number"/></td>	
			<td><bean:write name="infoSiteStudentInformation" property="name"/></td>		
			<td>
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


	<p class="mvert15"><bean:message key="label.confirmGroupStudentUnrolment"/></p>

	

			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.remove"/>                    		         	
			</html:submit> 
		
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="remove"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode")%>"/>	
			<logic:present name="shiftCode">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
			</logic:present>
	</html:form>


				<html:form action="/viewStudentGroupInformation" method="get" style="margin: 0; padding: 0;">
	
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
					</html:cancel>
		
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
					<logic:present name="shiftCode">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
					</logic:present>				
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode")%>"/>	

				</html:form>
</div>
	
</logic:notEmpty> 


</logic:present>

<logic:notPresent name="infoSiteStudentGroup">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:notPresent>