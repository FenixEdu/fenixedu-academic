<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.vigilant"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.incompatibilities"/></h2>
<ul>
<li><html:link page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link></li>
</ul>

<strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.incompatiblePerson"/></strong><br>
<logic:present name="vigilant" property="incompatiblePerson">
	<fr:view name="vigilant" property="incompatiblePerson.name"/>
	<html:link page="/vigilancy/vigilantManagement.do?method=removeIncompatiblePerson"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/></html:link>
</logic:present>
<logic:notPresent name="vigilant" property="incompatiblePerson">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noIncompatiblePerson"/>
</logic:notPresent>

<p><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilants"/></strong><br>
<fr:view name="vigilants" schema="presentVigilantName">
<fr:layout name="tabular">
<fr:property name="link(Adicionar)" value="/vigilancy/vigilantManagement.do?method=addIncompatiblePerson"/>
<fr:property name="param(Adicionar)" value="person.idInternal/oid"/>
<fr:property name="displayHeaders" value="false"/>
</fr:layout>
</fr:view>