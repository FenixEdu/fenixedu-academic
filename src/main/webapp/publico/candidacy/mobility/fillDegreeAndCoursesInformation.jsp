<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>


<%!
	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<p class="steps">
	<span><bean:message key="mobility.label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> >
	<span><bean:message key="mobility.label.step.two.educational.background" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.three.mobility.program" bundle="CANDIDATE_RESOURCES" /></span> >
	<span class="actual"><bean:message key="mobility.label.step.four.degree.and.subjects" bundle="CANDIDATE_RESOURCES" /></span> >
	<span><bean:message key="mobility.label.step.five.honour.declaration" bundle="CANDIDATE_RESOURCES" /></span>	 
</p>

<%--
<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>

<fr:form id="thisForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>'>

	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<h2 class="mtop1">
		<%--
		<bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" />
		--%>
		Degree and Subjects
	</h2>
	
	<bean:define id="universityName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity.nameI18n.content" type="String"/>
	<bean:define id="programName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description" type="String"/> 
	
	<p class="mbottom05"><bean:message key="message.mobility.available.degrees.for.mobility.agreement" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= programName %>" arg1="<%= universityName %>"/></p>

<style>
table.asd table tr td {
border: none;
padding: 0 !important;
}
</style>

	<fr:view	name="individualCandidacyProcessBean"
				schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight thright mtop05 asd"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>

	<span class="infoop2">
		<bean:message key="message.mobilityApplications.changeDegreeSelectionText" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</span>
	<div class="mtop2">
		<a id="showSelectCourses" href="#"><bean:message key="message.mobilityApplications.selectCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
		<a id="showSelectDegree" class="indent1" href="#"><bean:message key="message.mobilityApplications.selectDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
	</div>
	
	<div id="selectCourses" class="mtop3">
		<p>
			<%--
			<strong><bean:message key="label.eramsus.candidacy.choosen.subjectsAndDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
			--%>
			<strong>Choose your degree and subjects</strong>
		</p>
	
	<%--	
		<p>
			<fr:view name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
		</p>
	--%>	
	
		<p class="mbottom05"><bean:message key="message.mobility.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
		
		<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" schema="PublicErasmusCandidacyProcess.degreeCourseInformationBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:destination name="chooseDegreePostback" path="/candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=chooseDegree" />
			</fr:layout>
		</fr:edit>
			
		<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;">+ Add subject</html:submit>
		
		<logic:empty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
			<p class="mvert15"><em><bean:message key="erasmus.message.empty.courses" bundle="CANDIDATE_RESOURCES" />.</em></p>
		</logic:empty>
		
		<logic:notEmpty name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses">
		
			<p class="mtop2 mbottom05"><b>Selected subjects</b></p>
		
			<table class="tstyle1 thlight thcenter mtop05">
				<tr>
					<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
					<th><bean:message key="label.erasmus.ects" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
					<th><!-- just in case --></th>
				</tr>
				<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
					<bean:define id="curricularCourseId" name="course" property="externalId" />
				<tr>
					<td>
						<fr:view name="course" property="nameI18N"/>
					</td>
					<td>
						<fr:view name="course" property="degree.nameI18N" /> - 
						<fr:view name="course" property="degree.sigla" />
					</td>			
					<td>
						<fr:view name="course" property="ectsCredits" />
					</td>				
					<td>
						<a href="#" onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	
		<p class="mtop15">
			<%--
			<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
			--%>
			<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</p>
	
		<span class="highlight1">
			> <fr:view name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
		</span>
	</div>	

	<div id="selectDegree" class="mtop3">	
		<fr:edit id="mobility.individual.application" name="mobilityIndividualApplicationProcessBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
				<fr:slot name="degree" key="label.mobility.degree" layout="menu-select-postback">
					<fr:property name="format" value="${presentationName}" />
					<fr:property name="destination" value="chooseDegreePostback"/>
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.DegreesForExecutionYearProviderForMobilityIndividualApplicationProcess" />		
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:destination name="chooseDegreePostback" path="/candidacies/caseHandlingMobilityApplicationIndividualProcess.do?method=chooseDegreeForMobility" />
			</fr:layout>
		</fr:edit>
	</div>		
	<p class="mtop2">
		<html:submit onclick="this.form.method.value='acceptHonourDeclaration'; return true;"><bean:message key="label.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
		<%--
		<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		--%>
	</p>

</fr:form>

<script type="text/javascript">
	var viewSelector;
	$(document).ready(function () {
		viewSelector = <%= request.getAttribute("selectDegreeView") %>; 
		if (viewSelector) {
			$('#selectCourses').toggle();
			$('#showSelectDegree').addClass("disabledLinkAsTag");
			$('#showSelectCourses').click(clickOnSelectCourses);
		} else  {
			$('#selectDegree').toggle();
			$('#showSelectCourses').addClass("disabledLinkAsTag");
			$('#showSelectDegree').click(clickOnSelectDegree);
		}
	});
	
	function togglePanels () {
		$('#selectCourses').toggle();
		$('#selectDegree').toggle();
	};
	
	function clickOnSelectCourses () {
		$('#showSelectCourses').addClass("disabledLinkAsTag");
		$('#showSelectDegree').removeClass("disabledLinkAsTag");
		
		$('#showSelectCourses').unbind('click');
		$('#showSelectDegree').click(clickOnSelectDegree);
		
		togglePanels();
	}
	
	function clickOnSelectDegree () {
		$('#showSelectDegree').addClass("disabledLinkAsTag");
		$('#showSelectCourses').removeClass("disabledLinkAsTag");
		
		$('#showSelectDegree').unbind('click');
		$('#showSelectCourses').click(clickOnSelectCourses);
		
		togglePanels();
	}
</script>