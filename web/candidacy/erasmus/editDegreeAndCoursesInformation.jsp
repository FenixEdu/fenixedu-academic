<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="processId" name="process" property="idInternal" />

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<fr:form action='<%= f("/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&amp;processId=%s&amp", processId.toString()) %>' id="thisForm">

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<h2 class="mtop1"><bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	<fr:edit id="degree.course.information.bean.edit" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
	        <fr:destination name="chooseDegreePostback" path='<%= f("/caseHandlingErasmusIndividualCandidacyProcess.do?userAction=editCandidacy&amp;method=chooseDegree&amp;processId=%s", processId) %>' />
		</fr:layout>
	</fr:edit>
		
	<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
	
	<table class="tstyle2 thlight thcenter">
	<tr>
		<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
		<th><!-- just in case --></th>
	</tr>
	<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
		<bean:define id="curricularCourseId" name="course" property="externalId" />
	<tr>
		<td>
			<fr:view 	name="course"
						property="nameI18N">
<%-- 				<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>--%>
			</fr:view>
		</td>
		<td>
			<a onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
		</td>
	</tr>
	</logic:iterate>
	</table>

		<h2 class="mtop1"><bean:message key="label.erasmus.master.programme" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit id="individualCandidacyProcessBean.degree"
			name="individualCandidacyProcessBean"
			schema="ErasmusIndividualCandidacyProcessBean.selectDegree.manage">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
				
	<html:submit onclick="this.form.method.value='executeEditDegreeAndCoursesInformation'; return true;"><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>

