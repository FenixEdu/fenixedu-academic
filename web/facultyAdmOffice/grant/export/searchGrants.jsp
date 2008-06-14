<%@ page language="java"%>
<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />
<em><bean:message key="label.facultyAdmOffice.portal.name" /></em>
<h2><bean:message key="link.export.grants" /></h2>

<logic:present name="grantSearch">
	<fr:form action="/exportGrants.do?method=searchGrants">
		<h3><bean:message key="label.contract" /></h3>
		<fr:edit id="grantSearch" name="grantSearch" schema="search.grants.contract">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		<h3><bean:message key="label.subsidy" /></h3>
		<fr:edit id="grantSearch2" name="grantSearch" schema="search.grants.subsidy">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		<h3><bean:message key="label.insurance" /></h3>
		<fr:edit id="grantSearch3" name="grantSearch" schema="search.grants.insurance">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.export" property="export">
			<bean:message key="link.export"/>
		</html:submit></p>
	</fr:form>
	<bean:define id="searchList" name="grantSearch" property="search" />
	<logic:notEmpty name="searchList">
		<bean:size id="searchResultNumber" name="searchList"/>
		<bean:message key="message.searchResultNumber" arg0="<%= searchResultNumber.toString()%>"/>
		<fr:view name="searchList" schema="show.grantContractRegime">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

