<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.editVigilantGroup"/></h2>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

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

<fr:form id="removeCoordinatorsForm" action="/vigilancy/vigilantGroupManagement.do">
<html:hidden property="method" value="removeCoordinatorsFromGroup"/>

<fr:edit name="bean" id="removeCoordinators" schema="removeCoordinators" 
action="vigilancy/vigilantGroupManagement.do?method=removeCoordinatorsFromGroup"
nested="true">
	<fr:layout>
		<fr:property name="displayLabel" value="false"/>
		<fr:property name="classes" value="mvert0"/>
	</fr:layout>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('removeCoordinatorsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
<%-- 
	<span class="switchInline"><a href="javascript:checkall('removeCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('removeCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a></span>
--%>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.remove" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>


<fr:form id="selectedUnit" action="/vigilancy/vigilantGroupManagement.do">
<html:hidden property="method" value="selectUnit"/>
<html:hidden property="forwardTo" value="editCoordinators"/>

<fr:edit id="selectUnit" name="bean" schema="selectUnitInVigilantGroup" nested="true">
<fr:destination name="postback" path="/vigilancy/vigilantGroupManagement.do"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thmiddle thlight thright mtop15"/>
	</fr:layout>
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<div id="addCoordinatorsToGroup">
<p class="mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></strong></p>
<fr:form id="addCoordinatorsForm" action="/vigilancy/vigilantGroupManagement.do">
<html:hidden property="method" value="addCoordinatorsToGroup"/>
<fr:edit name="bean" id="addCoordinators" schema="addCoordinators" 
			action="/vigilancy/vigilantGroupManagement.do?method=addCoordinatorsToGoup">
			<fr:layout>
			<fr:property name="displayLabel" value="false"/>
			<fr:property name="classes" value="mvert0"/>
			</fr:layout>
</fr:edit>

<p class="mtop05">
	<span class="switchInline"><a href="javascript:document.getElementById('addCoordinatorsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:checkall('addCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a>, </span>
	<span class="switchInline"><a href="javascript:uncheckall('addCoordinatorsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.examCoordinators"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>
</div>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>