<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>


<br />
<h2><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>



<fr:edit id="searchBean" name="searchBean" schema="ExternalUnitsSearchBean.search" action="/externalUnits.do?method=search" >
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="/externalUnits.do?method=prepareSearch" />
</fr:edit>


<logic:present name="searchBean" property="results">

	<logic:notEmpty name="searchBean" property="results">
		<fr:view name="searchBean" property="results" schema="AbstractExternalUnitResultBean.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight"/>
				<fr:property name="columnClasses" value=",,acenter,acenter,acenter,acenter"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="searchBean" property="results">
		<p>
			<em><bean:message key="label.externalUnits.noSearchResults" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>


	<bean:define id="earthUnitId">&amp;oid=<bean:write name="searchBean" property="earthUnit.externalId" /></bean:define>
	<ul>
		<li><html:link page="<%="/externalUnits.do?method=prepareCreateCountry" + earthUnitId %>"><bean:message key="label.externalUnits.createCountry" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>


</logic:present>

