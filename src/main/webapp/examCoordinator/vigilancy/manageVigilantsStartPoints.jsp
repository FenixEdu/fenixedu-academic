<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editStartPoints"/></h2>

<strong><bean:message key="label.vigilancy.vigilantGroup" bundle="VIGILANCY_RESOURCES"/></strong>: <em><fr:view name="group" property="name"/></em>

<p class="mtop0">
<fr:edit id="editPoints" name="vigilants" schema="editStartPoints">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5"/>

	</fr:layout>
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
	<fr:destination name="success" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
	<fr:hasMessages for="editPoints">
		<p>
			<span class="error0">			
			<fr:message for="editPoints" show="message"/>
			</span>
		</p>
	</fr:hasMessages>

</fr:edit>
</p>