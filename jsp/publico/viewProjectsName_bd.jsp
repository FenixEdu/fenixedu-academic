<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoGroupPropertiesList">
		<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>

	<logic:notEmpty name="component" property="infoGroupPropertiesList">
        <h2><bean:message key="label.groupings"/></h2>
		<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
			<tbody>
			<tr>
				<th><bean:message key="label.groupingName" /></th>
				<th><bean:message key="label.groupingDescription" /></th>
				<th><bean:message key="label.executionCourses" /></th>
			</tr>
			<logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
			<tr>
				<td>
					<html:link page="<%= "/viewSite.do" + "?method=viewShiftsAndGroupsAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) %>" paramId="groupProperties" paramName="infoGroupProperties" paramProperty="idInternal">
						<b><bean:write name="infoGroupProperties" property="name"/></b>
					</html:link>
				</td>
				<td>
					<logic:notEmpty name="infoGroupProperties" property="projectDescription">
						<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
			      	</logic:notEmpty>
                	
					<logic:empty name="infoGroupProperties" property="projectDescription">
				      	<bean:message key="message.project.wihtout.description"/>
				   	</logic:empty>
			   	</td>
			   	
			   	<td>
                	<bean:size id="count" name="infoGroupProperties" property="infoExportGroupings"/>
                		<logic:greaterThan name="count" value="1">
            		    	<logic:iterate id="infoGroupPropertiesExecutionCourseElement" name="infoGroupProperties" property="infoExportGroupings" >
                				<bean:define id="infoExecutionCourse" name="infoGroupPropertiesExecutionCourseElement" property="infoExecutionCourse" />
									<bean:write name="infoExecutionCourse" property="nome"/></br>
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
</logic:present>

<logic:notPresent name="siteView" property="component">
	<h4><bean:message key="message.infoGroupPropertiesList.not.available" /></h4>
</logic:notPresent>
