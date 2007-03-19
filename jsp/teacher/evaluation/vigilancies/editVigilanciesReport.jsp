<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<bean:define id="executionCourseId" name="executionCourseID"/>
<bean:define id="evaluationId" name="evaluation" property="idInternal"/>

<em><bean:message key="label.vigilancies" bundle="APPLICATION_RESOURCES"/></em>
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
	
<form id="convokeForm" action="<%= request.getContextPath() + "/teacher/evaluation/vigilancy/vigilantsForEvaluation.do"%>">
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
<td class="acenter"><fr:view name="vigilancy" property="active" /></td>
<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>

			<logic:equal name="vigilancy" property="attended" value="true">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="attended" value="false">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getIdInternal(), "ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getIdInternal(), "", "ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="notAttended" value="true">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>			
			<logic:equal name="vigilancy" property="notAttended" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getIdInternal(),  "NOT_ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getIdInternal(), "", "NOT_ATTENDED")%>"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="true">
						<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatus", vigilancy.getIdInternal(),  "DISMISSED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', participationType: '%s'});","changeConvokeStatust", vigilancy.getIdInternal(), "", "DISMISSED")%>"/></td>			
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
	<fr:property name="param(notParticipate)" value="idInternal/oid" />
	<fr:property name="visibleIf(notParticipate)" value="attended" />
	
	<fr:property name="key(participate)" value="label.vigilancy.attended"/>
	<fr:property name="bundle(participate)" value="VIGILANCY_RESOURCES"/>
	<fr:property name="link(participate)" value="<%="/evaluation/vigilancy/vigilantsForEvaluation.do?method=reportPresence&amp;executionCourseID=" + executionCourseId + "&amp;evaluationOID=" + evaluationId%>"/>
	<fr:property name="param(participate)" value="idInternal/oid" />
	<fr:property name="visibleIfNot(participate)" value="attended" />
	
 </fr:layout> 

</fr:view>
 --%>