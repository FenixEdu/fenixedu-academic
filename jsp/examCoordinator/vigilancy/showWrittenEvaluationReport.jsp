<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.writtenEvaluationReport"/></h2>
<bean:define id="writtenEvaluationId" name="writtenEvaluation" property="idInternal"/>

<ul>
	<li><html:link page="/vigilancy/convokeManagement.do?method=prepareEditConvoke"><bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link>
	<li>
		<html:link page="<%= "/vigilancy/convokeManagement.do?writtenEvaluationId=" + writtenEvaluationId + "&amp;method=prepareAddMoreVigilants" %>">
			<bean:message key="label.convokeMore" bundle="VIGILANCY_RESOURCES"/>
		</html:link>
	</li>

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

<script type="text/javascript">
	function submitForm(form, options) {
		
		form.method.value    		  = options.method;
		form.oid.value 	   			  = options.oid;
		form.bool.value			      = options.bool;
		form.participationType.value  = options.participationType;
		form.submit();
	}
</script>

<logic:notEmpty name="writtenEvaluation" property="teachersVigilancies">
<strong><bean:message key="label.vigilancy.vigilantsThatTeachCourse" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>


<form id="convokeForm" action="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do"%>">
	<input type="hidden" name="writtenEvaluationId" value="<%= writtenEvaluationId %>"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="oid" value=""/>
	<input type="hidden" name="bool" value=""/>
	<input type="hidden" name="participationType" value=""/>

<table class="tstyle1">
	<tr>
		<th rowspan="2"><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="2"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="3"><bean:message key="label.vigilancy.attendedStatus" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<tr>
		<th><bean:message key="message.yes" bundle="APPLICATION_RESOURCES"/></th>
		<th><bean:message key="message.no" bundle="APPLICATION_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.notAttended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.dismissed" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>

	<logic:iterate id="vigilancy"  name="writtenEvaluation" property="teachersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">

	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilant.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.name"/></td>
			
			<logic:equal name="vigilancy" property="active" value="true">
			<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/>
			</td>
			<td class="acenter">
			<input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "false", "none")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "false", "none")%>"/>	
			</td>
			</logic:equal>
 
			<logic:equal name="vigilancy" property="active" value="false">
			<td class="acenter">
			<input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "true", "none")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "true", "none")%>"/>	
			</td>
			<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" checked="checked"/></td>
			</logic:equal>

			<logic:equal name="vigilancy" property="attended" value="true">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="attended" value="false">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "ATTENDED")%>"/></td>
			</logic:equal>
			
			<logic:equal name="vigilancy" property="notAttended" value="true">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>			
			<logic:equal name="vigilancy" property="notAttended" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "NOT_ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "NOT_ATTENDED")%>"/></td>
			</logic:equal>
						
			<logic:equal name="vigilancy" property="dismissed" value="true">
						<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "DISMISSED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "DISMISSED")%>"/></td>			
			</logic:equal>


			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>
	</tr>

	</logic:iterate>
	
</table>
</form>
</logic:notEmpty>

<logic:notEmpty name="writtenEvaluation" property="othersVigilancies">
<strong><bean:message key="label.vigilancy.vigilants" bundle="VIGILANCY_RESOURCES"/>:</strong><br/>

<form id="convokeForm2" action="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do"%>">
	<input type="hidden" name="writtenEvaluationId" value="<%= writtenEvaluationId %>"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="oid" value=""/>
	<input type="hidden" name="bool" value=""/>
	<input type="hidden" name="participationType" value=""/>

<table class="tstyle1">
	<tr>
		<th rowspan="2"><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="2"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></th>
		<th colspan="3"><bean:message key="label.vigilancy.attendedStatus" bundle="VIGILANCY_RESOURCES"/></th>
		<th rowspan="2"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>
	<tr>
		<th><bean:message key="message.yes" bundle="APPLICATION_RESOURCES"/></th>
		<th><bean:message key="message.no" bundle="APPLICATION_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.notAttended" bundle="VIGILANCY_RESOURCES"/></th>
		<th><bean:message key="label.vigilancy.dismissed" bundle="VIGILANCY_RESOURCES"/></th>
	</tr>

	<logic:iterate id="vigilancy"  name="writtenEvaluation" property="othersVigilancies" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">

	<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
	<tr class="<%= !vigilancy.isActive() ? "color888" : ""%>">
		<td><fr:view name="vigilancy" property="vigilant.teacherCategoryCode"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.username"/></td>
			<td><fr:view name="vigilancy" property="vigilant.person.name"/></td>
			
			<logic:equal name="vigilancy" property="active" value="true">
			<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/>
			</td>
			<td class="acenter">
			<input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "false", "none")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "false", "none")%>"/>	
			</td>
			</logic:equal>
 
			<logic:equal name="vigilancy" property="active" value="false">
			<td class="acenter">
			<input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "true", "none")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","convokeActiveEditInReport", vigilancy.getIdInternal(), "true", "none")%>"/>	
			</td>
			<td class="acenter"><input name="<%= "radioActive-" + vigilancy.getIdInternal() %>"  type="radio" checked="checked"/></td>
			</logic:equal>

			<td class="acenter"><fr:view name="vigilancy" property="confirmed"/></td>

			<logic:equal name="vigilancy" property="attended" value="true">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="attended" value="false">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "ATTENDED")%>"/></td>
			</logic:equal>
			
			<logic:equal name="vigilancy" property="notAttended" value="true">
					<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>			
			<logic:equal name="vigilancy" property="notAttended" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "NOT_ATTENDED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "NOT_ATTENDED")%>"/></td>
			</logic:equal>
						
			<logic:equal name="vigilancy" property="dismissed" value="true">
						<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" checked="checked"/></td>
			</logic:equal>
			<logic:equal name="vigilancy" property="dismissed" value="false">
				<td class="acenter"><input name="<%= "radioAttend-" + vigilancy.getIdInternal() %>" type="radio" onclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "DISMISSED")%>" ondblclick="<%= String.format("submitForm(this.form, {method: '%s', oid: '%s', bool: '%s', participationType: '%s'});","changeConvokeStatusInReport", vigilancy.getIdInternal(), "", "DISMISSED")%>"/></td>			
			</logic:equal>

			<td class="acenter"><fr:view name="vigilancy" property="points"/></td>			
	</tr>

	</logic:iterate>
	
</table>
</form>
</logic:notEmpty>