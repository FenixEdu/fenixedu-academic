<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html:xhtml/>

<br/>
<h2><strong><bean:message key="link.create.external.person"/></strong></h2>
<br/>

<span class="error"><!-- Error messages go here --><html:errors/><br /></span>

<fr:form action="/externalPerson.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExternalPersonAndParkingParty"/>

	<h2><strong><bean:message key="label.person.title.personal.info" bundle="MANAGER_RESOURCES" /></strong></h2>
	<fr:edit id="personData" name="externalPersonBean" schema="net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1"/>
	        <fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<h2><strong><bean:message key="label.person.unit.info" bundle="MANAGER_RESOURCES" /></strong></h2>
	<fr:edit id="unitData" name="externalPersonBean" schema="net.sourceforge.fenixedu.dataTransferObject.person.ExternalPersonBean.Unit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1"/>
	        <fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
