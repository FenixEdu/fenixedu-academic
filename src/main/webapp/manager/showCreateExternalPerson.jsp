<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<html:xhtml/>

<br/>
<h2><strong><bean:message key="link.create.external.person" bundle="MANAGER_RESOURCES"/></strong></h2>
<br/>
<h2><strong><bean:message key="label.login.info" bundle="MANAGER_RESOURCES" /></strong></h2>
<logic:iterate id="identification" name="person" property="user.identifications">
	<fr:view name="identification" schema="net.sourceforge.fenixedu.domain.Login" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
<br/>
</logic:iterate>
<br/>
<h2><strong><bean:message key="label.person.title.personal.info" bundle="MANAGER_RESOURCES" /></strong></h2>
<fr:view name="person" schema="net.sourceforge.fenixedu.domain.Person" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
<br/>
<h2><strong><bean:message key="label.person.unit.info" bundle="MANAGER_RESOURCES"/></strong></h2>
<logic:iterate id="accountability" name="person" property="parentsSet">
	<fr:view name="accountability" property="parentParty" schema="net.sourceforge.fenixedu.domain.organizationalStructure.Unit.name">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:iterate>
