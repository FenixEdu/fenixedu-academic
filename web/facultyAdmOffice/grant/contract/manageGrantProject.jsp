<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.project.information"/></h2>

<div class="infoop2">
	<p><bean:message key="info.grant.manage.grantproject.information"/></p>
	<p><bean:message key="info.grant.manage.grantproject.edit"/>"<bean:message key="link.edit"/>".</p>
	<p><bean:message key="info.grant.manage.grantproject.create"/>"<bean:message key="link.create.grant.project"/>".</p>
</div>


<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<logic:present name="infoGrantProjectList">

	<%-- Create a new Grant Project --%>
	<p>
		<bean:message key="message.grant.project.creation"/>:
		<html:link page="/editGrantProject.do?method=prepareEditGrantProjectForm">
			<bean:message key="link.create.grant.project"/>
		</html:link>
	</p>

    <table class="tstyle4">
    <%-- Table with grant type description rows --%>
    <tr>
        <th>
            <bean:message key="label.grant.project.number"/>
        </th>
        <th>
            <bean:message key="label.grant.project.designation"/>
        </th>
        <th>
            <bean:message key="label.grant.project.responsibleTeacher.number"/>
        </th>
        <th>
            <bean:message key="label.grant.project.grantCostCenter.number"/>
        </th>
        <th></th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoGrantProject" name="infoGrantProjectList">
        <tr>
            <td>
	            <bean:write name="infoGrantProject" property="number"/>
            </td>
            <td>
	            <bean:write name="infoGrantProject" property="designation"/>
            </td>
            <td class="acenter">
            	<logic:present name="infoGrantProject" property="infoResponsibleTeacher">
		            <bean:write name="infoGrantProject" property="infoResponsibleTeacher.teacherNumber"/>
		        </logic:present>
            </td>
            <td class="acenter">
               	<logic:present name="infoGrantProject" property="infoGrantCostCenter">
		            <bean:write name="infoGrantProject" property="infoGrantCostCenter.number"/>
		        </logic:present>
            </td>
            <td>
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
    <p><bean:message key="message.grant.project.nonExistentProjects" /></p>
</logic:notPresent>   	


<%-- Create a new Grant Project --%>
<p>
	<bean:message key="message.grant.project.creation"/>:
	<html:link page="/editGrantProject.do?method=prepareEditGrantProjectForm">
		<bean:message key="link.create.grant.project"/>
	</html:link>
</p>


</logic:messagesNotPresent>