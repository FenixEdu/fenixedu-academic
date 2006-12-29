<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editVigilantGroup"/></h2>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>


<ul>
	<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<logic:notEmpty name="unableToRemove">
<div class="warning0">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.unableToRemoveVigilantsDueToConvokes"/>
	<fr:view name="unableToRemove">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="presentVigilant"/>
			<fr:property name="classes" value="mbottom05"/>
		</fr:layout>
	</fr:view>
</div>
</logic:notEmpty>


<div id="vigilantsInGroup">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilantGroup"/> <fr:view name="bean" property="selectedVigilantGroup.name"/>:</strong></p>

<fr:form id="removeVigilantsForm" action="/vigilancy/vigilantGroupManagement.do?method=removeVigilantsFromGroup">
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>

<fr:edit name="bean" id="removeVigilants" schema="removeVigilants" 
action="vigilancy/vigilantGroupManagement.do?method=removeVigilantsFromGroup"
nested="true">
	<fr:layout>
	<fr:property name="displayLabel" value="false"/>
	<fr:property name="classes" value="mvert0"/>
	</fr:layout>
</fr:edit>

<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<fr:form id="selectedUnit" action="/vigilancy/vigilantGroupManagement.do?method=selectUnit&forwardTo=vigilants">
	<fr:edit id="selectUnit" name="bean" schema="selectUnitInVigilantGroup" nested="true">
		<fr:destination name="postback" path="/vigilancy/vigilantGroupManagement.do?method=selectUnit"/>
		<fr:layout>
			<fr:property name="classes" value="mtop15"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<div id="addVigilantsToGroup">
<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.employees"/></strong>
<fr:form id="addVigilantsForm" action="/vigilancy/vigilantGroupManagement.do?method=addVigilantsToGroup">
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a> </span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>

<fr:edit name="bean" id="addVigilants" schema="addVigilants" 
			action="/vigilancy/vigilantGroupManagement.do?method=addVigilantsToGroup">
			<fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
</fr:edit>

<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a> </span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<p class="mtop15"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.externalPersonToGroup"/></strong></p>

<fr:form id="addSingleVigilant" action="/vigilancy/vigilantGroupManagement.do?method=addVigilantToGroupByUsername">
<fr:edit name="bean" id="addExternalPersonToGroup" schema="addExternalPerson"
	nested="true">
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"/>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addSingleVigilant').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>