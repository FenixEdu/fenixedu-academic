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

<h2><bean:message key="label.vigilanciesReport" bundle="VIGILANCY_RESOURCES"/></h2>

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

<strong><fr:view name="evaluation" property="name"/> (<fr:view name="evaluation" property="dayDateYearMonthDay"/> - <fr:view name="evaluation" property="beginningDateHourMinuteSecond"/>) </strong>

<script type="text/javascript">
	function submitForm(form, options) {
		
		form.method.value    		  = options.method;
		form.oid.value 	   			  = options.oid;
		form.participationType.value  = options.participationType;
		form.submit();
	}
</script>
	
<form id="convokeForm" method="post" action="<%= request.getContextPath() + "/teacher/evaluation/vigilancy/vigilantsForEvaluation.do"%>">
	<input type="hidden" name="evaluationOID" value="<%= evaluationId %>"/>
	<input type="hidden" name="executionCourseID" value="<%= executionCourseId %>"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="oid" value=""/>
	<input type="hidden" name="participationType" value=""/>

<table class="tstyle1">
	<tr>
		<th rowspan="2"><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="3"><bean:message key="label.vigilancy.attendedStatus" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<tr>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.notAttended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.dismissed" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>


<logic:iterate id="vigilancy" name="ownVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
<td class="acenter">
	<logic:present name="vigilancy" property="vigilantWrapper.teacher">
		<logic:present name="vigilancy" property="vigilantWrapper.teacher.category">
			<fr:view name="vigilancy" property="vigilantWrapper.teacher.category.name"/>
		</logic:present>
	</logic:present>
</td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.name" /></td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
<td class="acenter"><fr:view name="vigilancy" property="points" /></td>
<td class="acenter"><fr:view name="vigilancy" property="active" /></td>
<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>

			<logic:equal name="vigilancy" property="attended" value="true">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="attended" value="false">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "", "ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="notAttended" value="true">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>			
			<logic:equal name="vigilancy" property="notAttended" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(),  "NOT_ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "", "NOT_ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="true">
						<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(),  "DISMISSED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatust", vigilancy.getExternalId(), "", "DISMISSED")%>"/></td>			
			</logic:equal>

</tr>
</logic:iterate>

<logic:iterate id="vigilancy" name="vigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
<td class="acenter">
	<logic:present name="vigilancy" property="vigilantWrapper.teacher">
		<logic:present name="vigilancy" property="vigilantWrapper.teacher.category">
			<fr:view name="vigilancy" property="vigilantWrapper.teacher.category.name"/>
		</logic:present>
	</logic:present>
</td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.name" /></td>
<td class="acenter"><fr:view name="vigilancy" property="vigilantWrapper.person.username"/></td>
<td class="acenter"><fr:view name="vigilancy" property="points" /></td>
<td class="acenter"><fr:view name="vigilancy" property="active" /></td>
<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>

			<logic:equal name="vigilancy" property="attended" value="true">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="attended" value="false">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "", "ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="notAttended" value="true">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>			
			<logic:equal name="vigilancy" property="notAttended" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(),  "NOT_ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(), "", "NOT_ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="true">
						<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getExternalId() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getExternalId(),  "DISMISSED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatust", vigilancy.getExternalId(), "", "DISMISSED")%>"/></td>			
			</logic:equal>

</tr>
</logic:iterate>

</table>




</form>


<%-- 
<fr:view name="vigilancies" schema="presentVigilancyWithParticipation">
 <fr:layout name="tabular">
 	<fr:property name="classes" value="tstyle1"/>
	<fr:property name="columnClasses" value=",,,acenter,acenter,acenter,acenter,acenter"/>
	<fr:property name="groupLinks" value="true"/>
	<fr:property name="key(notParticipate)" value="label.vigilancy.notAttended"/>
	<fr:property name="bundle(notParticipate)" value="VIGILANCY_RESOURCES"/>
	<fr:property name="link(notParticipate)" value="<%="/evaluation/vigilancy/vigilantsForEvaluation.do?method=revokePresence&amp;executionCourseID=" + executionCourseId + "&amp;evaluationOID=" + evaluationId%>"/>
	<fr:property name="param(notParticipate)" value="externalId/oid" />
	<fr:property name="visibleIf(notParticipate)" value="attended" />
	
	<fr:property name="key(participate)" value="label.vigilancy.attended"/>
	<fr:property name="bundle(participate)" value="VIGILANCY_RESOURCES"/>
	<fr:property name="link(participate)" value="<%="/evaluation/vigilancy/vigilantsForEvaluation.do?method=reportPresence&amp;executionCourseID=" + executionCourseId + "&amp;evaluationOID=" + evaluationId%>"/>
	<fr:property name="param(participate)" value="externalId/oid" />
	<fr:property name="visibleIfNot(participate)" value="attended" />
	
 </fr:layout> 

</fr:view>
 --%>