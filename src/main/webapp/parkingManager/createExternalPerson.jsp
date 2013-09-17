<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<html:xhtml/>

<em><bean:message key="label.parking"/></em>
<h2><bean:message key="link.create.external.person"/></h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors/></span>
</p>

<fr:form action="/externalPerson.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExternalPersonAndParkingParty"/>

	<h3 class="mtop2 mbottom05"><bean:message key="label.person.unit.info" bundle="MANAGER_RESOURCES" /></h3>
	<fr:edit id="unitData" name="externalPersonBean" schema="net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean.allUnits" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom05 thmiddle"/>
	        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>

	<h3 class="mtop15 mbottom05"><bean:message key="label.person.title.personal.info" bundle="MANAGER_RESOURCES" /></h3>
	<fr:edit id="personData" name="externalPersonBean" schema="net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 thmiddle"/>
	        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>

	<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
