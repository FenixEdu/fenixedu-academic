<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />


<logic:empty name="component" property="infoGroupPropertiesList">
	<br/>
	<br/>
	<h2><bean:message key="message.infoNewProjectsProposalsList.not.available" /></h2>
	<br/>
	<br/>	
	<html:link page="<%="/viewExecutionCourseProjects.do?method=prepareViewExecutionCourseProjects&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
    	<bean:message key="link.backToProjectsAndLink"/></html:link><br/>
	<br/>
</logic:empty>

	
<logic:notEmpty name="component" property="infoGroupPropertiesList"> 
 
	<h2><bean:message key="title.NewProjectProposals"/></h2>
	
	<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.newProjectProposals.description" />
		</td>
	</tr>
	</table>
	<br/>

	<span class="error"><html:errors/></span> 	
	
<br/>
	<html:link page="<%="/viewExecutionCourseProjects.do?method=prepareViewExecutionCourseProjects&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
    	<bean:message key="link.backToProjectsAndLink"/></html:link>
<br/>
<br/>
<table width="100%" border="0" style="text-align: left;">
        <tbody>
    	 <tr>
			<th class="listClasses-header" width="25%" ><bean:message key="label.projectName" />
			</th>
			<th class="listClasses-header" width="45%" ><bean:message key="label.projectDescription" />
			</th>
			<th class="listClasses-header" width="30%" ><bean:message key="label.newProjectProposalExecutionCourses" />
			</th>
		</tr>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
                <tr>
                    <td class="listClasses" align="left">
                        <b><html:link page="<%= "/importGroupProperties.do?method=prepareImportGroupProperties&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
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
                	
                	 
                	<td class="listClasses" align="left">
            		    <logic:iterate id="infoExportGrouping" name="infoGroupProperties" property="infoExportGroupings" >
	                		<bean:define id="infoExecutionCourse" name="infoExportGrouping" property="infoExecutionCourse" />
							<bean:write name="infoExecutionCourse" property="nome"/></br>
                    	 </logic:iterate>
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