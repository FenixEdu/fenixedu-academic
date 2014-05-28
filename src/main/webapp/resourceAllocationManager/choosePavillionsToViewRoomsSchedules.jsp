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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>Listagem de Horários por Salas</h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<fr:form action="/viewAllRoomsSchedulesDA.do">
	<input type="hidden" name="method" value="list"/>

	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllRoomsSchedulesDA$ChooseBuildingBean" bundle="SOP_RESOURCES">
			<fr:slot name="academicInterval" layout="menu-select" key="link.choose.execution.period">
				<fr:property name="format" value="\${pathName}" />
				<fr:property name="from" value="availableIntervals" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>

	<html:submit onclick="this.form.method.value='choose'"><bean:message key="label.change"></bean:message></html:submit>
	
	<br />
	
	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllRoomsSchedulesDA$ChooseBuildingBean" bundle="SOP_RESOURCES">
			<fr:slot name="selectedBuildings" layout="option-select" key="label.choose.rooms">
				<fr:property name="from" value="availableBuildings" />
				<fr:property name="classes" value="nobullet noindent"/>
				<fr:property name="selectAllShown" value="true" />
				<fr:property name="eachSchema" value="person.name" />
				<fr:property name="eachLayout" value="values-dash" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
		<bean:message key="label.list"/>
	</html:submit>
</fr:form>
	
	<h4>Listagens especificas (<bean:write name="bean" property="academicInterval.pathName"/>)</h4>
	<ul>
		<li>
			<html:link action="/viewAllRoomsSchedulesDA.do?method=downloadRoomLessonOccupationInfo" paramId="academicIntervalString" paramName="bean" paramProperty="academicInterval.representationInStringFormat">
				<bean:message key="link.download.room.lesson.occupation.mao"/>
			</html:link>
		</li>
		<li>
			<html:link action="/viewAllRoomsSchedulesDA.do?method=downloadScheduleList" paramId="academicIntervalString" paramName="bean" paramProperty="academicInterval.representationInStringFormat">
				<bean:message key="link.download.schedule.list"/>
			</html:link>
		</li>
		<li>
			<html:link action="/viewAllRoomsSchedulesDA.do?method=downloadShiftAttendence" paramId="academicIntervalString" paramName="bean" paramProperty="academicInterval.representationInStringFormat">
				<bean:message key="link.download.shiftAttendence.list"/>
			</html:link>
		</li>
	</ul>