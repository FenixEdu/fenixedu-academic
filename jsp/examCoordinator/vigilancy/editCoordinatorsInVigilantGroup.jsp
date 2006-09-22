<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.person.vigilancy.editVigilantGroup"/></h2>
<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/examCoordinator/vigilancy/checkall.js"></script>

<ul>
<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages" /></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<div id="coordinatorsInGroup">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.coordinatorsGroup"/></strong></p>

<fr:form id="removeCoordinatorsForm" action="/vigilancy/vigilantGroupManagement.do?method=removeCoordinatorsFromGroup">
<fr:edit name="bean" id="removeCoordinators" schema="removeCoordinators" 
action="vigilancy/vigilantGroupManagement.do?method=removeCoordinatorsFromGroup"
nested="true">
	<fr:layout>
	<fr:property name="displayLabel" value="false"/>
	<fr:property name="sortBy" value="person.name"/>
	<fr:property name="classes" value="mvert0"/>
	</fr:layout>
	
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeCoordinatorsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('removeCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a></span>
	<html:submit styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<fr:form id="selectedUnit" action="/vigilancy/vigilantGroupManagement.do?method=selectUnit&forwardTo=editCoordinators">
<fr:edit id="selectUnit" name="bean" schema="selectUnitInVigilantGroup" nested="true">
<fr:destination name="postback" path="/vigilancy/vigilantGroupManagement.do?method=selectUnit"/>
	<fr:layout>
			<fr:property name="classes" value="mtop15"/>
		</fr:layout>
</fr:edit>
	<html:submit styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<div id="addCoordinatorsToGroup">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></strong></p>
<fr:form id="addCoordinatorsForm" action="/vigilancy/vigilantGroupManagement.do?method=addCoordinatorsToGroup">
<fr:edit name="bean" id="addCoordinators" schema="addCoordinators" 
			action="/vigilancy/vigilantGroupManagement.do?method=addCoordinatorsToGoup">
			<fr:layout>
			<fr:property name="displayLabel" value="false"/>
			<fr:property name="classes" value="mvert0"/>
			<fr:property name="sortBy" value="person.name"/>
			</fr:layout>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addCoordinatorsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a></span>
	<html:submit styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>