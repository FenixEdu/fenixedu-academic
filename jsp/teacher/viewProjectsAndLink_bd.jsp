<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

	<h2><bean:message key="title.ExecutionCourseProjects"/></h2>
	<br>
	
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.viewProjectsAndLink.description" />
		</td>
	</tr>
</table>

	<logic:empty name="component" property="infoGroupPropertiesList">
	<h2><bean:message key="message.infoGroupPropertiesList.not.available" /></h2>
	</logic:empty>
	
	
<br>
<br>
<b><html:link page="/createGroupProperties.do?method=prepareCreateGroupProperties" paramId="objectCode" paramName="objectCode" ><bean:message key="link.groupPropertiesDefinition"/></html:link></b>
<br>
<br>
<br>
 
 <logic:notEmpty name="component" property="infoGroupPropertiesList"> 
<table border="0" style="text-align: left;">
        <tbody>
    	 <tr>
			<td class="listClasses-header" width="20%" ><bean:message key="label.projectName" />
			</td>
			<td class="listClasses-header" width="45%" ><bean:message key="label.projectDescription" />
			</td>
			<td class="listClasses-header" width="35%" ><bean:message key="label.properties" />
			</td>
		</tr>
            <logic:iterate id="infoGroupProperties" name="component" property="infoGroupPropertiesList" >
                <tr>
                    <td class="listClasses" align="left">
                        <b><html:link page="<%= "/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="groupPropertiesCode" paramName="infoGroupProperties" paramProperty="idInternal">
							<bean:write name="infoGroupProperties" property="name"/></html:link></b>
                    		
                    </td>
                    
                    <td class="listClasses">
                    
             		<logic:notEmpty name="infoGroupProperties" property="projectDescription">
                     	<bean:write name="infoGroupProperties" property="projectDescription"/>
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