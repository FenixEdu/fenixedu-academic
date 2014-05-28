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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 1</strong>: <bean:message key="label.candidacy.selectPerson" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 2</strong>: <bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 3</strong>: <bean:message key="label.erasmus.candidacy.educational.background" bundle="ACADEMIC_OFFICE_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="APPLICATION_RESOURCES" /> 4</strong>: <bean:message key="label.erasmus.candidacy.degrees.and.subjects" bundle="ACADEMIC_OFFICE_RESOURCES" /> </span>
</p>

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

<bean:define id="parentProcessId" name="parentProcess" property="externalId" />

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<fr:form action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?userAction=createCandidacy&amp;parentProcessId=%s", parentProcessId.toString()) %>' id="thisForm">

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
	
	<span class="infoop2">
		<bean:message key="message.mobilityApplications.changeDegreeSelectionText" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</span>
	<div class="mtop2">
		<a id="showSelectCourses" href="#"><bean:message key="message.mobilityApplications.selectCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
		<a id="showSelectDegree" class="indent1" href="#"><bean:message key="message.mobilityApplications.selectDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></a>
	</div>
	
	<div id="selectCourses" class="mtop3">
		<p><em><bean:message key="message.erasmus.select.courses.of.associated.degrees" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
		
		<fr:edit id="degree.course.information.bean" name="degreeCourseInformationBean" schema="ErasmusCandidacyProcess.degreeCourseInformationBean">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		        <fr:destination name="chooseDegreePostback" path="/caseHandlingMobilityIndividualApplicationProcess.do?method=chooseDegree" />
			</fr:layout>
		</fr:edit>
			
		<html:submit onclick="$('#methodId').attr('value', 'addCourse'); $('#skipValidationId').attr('value', 'true'); $('#thisForm').submit(); return true;"><bean:message key="label.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		
		<table class="tstyle2 thlight thcenter">
		<tr>
			<th><bean:message key="label.erasmus.course" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><bean:message key="label.erasmus.degree" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
			<th><!-- just in case --></th>
		</tr>
		<logic:iterate id="course" name="individualCandidacyProcessBean" property="sortedSelectedCurricularCourses" indexId="index">
			<bean:define id="curricularCourseId" name="course" property="externalId" />
		<tr>
			<td>
				<fr:view 	name="course"
							property="nameI18N">
				</fr:view>
			</td>
			<td>
				<fr:view	name="course"
							property="degree.nameI18N" /> - 
				<fr:view	name="course"
							property="degree.sigla" />
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
		        <fr:destination name="chooseDegreePostback" path="/caseHandlingMobilityIndividualApplicationProcess.do?method=chooseDegreeForMobility" />
			</fr:layout>
		</fr:edit>
	</div>

	<html:submit onclick="this.form.method.value='createNewProcess'; return true;"><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
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
