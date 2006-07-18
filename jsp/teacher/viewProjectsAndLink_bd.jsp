<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />


	<logic:empty name="component" property="infoGroupPropertiesList">
	<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
		<logic:present name="hasProposals">
			<bean:message key="label.teacher.emptyProjectsAndLinkWithProposals.description" />
		</logic:present>
		<logic:notPresent name="hasProposals">
			<bean:message key="label.teacher.emptyProjectsAndLink.description" />
		</logic:notPresent>
		</td>
	</tr>
	</table>
	
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	
	<html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefinition"/></html:link>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<logic:present name="hasProposals">
		<html:link page="/viewNewProjectProposals.do?method=viewNewProjectProposals" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.received"/></html:link>
		</logic:present>

	</logic:empty>
	


 
 <logic:notEmpty name="component" property="infoGroupPropertiesList"> 
 
	<h2><bean:message key="title.ExecutionCourseProjects"/></h2>
	
	<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
		<logic:present name="hasProposals">
			<logic:present name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkWithProposalsAndWaiting.description" />
			</logic:present>
			<logic:notPresent name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkWithProposals.description" />
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="hasProposals">
			<logic:present name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLinkAndWaiting.description" />
			</logic:present>
			<logic:notPresent name="waitingAnswer">
				<bean:message key="label.teacher.viewProjectsAndLink.description" />
			</logic:notPresent>
		</logic:notPresent>
		</td>
	</tr>
	</table>
	<br/>

	<span class="error"><html:errors/></span> 	
<br/>
<br/>
	<html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefinition"/></html:link>
		<logic:present name="hasProposals">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<html:link page="/viewNewProjectProposals.do?method=viewNewProjectProposals" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.received"/></html:link>
		</logic:present>
		<logic:present name="waitingAnswer">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<html:link page="/viewSentedProjectProposalsWaiting.do?method=viewSentedProjectProposalsWaiting" paramId="objectCode" paramName="objectCode" ><bean:message key="link.executionCourseProposals.sented"/></html:link>
		</logic:present>
<br/>
<br/>
<br/>
<table border="0" style="text-align: left;">
        <tbody>
    	 <tr>
			<th class="listClasses-header" width="20%" ><bean:message key="label.projectName" />
			</th>
			<th class="listClasses-header" width="30%" ><bean:message key="label.projectDescription" />
			</th>
			<th class="listClasses-header" width="30%" ><bean:message key="label.properties" />
			</th>
			<th class="listClasses-header" width="20%" ><bean:message key="label.executionCourses" />
			</th>
		</tr>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
            	<tr>
                    <td class="listClasses" align="left">
                        <b><html:link page="<%= "/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
							<bean:write name="infoGroupProperties" property="name"/></html:link></b>
                    		
                    </td>
                    
                    <td class="listClasses">
                    
             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
                     	<bean:write name="infoGroupProperties" property="projectDescription" filter="false"/>
                	</logic:notEmpty>
                	
             		<logic:empty name="infoGroupProperties" property="projectDescription">
                     	<bean:message key="message.project.wihtout.description"/>
                	</logic:empty>
                	</td>
                	
                	 <td align="left" class="listClasses" >
                	 
                	 <logic:empty name="infoGroupProperties" property="maximumCapacity">
                	 <logic:empty name="infoGroupProperties" property="minimumCapacity">
                	 <logic:empty name="infoGroupProperties" property="groupMaximumNumber">
                	 	<bean:message key="message.project.wihtout.properties"/>
                	 </logic:empty>
                	 </logic:empty>
                	 </logic:empty>
                	 
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="maximumCapacity">
                	 <b><bean:message key="label.teacher.viewProjectsAndLink.MaximumCapacity"/>:</b> <bean:write name="infoGroupProperties" property="maximumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="minimumCapacity">
                	 <b><bean:message key="label.teacher.viewProjectsAndLink.MinimumCapacity"/>:</b> <bean:write name="infoGroupProperties" property="minimumCapacity"/>
                	 <br/>
                	 </logic:notEmpty>
                	 
                	 <logic:notEmpty name="infoGroupProperties" property="groupMaximumNumber">
                	 <b><bean:message key="message.groupPropertiesGroupMaximumNumber"/>:</b> <bean:write name="infoGroupProperties" property="groupMaximumNumber"/>
                   	 </logic:notEmpty>
                	</td>
                	
                	<td class="listClasses" align="left">
                		<bean:size id="count" name="infoGroupProperties" property="infoExportGroupings"/>
                		<logic:greaterThan name="count" value="1">
            		    <logic:iterate id="infoExportGrouping" name="infoGroupProperties" property="infoExportGroupings" >
                		<bean:define id="infoExecutionCourse" name="infoExportGrouping" property="infoExecutionCourse" />
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
<h2>
<bean:message key="message.infoGroupPropertiesList.not.available" />
</h2>
</logic:notPresent>