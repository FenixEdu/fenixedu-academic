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
<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="groupings" name="executionCourse" property="groupings"/>
<logic:empty name="groupings">
	<h2><bean:message key="label.groupings"/></h2>
	<p><em><bean:message key="message.infoGroupPropertiesList.not.available" /></em></p>
</logic:empty>

<logic:notEmpty name="groupings">
       <h2><bean:message key="label.groupings"/></h2>
	<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
		<tbody>
		<tr>
			<th><bean:message key="label.groupingName" /></th>
			<th><bean:message key="label.groupingDescription" /></th>
			<th><bean:message key="label.executionCourses" /></th>
		</tr>
		<logic:iterate id="grouping" name="groupings">
		<tr>
			<td>
				<bean:define id="url" type="java.lang.String">/executionCourse.do?method=grouping&amp;groupingID=<bean:write name="grouping" property="externalId"/></bean:define>
				<html:link page="<%= url %>"
						paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
					<b><bean:write name="grouping" property="name"/></b>
				</html:link>
			</td>
			<td>
				<logic:notEmpty name="grouping" property="projectDescription">
					<bean:write name="grouping" property="projectDescription" filter="false"/>
		      	</logic:notEmpty>
               	
				<logic:empty name="grouping" property="projectDescription">
			      	<bean:message key="message.project.wihtout.description"/>
			   	</logic:empty>
		   	</td>
		   	
		   	<td>
               	<bean:size id="count" name="grouping" property="exportGroupings"/>
               		<logic:greaterThan name="count" value="1">
           		    	<logic:iterate id="exportGrouping" name="grouping" property="exportGroupings" >
               				<bean:define id="otherExecutionCourse" name="exportGrouping" property="executionCourse" />
								<bean:write name="otherExecutionCourse" property="nome"/><br/>
                   	 	</logic:iterate>
                   	</logic:greaterThan>
					<logic:equal name="count" value="1">
						<bean:message key="message.project.wihtout.coavaliation"/>
                   	</logic:equal>
                   </td>
		</tr>
		</logic:iterate>
		</tbody>
	</table>
</logic:notEmpty>
