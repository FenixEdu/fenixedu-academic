<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- Personal Data -->

<p class="header"><strong><bean:message key="label.viewDepartmentTeachersExpectations.teacherPersonalData" /></strong></p>
<ul style="list-style: none;">
	<li>
		<strong><ft:label property="teacher.person.nome" />:</strong>&nbsp;&nbsp;&nbsp;<ft:view property="teacher.person.nome" />
	</li>
	<li>
		<strong><ft:label property="teacher.teacherNumber" />:</strong>&nbsp;&nbsp;&nbsp;<ft:view property="teacher.teacherNumber" />
	</li>
</ul>

<!-- Education Expectations -->
<p class="header"><strong><bean:message key="label.viewDepartmentTeachersExpectations.education" /></strong></p>

<!-- Graduations -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.graduations" />:</p>
<ul>
	<li>
		<i><ft:label property="graduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="graduations" />
	</li>
	<li>
		<i><ft:label property="graduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="graduationsDescription" />
	</li>
</ul>

<!-- Cientific Pos-Graduations -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.cientificPosGraduations" />:</p>
<ul>
	<li>
		<i><ft:label property="cientificPosGraduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="cientificPosGraduations" />
	</li>
	<li>
		<i><ft:label property="cientificPosGraduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="cientificPosGraduationsDescription" />
	</li>
</ul>

<!-- Professional Pos-Graduations -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.professionalPosGraduations" />:</p>
<ul>
	<li>
		<i><ft:label property="professionalPosGraduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="professionalPosGraduations" />
	</li>
	<li>
		<i><ft:label property="professionalPosGraduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="professionalPosGraduationsDescription" />
	</li>
</ul>


<!-- Seminaries -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.seminaries" />:</p>
<ul>
	<li>
		<i><ft:label property="seminaries" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="seminaries" />
	</li>
	<li>
		<i><ft:label property="seminariesDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="seminariesDescription" />
	</li>
</ul>

<!-- Education Main Focus -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="educationMainFocus" /></div>



<!-- Investigation Expectations -->
<p class="header"><strong><bean:message	key="label.viewDepartmentTeachersExpectations.investigation" /></strong></p>
<!-- Investigation -->
<ul>
	<li>
		<i><ft:label property="researchAndDevProjects" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="researchAndDevProjects" />
	</li>
	<li>
		<i><ft:label property="jornalArticlePublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="jornalArticlePublications" />
	</li>
	<li>
		<i><ft:label property="bookPublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="bookPublications" />
	</li>
	<li>
		<i><ft:label property="conferencePublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="conferencePublications" />
	</li>
	<li>
		<i><ft:label property="technicalReportPublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="technicalReportPublications" />
	</li>
	<li>
		<i><ft:label property="patentPublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="patentPublications" />
	</li>
	<li>
		<i><ft:label property="otherPublications" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="otherPublications" />
	</li>
	<li>
		<i><ft:label property="otherPublicationsDescription" /></i>:<br/><ft:view property="otherPublicationsDescription" />
	</li>
</ul>

<!-- Research and Dev Main Focus -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="researchAndDevMainFocus" /></div>

<!-- Orientation -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.orientation" />:</p>
<ul>
	<li>
		<i><ft:label property="phdOrientations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="phdOrientations" />
	</li>
	<li>
		<i><ft:label property="masterDegreeOrientations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="masterDegreeOrientations" />
	</li>
	<li>
		<i><ft:label property="finalDegreeWorkOrientations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="finalDegreeWorkOrientations" />
	</li>
</ul>

<!-- Orientation Main Focus -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>: </p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="orientationsMainFocus" /></div>


<!-- University Service Expectations -->
<p class="header"><strong><bean:message	key="label.viewDepartmentTeachersExpectations.universityService" /></strong></p>

<ul class="limbottom">
	<li>
		<i><ft:label property="departmentOrgans" />:</i><br/><ft:view property="departmentOrgans" />
	</li>
	<li>
		<i><ft:label property="istOrgans" />:</i><br/><ft:view property="istOrgans" />
	</li>
	<li>
		<i><ft:label property="utlOrgans" />:</i><br/><ft:view property="utlOrgans" />
	</li>
</ul>

<!-- University Service Main Focus -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="universityServiceMainFocus" /></div>

<!-- Professional Activity Expectations -->
<p class="header"><strong><bean:message	key="label.viewDepartmentTeachersExpectations.professionalActivity" /></strong></p>

<ul class="limbottom">
	<li>
		<i><ft:label property="cientificComunityService" />:</i><br/><ft:view property="cientificComunityService" />
	</li>
	<li>
		<i><ft:label property="societyService" />:</i><br/><ft:view property="societyService" />
	</li>
	<li>
		<i><ft:label property="consulting" />:</i><br/><ft:view property="consulting" />
	</li>
	<li>
		<i><ft:label property="companySocialOrgans" />:</i><br/><ft:view property="companySocialOrgans" />
	</li>
	<li>
		<i><ft:label property="companyPositions" />:</i><br/><ft:view property="companyPositions" />
	</li>
</ul>

<!-- Professional Activity Main Focus -->
<p style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="professionalActivityMainFocus" /></div>



