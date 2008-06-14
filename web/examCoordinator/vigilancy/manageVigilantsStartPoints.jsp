<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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