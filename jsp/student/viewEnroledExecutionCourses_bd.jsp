<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	


<logic:present name="infoExecutionCourses"> 
<html:form action="/viewExecutionCourseProjects" method="get">


	<logic:empty name="infoExecutionCourses">
		<em><bean:message key="title.student.portalTitle"/></em>
		<h2><bean:message key="label.student.groupEnrollment.title"/></h2>
		<p class="mtop15"><span class="warning0"><bean:message key="message.executionCourses.not.available"/></span></p>
		<p>
			<span class="error"><!-- Error messages go here --><html:errors /></span>
		</p>
	</logic:empty>


	<logic:notEmpty name="infoExecutionCourses">
		<em><bean:message key="title.student.portalTitle"/></em>
		<h2><bean:message key="label.student.groupEnrollment.title"/></h2>
	
		<div class="infoop2"><bean:message key="label.student.viewEnroledExecutionCourses.description" />.</div>
	
		<p class="mtop15"><em><bean:message key="title.ChooseExecutionCourse"/></em>:</p>
	
		<p>
			<span class="error0"><!-- Error messages go here --><html:errors /></span>
		</p>


	<table class="tstyle4">	
		<tr>
			<th></th>
		 	<th><bean:message key="label.executionCourse"/></th>
		 	<th><bean:message key="Label.groupings"/></th>
		</tr>
		
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCourses">
			<tr>
				<td><bean:define id="executionCourseID" name="infoExecutionCourse" property="idInternal" type="java.lang.Integer"/>
					<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseCode" property="executionCourseCode" value="<%= executionCourseID.toString() %>"/>
			 	</td>
			 	<td><bean:write name="infoExecutionCourse" property="nome"/></td>
			 	<td style="line-height: 200%;">
			 		<ul style="text-align: left; margin: 0 1em; padding: 0.5em;">
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
	
<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.choose"/></html:submit>
</p>





</logic:notEmpty>

</html:form>	

</logic:present>

<logic:notPresent name="infoExecutionCourses">
		
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
	<br/>
	span class="error"><html:errors/></span>
	<br/>
	
</logic:notPresent>



   
