<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.facultyAdmOffice.portal.name"/></em>
<h2><bean:message key="label.grant.type.information"/></h2>

<div class="infoop2">
	<p><bean:message key="info.grant.manage.granttype.information"/></p>
	<p><bean:message key="info.grant.manage.granttype.edit"/>"<bean:message key="link.edit"/>".</p>
	<p><bean:message key="info.grant.manage.granttype.create"/>"<bean:message key="link.create.grant.type"/>".</p>
</div>

<%-- Presenting errors --%>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<logic:present name="grantTypes">

	<%-- Create a new Grant Type --%>
	<p>
		<bean:message key="message.grant.type.creation"/>:&nbsp;
		<html:link page="/editGrantType.do?method=prepareEditGrantTypeForm">
			<bean:message key="link.create.grant.type"/>
		</html:link>
	</p>

	<fr:view name="grantTypes" schema="show.grantType">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 printborder" />
			<fr:property name="columnClasses" value="acenter" />
			<fr:property name="headerClasses" value="acenter" />
            <fr:property name="link(edit)" value="/editGrantType.do?method=prepareEditGrantTypeForm" />
			<fr:property name="key(edit)" value="link.edit" />
			<fr:property name="param(edit)" value="idInternal/idGrantType" />
		</fr:layout>
	</fr:view> 
</logic:present>
    
<logic:notPresent name="grantTypes">
    <p align="center"><bean:message key="message.grant.type.nonExistentGrantTypes" /></p>
</logic:notPresent>


<%-- Create a new Grant Type --%>
<p>
	<bean:message key="message.grant.type.creation"/>:&nbsp;
	<html:link page="/editGrantType.do?method=prepareEditGrantTypeForm">
		<bean:message key="link.create.grant.type"/>
	</html:link>
</p>

</logic:messagesNotPresent>