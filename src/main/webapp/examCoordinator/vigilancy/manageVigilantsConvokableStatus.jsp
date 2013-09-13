<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilantPermissions"/></h2>

<strong><bean:message key="label.vigilancy.vigilantGroup" bundle="VIGILANCY_RESOURCES"/></strong>: <em><fr:view name="group" property="name"/></em>


<fr:edit name="vigilantWrappers" schema="editConvokableStatus">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5"/>

	</fr:layout>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
	<fr:destination name="success" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>

</fr:edit>
