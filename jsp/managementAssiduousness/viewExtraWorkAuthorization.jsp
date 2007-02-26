<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.viewExtraWorkAuthorization" /></h2>


<p><html:link page="/manageExtraWorkAuthorization.do?method=prepareEditExtraWorkAuthorization" 
	paramName="extraWorkAuthorization" paramProperty="idInternal" paramId="authorizationID">
		<bean:message key="link.editExtraWorkAuthorization" bundle="ASSIDUOUSNESS_RESOURCES"/></html:link></p>
		
<fr:view name="extraWorkAuthorization" schema="view.extraWorkAuthorization">
	<fr:layout name="tabular">
	</fr:layout>
</fr:view>
	
<fr:view name="extraWorkAuthorization" property="employeeExtraWorkAuthorizations" layout="tabular" schema="view.employeeExtraWorkAuthorization">
	<fr:layout>
		<fr:property name="classes" value="tstyle8 thleft thlight emphasis2"/>
	</fr:layout>
</fr:view>

<p><html:link page="/manageExtraWorkAuthorization.do?method=prepareEditExtraWorkAuthorization" 
	paramName="extraWorkAuthorization" paramProperty="idInternal" paramId="authorizationID">
		<bean:message key="link.editExtraWorkAuthorization" bundle="ASSIDUOUSNESS_RESOURCES"/></html:link></p>
		
<i><bean:message key="label.subtitle" bundle="ASSIDUOUSNESS_RESOURCES"/>:</i><br/>
<strong><bean:message key="label.A" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.normalExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>
<strong><bean:message key="label.B" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.normalExtraWorkB" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>
<strong><bean:message key="label.C" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.nightExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>
<strong><bean:message key="label.D" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.weeklyRestExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>
<strong><bean:message key="label.E" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.auxiliarPersonel" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>
<strong><bean:message key="label.F" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.executiveAuxiliarPersonel" bundle="ASSIDUOUSNESS_RESOURCES"/><br/>

			
