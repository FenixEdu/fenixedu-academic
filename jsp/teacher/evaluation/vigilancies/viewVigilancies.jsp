<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="executionCourseId" name="executionCourseID"/>
<bean:define id="evaluationId" name="evaluation" property="idInternal"/>

<em><bean:message key="label.vigilancies" bundle="APPLICATION_RESOURCES"/></em>
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

<strong><fr:view name="evaluation" property="name"/> (<fr:view name="evaluation" property="dayDateYearMonthDay"/> - <fr:view name="evaluation" property="beginningDateHourMinuteSecond"/>) </strong>

<logic:notEmpty name="vigilancies">

<script type="text/javascript">
	function submitForm(form, options) {
		
		form.method.value    = options.method;
		form.oid.value 	   	 = options.oid;
		form.bool.value 	 = options.bool;
		form.submit();
	}
</script>
	
<form id="convokeForm" action="<%= request.getContextPath() + "/teacher/evaluation/vigilancy/vigilantsForEvaluation.do"%>">
	<input type="hidden" name="evaluationOID" value="<%= evaluationId %>"/>
	<input type="hidden" name="executionCourseID" value="<%= executionCourseId %>"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="oid" value=""/>
	<input type="hidden" name="bool" value=""/>

<table class="tstyle1">
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

<logic:iterate id="vigilancy" name="vigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
<td class="acenter">
	<logic:present name="vigilancy" property="vigilant.teacher">
		<fr:view name="vigilancy" property="vigilant.teacher.category.code"/>
	</logic:present>
</td>
<td class="acenter"><fr:view name="vigilancy" property="vigilant.person.name" /></td>
<td class="acenter"><fr:view name="vigilancy" property="vigilant.person.username"/></td>
<td class="acenter"><fr:view name="vigilancy" property="points" /></td>

			<logic:equal name="vigilancy" property="active" value="true">
				<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
				<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getIdInternal(), "false")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getIdInternal(), "false")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="active" value="false">
					<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getIdInternal(), "true")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s'});","changeActiveConvoke", vigilancy.getIdInternal(), "true")%>"/></td>
					<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>

<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>
</tr>
</logic:iterate>

</table>
</form>

</logic:notEmpty>
<logic:empty name="vigilancies">
	<p>
	<bean:message key="label.noVigilanciesFoundForEvaluation" bundle="VIGILANCY_RESOURCES"/>
	</p>
</logic:empty>
