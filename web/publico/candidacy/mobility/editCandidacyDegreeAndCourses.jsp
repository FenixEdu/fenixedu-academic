<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>


<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>


<fr:form action='<%= mappingPath + ".do?userAction=editCandidacy" %>' id="thisForm">
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<input type="hidden" id="removeId" name ="removeCourseId"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	<input type="hidden" id="methodId" name="method" />
 	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" visible="false" />

	<h2 class="mtop1"><bean:message key="label.erasmus.chooseCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
	<bean:define id="universityName" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedUniversity.nameI18n.content" type="String"/> 
	<p><em><bean:message key="message.erasmus.for.chosen.university.must.select.majority.of.courses" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= universityName %>"/></em></p>

	<fr:view	name="individualCandidacyProcessBean"
				schema="ErasmusCandidacyProcess.view.possible.degrees">
			<fr:layout>
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
	</fr:view>

	<p>
		<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>:
		<fr:view	name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
	</p>
	
	
	<p><em><bean:message key="message.erasmus.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
	
	<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="chooseDegreePostback" path="<%= mappingPath + ".do?method=chooseDegree&userAction=editCandidacy" %>"  />
	</fr:edit>
		
	<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
	
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
			<fr:view 	name="course" property="nameI18N"/>
		</td>
		<td>
			<fr:view	name="course"
						property="degree.nameI18N" /> - 
			<fr:view	name="course"
						property="degree.sigla" />
		</td>
		<td>
			<fr:view	name="course" property="ectsCredits" />
		</td>
		<td>
			<a onclick="<%= f("$('#methodId').attr('value', 'removeCourse'); $('#skipValidationId').attr('value', 'true'); $('#removeId').attr('value', %s); $('#thisForm').submit()", curricularCourseId) %>"><bean:message key="label.erasmus.remove" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
		</td>
	</tr>
	</logic:iterate>
	</table>

	<p>
		<strong><bean:message key="label.eramsus.candidacy.choosed.degree" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>:
		<fr:view	name="individualCandidacyProcessBean" property="selectedCourseNameForView"/>
	</p>

	<p class="mtop15">	
		<html:submit onclick="this.form.method.value='editDegreeAndCourses'; return true;"><bean:message key="label.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</p>
</fr:form>
