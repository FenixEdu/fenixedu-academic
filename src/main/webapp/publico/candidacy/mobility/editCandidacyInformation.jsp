<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusApplyForSemesterType"%>

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
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>

<p><a href='<%= f("%s?method=backToViewCandidacy&individualCandidacyProcess=%s", fullPath, individualCandidacyProcessOID) %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="true">
	<p><bean:message key="message.application.employee.edition.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<%--
<p><span><bean:message key="message.all.fields.are.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>


<fr:form action='<%= mappingPath + ".do?method=editCandidacyInformation" %>' >
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
		<h2 class="mtop1"><bean:message key="label.erasmus.home.institution" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.home.institution" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.exchange.coordinator.edit" 
					property="mobilityStudentDataBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.current.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit 	id="erasmusIndividualCandidacyProcessBean.current.study" 
					name="individualCandidacyProcessBean" 
					schema="ErasmusIndividualCandidacyProcess.current.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		
		<h2 class="mtop1"><bean:message key="label.erasmus.period.of.study" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<fr:edit	id="erasmusIndividualCandidacyProcessBean.period.of.study"
					name="individualCandidacyProcessBean"
					schema="ErasmusIndividualCandidacyProcess.period.of.study.edit" >
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
	
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.FIRST_SEMESTER.name() %>">
		<h2 class="mtop15 mbottom05"><bean:message key="label.erasmus.applyForSemester" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Mark the semester's you're going to study at IST</em></p>
		<fr:edit		id="erasmusStudentDataBean.applyForSemester.edit"
					name="individualCandidacyProcessBean"
					property="mobilityStudentDataBean"
					schema="ErasmusStudentDataBean.applyForSemester.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop05"/>
		        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= f("%s.do?method=editCandidacyInformationInvalid", mappingPath) %>'  />
		</fr:edit>
		</logic:equal>

		<h2 class="mtop15 mbottom05"><bean:message key="title.erasmus.language.competence" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
		<p><em>Note: The master programmes are given in english</em></p>
		
		<p><strong>Do you intent to participate in the intensive Portuguese Language Course</strong></p>
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.FIRST_SEMESTER.name() %>">
			<fr:edit	id="erasmusIndividualCandidacyProcessBean.language.intensive.course"
						name="individualCandidacyProcessBean"
						property="mobilityStudentDataBean"
						schema="MobilityStudentData.languageCompetence.intensive.portuguese.course">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05"/>
			        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<logic:equal name="individualCandidacyProcessBean" property="candidacyProcess.forSemester" value="<%= ErasmusApplyForSemesterType.SECOND_SEMESTER.name() %>">
			<fr:edit	id="erasmusIndividualCandidacyProcessBean.language.intensive.course"
						name="individualCandidacyProcessBean"
						property="mobilityStudentDataBean">
				<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="intensivePortugueseCourseFebruary" key="label.erasmus.language.competence.intensivePortugueseCourseFebruary" layout="radio"/>
				</fr:schema>
						
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05 ulnomargin inobullet"/>
			        <fr:property name="columnClasses" value="width225px,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />
			        <fr:property name="requiredMessageShown" value="false" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<h2 class="mtop15 mbottom05"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/></h2>
		<fr:edit id="individualCandidacyProcessBean.observations"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.observations">
 		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>
		
	
	<p class="mtop15">	
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</p>
</fr:form>
