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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.writtenEvaluationReport"/></h2>
<bean:define id="writtenEvaluationId" name="writtenEvaluation" property="externalId"/>

<ul>
	<li><html:link page="/vigilancy/vigilantManagement.do?method=prepareMap"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link>
</ul>
<p class="mtop2 mbottom05"><strong><bean:message key="label.vigilancy.course" bundle="VIGILANCY_RESOURCES"/>:</strong> <span class="highlight1"><fr:view name="writtenEvaluation" property="fullName"/></span></p>
<p class="mvert05"><strong><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>:</strong> <fr:view name="writtenEvaluation" property="beginningDateTime"/></p>
<p class="mvert05"><strong><bean:message key="label.vigilancy.associatedRooms" bundle="VIGILANCY_RESOURCES"/></strong>: 
	<logic:notEmpty name="writtenEvaluation" property="associatedRooms">  
	<fr:view name="writtenEvaluation" property="associatedRooms">
	<fr:layout name="flowLayout">
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="presentRooms"/>
				<fr:property name="htmlSeparator" value=","/>
	</fr:layout>
	</fr:view>
	</logic:notEmpty>
	<logic:empty name="writtenEvaluation" property="associatedRooms">  
		<em><bean:message key="label.vigilancy.associatedRoomsUnavailable" bundle="VIGILANCY_RESOURCES"/></em>
	</logic:empty>
</p>

<logic:notEmpty name="writtenEvaluation" property="teachersVigilancies">
<strong><bean:message key="label.vigilancy.vigilantsThatTeachCourse" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<logic:iterate id="vigilancy" name="writtenEvaluation" property="teachersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilantWrapper.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.name"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="active"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="attended"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:notEmpty name="writtenEvaluation" property="othersVigilancies">
<strong><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>

	</tr>
<logic:iterate id="vigilancy" name="writtenEvaluation" property="othersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
		
	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilantWrapper.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.name"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="active"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="attended"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>