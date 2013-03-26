<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<br/>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processId" name="process" property="idInternal" />

<fr:form action='<%="/caseHandlingStandaloneIndividualCandidacyProcess.do?processId=" + processId.toString() %>'>
 	<html:hidden property="method" value="executeEditCandidacyInformation" />

	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<fr:edit id="individualCandidacyProcessBean.candidacyDate" 
		 name="individualCandidacyProcessBean"
		 schema="StandaloneIndividualCandidacyProcessBean.candidacyDate">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>" />
	</fr:edit>


	<h3 class="mtop15 mbottom025"><bean:message key="label.curricularCourses" bundle="APPLICATION_RESOURCES"/>:</h3>
	<fr:edit id="individualCandidacyProcessBean.searchCurricularCourseByDegree" 
			 name="individualCandidacyProcessBean" property="searchCurricularCourseByDegree"
			 schema="StandaloneIndividualCandidacyProcessBean.add.curricularCourse">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="searchCurricularCourseByDegreePostBack" path='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=searchCurricularCourseByDegreePostBackWhenEditing&amp;processId=" + processId.toString() %>' />
		<fr:destination name="invalid" path='<%= "/caseHandlingStandaloneIndividualCandidacyProcess.do?method=executeEditCandidacyInformationInvalid&amp;processId=" + processId.toString() %>' />
	</fr:edit>
	<html:submit onclick="this.form.method.value='addCurricularCourseWhenEditing'; return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
	
	<br/>
	<br/>		

	<logic:notEmpty name="individualCandidacyProcessBean" property="curricularCourseBeans">
		<h3 class="mtop15 mbottom025"><bean:message key="label.curricularCourses" bundle="APPLICATION_RESOURCES"/>:</h3>
		<table class="tstyle1 mtop025">
			<logic:iterate id="curricularCourseBean" name="individualCandidacyProcessBean" property="curricularCourseBeans">
				<tr>
					<td class="nowrap"><bean:write name="curricularCourseBean" property="curricularCourseName"/></td>
					<td><bean:write name="curricularCourseBean" property="curricularCourseEcts"/></td>
					<td class="nowrap"><bean:write name="curricularCourseBean" property="degreeName"/></td>
					<td>
						<bean:define id="curricularCourseBeanKey" name="curricularCourseBean" property="key" />
						<html:hidden property="curricularCourseBeanKey" value="<%= curricularCourseBeanKey.toString() %>" />
						<html:submit onclick="this.form.method.value='removeCurricularCourseWhenEditing';return true;"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES" /></html:submit>
					</td>
				</tr>
			</logic:iterate>
			<tr align="right">
				<td colspan="4"><em><strong><bean:message key="label.total.ects" bundle="APPLICATION_RESOURCES" />:</strong> <bean:write name="individualCandidacyProcessBean" property="totalEctsCredits" /></em></td>
			</tr>
		</table>
	</logic:notEmpty>
	<logic:empty name="individualCandidacyProcessBean" property="curricularCourseBeans">
		<strong><em><bean:message key="label.candidacy.standalone.no.curricularCourses" bundle="APPLICATION_RESOURCES"/></em></strong>
	</logic:empty>

	<br/>		
	<br/>
		
	<html:submit><bean:message key="label.save" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcessAllowedActivities'; return true;"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
