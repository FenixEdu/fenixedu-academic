<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<bean:define id="infoEnrollments" name="infoEnrollments" scope="request"/>
<bean:define id="infoClass" name="infoClass" scope="request"/>

<html:form action="/viewEnrollments?method=start">
<p align="center"><span class="error"><html:errors/></span></p>
<p align="center">
<p align="center">O aluno está inscrito em...<br/></p>

	<table border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
		  	<td class="listClasses-header">
		  		<bean:message key="label.curricular.course.name" />
		  	</td>
		  	<td class="listClasses-header">
		  		<bean:message key="label.class" />
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
