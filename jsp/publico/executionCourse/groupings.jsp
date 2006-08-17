<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="groupings" name="executionCourse" property="groupings"/>
<logic:empty name="groupings">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
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
				<bean:define id="url" type="java.lang.String">/executionCourse.do?method=grouping&amp;groupingID=<bean:write name="grouping" property="idInternal"/></bean:define>
				<html:link page="<%= url %>"
						paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
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
