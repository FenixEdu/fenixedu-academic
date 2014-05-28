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


<bean:define id="executionCourseId" name="executionCourseID"/>
<bean:define id="evaluationId" name="evaluation" property="externalId"/>

<h2><bean:message key="label.viewVigilancies" bundle="VIGILANCY_RESOURCES"/></h2>

<ul>
<li>
<logic:equal name="evaluation" property="class.simpleName" value="WrittenTest">
<html:link page="<%="/evaluation/writtenTestsIndex.faces?executionCourseID=" + executionCourseId%>"><bean:message key="link.goBack" bundle="APPLICATION_RESOURCES"/></html:link>
</logic:equal>
<logic:equal name="evaluation" property="class.simpleName" value="Exam">
<html:link page="<%="/evaluation/examsIndex.faces?executionCourseID=" + executionCourseId%>"><bean:message key="link.goBack" bundle="APPLICATION_RESOURCES"/></html:link>
</logic:equal>
</li>
</ul>


<h3><fr:view name="evaluation" property="name"/> (<fr:view name="evaluation" property="dayDateYearMonthDay"/> - <fr:view name="evaluation" property="beginningDateHourMinuteSecond"/>)</h3>

<logic:notEmpty name="ownVigilancies">

<script type="text/javascript">
	function submitForm(form, options) {
		
		form.method.value    = options.method;
		form.oid.value 	   	 = options.oid;
		form.bool.value 	 = options.bool;
		form.submit();
	}
</script>
	
<form id="convokeForm" action="<%= request.getContextPath() + "/teacher/evaluation/vigilancy/vigilantsForEvaluation.do"%>" method="post">
	<input type="hidden" name="evaluationOID" value="<%= evaluationId %>"/>
	<input type="hidden" name="executionCourseID" value="<%= executionCourseId %>"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="oid" value=""/>
	<input type="hidden" name="bool" value=""/>


<p class="mtop2 mbottom05"><strong><bean:message key="label.teachersVigilants" bundle="VIGILANCY_RESOURCES"/></strong></p>

<table class="tstyle1 thlight mtop05">
	<tr>
		<th rowspan="2"><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="2"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<tr>
		<th><bean:message key="message.yes" bundle="APPLICATION_RESOURCES"/></th>
		<th><bean:message key="message.no" bundle="APPLICATION_RESOURCES"/></th>
	</tr>

<logic:iterate id="vigilancy" name="ownVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
<td class="acenter">
	<logic:present name="vigilancy" property="vigilantWrapper.teacher">
		<fr:view name="vigilancy" property="vigilantWrapper.teacherCategoryCode"/>
	</logic:present>
</td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.name" /></td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
<td class="acenter"><fr:view name="vigilancy" property="points" /></td>

			<logic:equal name="vigilancy" property="active" value="true">
				<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
				<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getExternalId(), "false")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getExternalId(), "false")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="active" value="false">
					<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getExternalId(), "true")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getExternalId(), "true")%>"/></td>
					<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>

<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>
</tr>
</logic:iterate>

</table>
</form>

</logic:notEmpty>


<logic:notEmpty name="vigilancies">

<p class="mtop1 mbottom05"><strong><bean:message key="label.otherVigilants" bundle="VIGILANCY_RESOURCES"/></strong></p>

<table class="tstyle1 thlight mtop05">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<logic:iterate id="vigilancy" name="vigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
		<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
			<td class="acenter">
				<logic:present name="vigilancy" property="vigilantWrapper.teacher">
					<fr:view name="vigilancy" property="vigilantWrapper.teacherCategoryCode"/>
				</logic:present>
			</td>
			<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.name" /></td>
			<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="points" /></td>
			<td class="acenter"><fr:view name="vigilancy" property="active"/></td>
			<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>
		</tr>
	</logic:iterate>
</table>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<fr:form action="<%= "/evaluation/vigilancy/vigilantsForEvaluation.do?method=requestUnconvokes&evaluationOID=" + evaluationId + "&executionCourseID=" + executionCourseId %>">
<table>
	<tr>
		<td>
			<fr:edit id="variantBean" name="unconvokeRequest" schema="variant.integer">
				<fr:layout>
					<fr:property name="classes" value="thlight"/>
				</fr:layout>
			</fr:edit>
		</td>
		<td>
			<html:submit>
				<bean:message key="label.deactivate" bundle="VIGILANCY_RESOURCES"/>
			</html:submit>
		</td>
	</tr>
</table>
</fr:form>


</logic:notEmpty>

<logic:empty name="vigilancies">
	<p>
	<bean:message key="label.noVigilanciesFoundForEvaluation" bundle="VIGILANCY_RESOURCES"/>
	</p>
</logic:empty>
