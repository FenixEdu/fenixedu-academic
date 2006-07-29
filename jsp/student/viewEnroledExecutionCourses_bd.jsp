<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	


<logic:present name="infoExecutionCourses"> 
<html:form action="/viewExecutionCourseProjects" method="get">


	<logic:empty name="infoExecutionCourses">
		<h2><bean:message key="message.executionCourses.not.available"/></h2>
		<br/>
		<span class="error"><html:errors/></span>
		<br/>
	</logic:empty>


	<logic:notEmpty name="infoExecutionCourses">

		<h2><bean:message key="label.student.groupEnrollment.title"/></h2>
	
		<div class="infoop2"><bean:message key="label.student.viewEnroledExecutionCourses.description" /></div>
	
		<p><em><bean:message key="title.ChooseExecutionCourse"/></em>:</p>
	
		<span class="error"><html:errors/></span>


	<table class="style1">	
		<tr>
			<td class="listClasses-header"></td>
		 	<th class="listClasses-header"><bean:message key="label.executionCourse"/></th>
		 	<th class="listClasses-header"><bean:message key="Label.groupings"/></th>
		</tr>
		
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCourses">
			<tr>
				<td class="listClasses"><bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" type="java.lang.Integer"/>
					<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseCode" property="executionCourseCode" value="<%= executionCourseID.toString() %>"/>
			 	</td>
			 	<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/></td>
			 	<td class="listClasses" style="line-height: 200%;">
			 		<ul style="text-align: left; margin: 0 0.5em; padding: 0.5em;">
					<logic:iterate id="infoGrouping" name="infoExecutionCourse" property="infoGroupings" length="1">
						<li><bean:write name="infoGrouping" property="name"/></li>
					</logic:iterate>
					<logic:iterate id="infoGrouping" name="infoExecutionCourse" property="infoGroupings" offset="1">
						<li><bean:write name="infoGrouping" property="name"/></li>
					</logic:iterate>
					</ul>
			 	</td>
			</tr>
			
		</logic:iterate>

	</table>
	
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.choose"/></html:submit>
<br />





</logic:notEmpty>

</html:form>	

</logic:present>

<logic:notPresent name="infoExecutionCourses">
		
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
	<br/>
	span class="error"><html:errors/></span>
	<br/>
	
</logic:notPresent>



   
