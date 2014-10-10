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

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayUnavailableInformation"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<fr:form action="/vigilancy/unavailablePeriodManagement.do?method=manageUnavailablePeriodsOfVigilants">
<fr:edit id="selectVigilantGroup" name="bean" schema="vigilantGroup.selectToEdit">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
	</fr:layout>
</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<logic:present name="bean" property="selectedVigilantGroup">
<bean:define id="vigilantGroup" name="bean" property="selectedVigilantGroup" type="net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup"/>

<ul class="mtop1">
	<li>
		<html:link page="<%= "/vigilancy/unavailablePeriodManagement.do?method=prepareAddPeriodToVigilant&gid=" + vigilantGroup.getExternalId() %>">
			<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.addAnUnavailablePeriodOfVigilant"/>
		</html:link>
	</li>
</ul>

<logic:empty name="unavailablePeriods"> 
	<p>
		<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noUnavailablePeriodsToManage"/>.</em>
	</p>
</logic:empty>

<logic:notEmpty name="unavailablePeriods">
	<fr:view 
		name="unavailablePeriods"
		schema="unavailableShowForCoordinator"
    >
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="columnClasses" value="nowrap smalltxt color888,nowrap smalltxt color888,,,nowrap" />
		<fr:property name="key(edit)" value="label.edit"/>
		<fr:property name="bundle(edit)" value="VIGILANCY_RESOURCES"/>
		<fr:property name="link(edit)" value="<%= "/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriodOfVigilant&gid=" + vigilantGroup.getExternalId() %>"/>
		<fr:property name="param(edit)" value="externalId/oid" />
		<fr:property name="key(delete)" value="label.delete"/>
		<fr:property name="bundle(delete)" value="VIGILANCY_RESOURCES"/>
		<fr:property name="link(delete)" value="<%= "/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriodOfVigilant&gid=" + vigilantGroup.getExternalId() %>"/>
		<fr:property name="param(delete)" value="externalId/oid" />

	</fr:layout>
	</fr:view>
   
</logic:notEmpty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>