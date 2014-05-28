<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present name="executionCourses"> 
<fr:form action="/viewExecutionCourseProjects.do">


	<logic:empty name="executionCourses">
		<h2><bean:message key="label.student.groupEnrollment.title"/></h2>
		<p class="mtop15"><em><bean:message key="message.executionCourses.not.available"/></em></p>
		<p>
			<span class="error"><!-- Error messages go here --><html:errors /></span>
		</p>
	</logic:empty>


	<logic:notEmpty name="executionCourses">
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
		
		<logic:iterate id="executionCourse" name="executionCourses">
			<tr>
				<td><bean:define id="executionCourseID" name="executionCourse" property="externalId" type="java.lang.String"/>
					<input type="radio" name="executionCourseCode" value="${executionCourseID}"/>
			 	</td>
			 	<td><bean:write name="executionCourse" property="nome"/></td>
			 	<td style="line-height: 200%;">
			 		<ul style="text-align: left; margin: 0 1em; padding: 0.5em;">
					<logic:iterate id="grouping" name="executionCourse" property="groupingsToEnrol" length="1">
						<li><bean:write name="grouping" property="name"/></li>
					</logic:iterate>
					<logic:iterate id="grouping" name="executionCourse" property="groupingsToEnrol" offset="1">
						<li><bean:write name="grouping" property="name"/></li>
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
</fr:form>	
</logic:present>

<logic:notPresent name="executionCourses">
		
	<h2><bean:message key="message.executionCourses.not.available"/></h2>
	<br/>
	span class="error"><html:errors/></span>
	<br/>
</logic:notPresent>
