<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<p><b><bean:message key="label.grant.project.information"/></b></p>

	<table class="infoop">
		<tr>
			<td rowspan=4><p class="emphasis-box">i</p></td>
		    <td><bean:message key="info.grant.manage.grantproject.information"/></td>
		</tr>
		<tr>
			<td><bean:message key="info.grant.manage.grantproject.edit"/>"<bean:message key="link.edit"/>".</td>
		</tr>
		<tr>
			<td><bean:message key="info.grant.manage.grantproject.create"/>"<bean:message key="link.create.grant.project"/>".</td>
		</tr>
	</table><br/><br/>


<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<logic:present name="infoGrantProjectList">

	<%-- Create a new Grant Project --%>
	<bean:message key="message.grant.project.creation"/>:&nbsp;
	<html:link page="/editGrantProject.do?method=prepareEditGrantProjectForm">
		<bean:message key="link.create.grant.project"/>
	</html:link><br/><br/>

    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with grant type description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.project.number"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.project.designation"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.project.responsibleTeacher.number"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.project.grantCostCenter.number"/>
        </th>
        <th class="listClasses-header">&nbsp;</th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantProject" name="infoGrantProjectList">
        <tr>
            <td class="listClasses">&nbsp;
	            <bean:write name="infoGrantProject" property="number"/>
            </td>
            <td class="listClasses">&nbsp;
	            <bean:write name="infoGrantProject" property="designation"/>
            </td>
            <td class="listClasses">&nbsp;
            	<logic:present name="infoGrantProject" property="infoResponsibleTeacher">
		            <bean:write name="infoGrantProject" property="infoResponsibleTeacher.teacherNumber"/>
		        </logic:present>
            </td>
            <td class="listClasses">&nbsp;
               	<logic:present name="infoGrantProject" property="infoGrantCostCenter">
		            <bean:write name="infoGrantProject" property="infoGrantCostCenter.number"/>
		        </logic:present>
            </td>
            <td class="listClasses">
		            <%-- Edit a Grant Project --%>
                    <bean:define id="idGrantProject" name="infoGrantProject" property="idInternal"/>
                    <html:link page='<%= "/editGrantProject.do?method=prepareEditGrantProjectForm&amp;idGrantProject=" + idGrantProject.toString() %>' > 
                        <bean:message key="link.edit" />
                    </html:link>        
            </td>       
        </tr>
    </logic:iterate>
    </table>
</logic:present>
    
<%-- If there are no grant projects --%>
<logic:notPresent name="infoGrantProjectList">
    <p align="center"><bean:message key="message.grant.project.nonExistentProjects" /></p>
</logic:notPresent>   	

<br/><br/>
<%-- Create a new Grant Project --%>
<bean:message key="message.grant.project.creation"/>:&nbsp;
<html:link page="/editGrantProject.do?method=prepareEditGrantProjectForm">
	<bean:message key="link.create.grant.project"/>
</html:link>


</logic:messagesNotPresent>