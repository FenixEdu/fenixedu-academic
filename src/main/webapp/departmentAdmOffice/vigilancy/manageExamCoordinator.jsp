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

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageExamCoordinator"/></h2>


<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/vigilancy/examCoordinatorManagement.do?method=selectUnitForCoordinator">
<fr:edit id="selectUnit" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean" layout="tabular"
name="bean" schema="selectUnitInVigilantGroup"/>
</fr:form>
<!--  selectUnitInVigilantGroup -->

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.VigilantGroupBean"/>


<logic:present name="bean" property="selectedUnit">		
<logic:empty name="bean" property="selectedUnit.examCoordinators">
	<p><em class="warning0"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noExamCoordinatoresForUnit"/></em></p>
</logic:empty>

<logic:notEmpty name="bean" property="selectedUnit.examCoordinators">
	<p class="mbottom0"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.coordinatorsForUnit"/></strong>:</p>
<fr:view name="bean" property="selectedUnit.examCoordinators" schema="showExamCoordinators">
<fr:layout name="tabular">
	<fr:property name="classes" value="tstyle1 mvert05" />
	<fr:property name="sortBy" value="executionYear=desc, person.name=asc" />
	<fr:property name="key(apagar)" value="label.delete"/>
	<fr:property name="bundle(apagar)" value="VIGILANCY_RESOURCES"/>
	<fr:property name="link(apagar)" value="<%= "/vigilancy/examCoordinatorManagement.do?method=deleteExamCoordinator&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>"/>
	<fr:property name="param(apagar)" value="externalId/oid"/>
	<fr:property name="visibleIf(apagar)" value="executionYear.current"/>
</fr:layout>
</fr:view>
<p class="mtop05 mbottom2">
	<html:link page="<%="/vigilancy/examCoordinatorManagement.do?method=editExamCoordinators&unitId=" + bean.getSelectedUnit().getExternalId() + "&deparmentId=" + bean.getSelectedDepartment().getExternalId() %>">
	<bean:message key="label.vigilancy.editPreviledges" bundle="VIGILANCY_RESOURCES"/>
	</html:link>
</p>
</logic:notEmpty>



<p class="mbottom05"><strong><bean:message key="label.vigilancy.selectPersonToCoordinate" bundle="VIGILANCY_RESOURCES"/></strong>:</p>

<fr:form action="/vigilancy/examCoordinatorManagement.do?method=addExamCoordinator">
<fr:edit name="bean" id="preserveState" schema="examCoordinator.create" 
action="/vigilancy/examCoordinatorManagement.do?method=addExamCoordinator"/>
<p><html:submit><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit></p>
</fr:form>




</logic:present>
