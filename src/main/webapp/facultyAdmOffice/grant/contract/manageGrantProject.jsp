<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<logic:present name="grantProjectList">

	<%-- Create a new Grant Project --%>
	<p>
		<bean:message key="message.grant.project.creation"/>:
		<html:link page="/editGrantProject.do?method=prepareEditGrantProjectForm">
			<bean:message key="link.create.grant.project"/>
		</html:link>
	</p>
	<fr:view name="grantProjectList" schema="show.grantProject">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
			<fr:property name="columnClasses" value="acenter" />
			<fr:property name="headerClasses" value="acenter" />
            <fr:property name="link(edit)" value="/editGrantProject.do?method=prepareEditGrantProjectForm" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="param(edit)" value="externalId/idGrantProject" />
		</fr:layout>
	</fr:view> 
                    
</logic:present>
    
<%-- If there are no grant projects --%>
<logic:notPresent name="grantProjectList">
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