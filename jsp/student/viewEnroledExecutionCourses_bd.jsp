<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	


<logic:present name="infoExecutionCourses"> 
<html:form action="/viewExecutionCourseProjects" method="get">


	<logic:empty name="infoExecutionCourses">
		<h2><bean:message key="message.executionCourses.not.available"/></h2>
		<br>
		<span class="error"><html:errors/></span>
		<br>
	</logic:empty>

	<logic:notEmpty name="infoExecutionCourses">
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.student.viewEnroledExecutionCourses.description" />
			</td>
		</tr>
		</table>
		<br>
		<br>
 		<h2><bean:message key="title.ChooseExecutionCourse"/></h2>
		<br>

		<span class="error"><html:errors/></span>
		<br>

	<table width="50%" cellpadding="0" border="0">	
		<tr>
			<td>
		 	</td>
		 	<td>
		 		<bean:message key="label.executionCourse"/>
		 	</td>
		 	<td>
			 	<bean:message key="Label.groupings"/>
		 	</td>
		</tr>
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCourses">
			<tr>
				<td>
					<bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" type="java.lang.Integer"/>
					<html:radio property="executionCourseCode" value="<%= executionCourseID.toString() %>"/>
			 	</td>
				<td>
					<bean:write name="infoExecutionCourse" property="nome"/>
			 	</td>
				<td>
					<logic:iterate id="infoGrouping" name="infoExecutionCourse" property="infoGroupings" length="1">
						<bean:write name="infoGrouping" property="name"/>
					</logic:iterate>
					<logic:iterate id="infoGrouping" name="infoExecutionCourse" property="infoGroupings" offset="1">
						, <bean:write name="infoGrouping" property="name"/>
					</logic:iterate>
			 	</td>
			</tr>
		</logic:iterate>
		<tr>
			<td>
    	</td>
    	</tr>	

	</table>
	<br>
	<br>
	<br>
<html:submit styleClass="inputbutton"><bean:message key="button.choose"/>                    		         	
</html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br>
</logic:notEmpty>

</html:form>	

</logic:present>

<logic:notPresent name="infoExecutionCourses">
		
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
	<br>
	span class="error"><html:errors/></span>
	<br>
	
</logic:notPresent>



   
