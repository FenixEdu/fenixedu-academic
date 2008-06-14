<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<script language="Javascript" type="text/javascript">
<!--
function addEmployee(){
	document.forms[0].method.value='prepareEditExtraWorkAuthorization';
	document.forms[0].addEmployee.value='yes';	
	document.forms[0].submit();
	return true;
}
// -->
</script>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.extraWorkAuthorization.edit" /></h2>

<p>
	<span class="error0"><html:errors/></span>
</p>

<fr:form action="/manageExtraWorkAuthorization.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="extraWorkAuthorizationForm" property="method" value="editExtraWorkAuthorization" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="extraWorkAuthorizationForm" property="addEmployee" value="no"/>
	
	<fr:edit id="extraWorkAuthorization" name="extraWorkAuthorizationFactory" schema="edit.extraWorkAuthorization">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	
	<ul>
		<li>
			<html:link href="javascript:addEmployee();" onclick=""><bean:message key="link.addEmployee" bundle="ASSIDUOUSNESS_RESOURCES"/></html:link>
		</li>
	</ul>
	
	
	<logic:notEmpty name="extraWorkAuthorizationFactory" property="employeesExtraWorkAuthorizations">
		<fr:edit id="employees" name="extraWorkAuthorizationFactory" property="employeesExtraWorkAuthorizations" 
			layout="tabular-editable" schema="edit.EmployeeExtraWorkAuthorizationBean">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thcenter thmiddle tdcenter"/>
				<fr:property name="columnClasses" value=""/>
			</fr:layout>
		</fr:edit>
		
		<p class="mbottom025"><em><bean:message key="label.subtitle" bundle="ASSIDUOUSNESS_RESOURCES"/>:</em></p>
		<p class="mvert025"><em><strong><bean:message key="label.A" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.normalExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
		<p class="mvert025"><em><strong><bean:message key="label.B" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.normalExtraWorkB" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
		<p class="mvert025"><em><strong><bean:message key="label.C" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.nightExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
		<p class="mvert025"><em><strong><bean:message key="label.D" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.weeklyRestExtraWork" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
		<p class="mvert025"><em><strong><bean:message key="label.E" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.auxiliarPersonel" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>
		<p class="mvert025"><em><strong><bean:message key="label.F" bundle="ASSIDUOUSNESS_RESOURCES"/></strong> - <bean:message key="label.executiveAuxiliarPersonel" bundle="ASSIDUOUSNESS_RESOURCES"/></em></p>

		
	</logic:notEmpty>
			
	<br/>											
	<html:submit><bean:message key="button.confirm" bundle="ASSIDUOUSNESS_RESOURCES"/></html:submit>
</fr:form>
