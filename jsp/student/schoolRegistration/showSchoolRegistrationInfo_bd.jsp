<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<bean:define id="infoEnrollments" name="infoEnrollments" scope="request"/>
<bean:define id="infoClass" name="infoClass" scope="request"/>

<html:form action="<%= "/viewEnrollments?method=declarations&amp;degreeName="+ request.getAttribute("degreeName")%>">
 <strong>Página 5 de 6</strong>
<p align="center"><span class="error"><html:errors/></span></p>
<p align="center">
	<logic:present name="studentRegistered" scope="request">
		<p align="center"><span class="error"><%= request.getAttribute("studentRegistered") %></span></p>
	</logic:present>
	<logic:notPresent name="studentRegistered" scope="request">
		<p align="center"><b><font color="red"><bean:message key="label.register.success" /></font></b></p>
	</logic:notPresent>
</p>
<br/></br/>
<p align="center"><b><bean:message key="label.enrollments" /></b><br/></p>

	<table border="0" cellpadding="5" cellspacing="0" align="center">
		<tr>
		  	<td class="listClasses-header">
		  		<bean:message key="label.curricular.course.name" bundle="DEFAULT"/>
		  	</td>
		  	<td class="listClasses-header">
		  		<bean:message key="label.class" bundle="DEFAULT" />
		  	</td>
		</tr>
	  	
		<logic:iterate id="enrolment" name="infoEnrollments">
	  		<tr>
			  <td class="listClasses">
			    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
			  </td>
 			  <td class="listClasses">
			  	<bean:write name="infoClass" property="nome"/>
			  </td>
			 </tr>
		</logic:iterate>
		
	</table>
<p align="center"><html:submit value="Continuar" styleClass="inputbutton"/></p>
</html:form>
