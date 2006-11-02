<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- Personal Data -->

<h2><bean:message key="label.viewDepartmentTeachersExpectations.teacherPersonalData" /></h2>
<ul style="list-style: none;">
	<li>
		<strong><ft:label property="teacher.person.nome" />:</strong>&nbsp;&nbsp;&nbsp;<ft:view property="teacher.person.nome"  />
	</li>
	<li>
		<strong><ft:label property="teacher.teacherNumber" />:</strong>&nbsp;&nbsp;&nbsp;<ft:view property="teacher.teacherNumber" />
	</li>
</ul>

<!-- Education Expectations -->
<h2><bean:message key="label.viewDepartmentTeachersExpectations.education" /></h2>

<!-- Graduations -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.graduations" />:</p>
<ul>
	<li>
		<i><ft:label property="graduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="graduations"  />
	</li>
	<li>
		<i><ft:label property="graduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="graduationsDescription" layout="html"/>
	</li>
</ul>

<!-- Cientific Pos-Graduations -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.cientificPosGraduations" />:</p>
<ul>
	<li>
		<i><ft:label property="cientificPosGraduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="cientificPosGraduations" />
	</li>
	<li>
		<i><ft:label property="cientificPosGraduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="cientificPosGraduationsDescription" layout="html" />
	</li>
</ul>

<!-- Professional Pos-Graduations -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.professionalPosGraduations" />:</p>
<ul>
	<li>
		<i><ft:label property="professionalPosGraduations" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="professionalPosGraduations" />
	</li>
	<li>
		<i><ft:label property="professionalPosGraduationsDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="professionalPosGraduationsDescription" layout="html" />
	</li>
</ul>


<!-- Seminaries -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.seminaries" />:</p>
<ul>
	<li>
		<i><ft:label property="seminaries" /></i>:&nbsp;&nbsp;&nbsp;<ft:view property="seminaries" />
	</li>
	<li>
		<i><ft:label property="seminariesDescription" /></i>:&nbsp;&nbsp;&nbsp;<br/><ft:view property="seminariesDescription" layout="html" />
	</li>
</ul>

<!-- Education Main Focus -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="educationMainFocus" layout="html"/></div>



<!-- Investigation Expectations -->
<h2><bean:message	key="label.viewDepartmentTeachersExpectations.investigation" /></h2>
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
		<i><ft:label property="otherPublicationsDescription" /></i>:<br/><ft:view property="otherPublicationsDescription" layout="html" />
	</li>
</ul>

<!-- Research and Dev Main Focus -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="researchAndDevMainFocus" layout="html"/></div>

<!-- Orientation -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.orientation" />:</p>
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
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>: </p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="orientationsMainFocus" layout="html"/></div>


<!-- University Service Expectations -->
<h2><bean:message	key="label.viewDepartmentTeachersExpectations.universityService" /></h2>

<ul class="limbottom">
	<li>
		<i><ft:label property="departmentOrgans" />:</i><br/><ft:view property="departmentOrgans" layout="html" />
	</li>
	<li>
		<i><ft:label property="istOrgans" />:</i><br/><ft:view property="istOrgans" layout="html" />
	</li>
	<li>
		<i><ft:label property="utlOrgans" />:</i><br/><ft:view property="utlOrgans" layout="html"/>
	</li>
</ul>

<!-- University Service Main Focus -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="universityServiceMainFocus" layout="html"/></div>

<!-- Professional Activity Expectations -->
<h2><bean:message	key="label.viewDepartmentTeachersExpectations.professionalActivity" /></h2>

<ul class="limbottom">
	<li>
		<i><ft:label property="cientificComunityService" />:</i><br/><ft:view property="cientificComunityService" layout="html" />
	</li>
	<li>
		<i><ft:label property="societyService" />:</i><br/><ft:view property="societyService" layout="html" />
	</li>
	<li>
		<i><ft:label property="consulting" />:</i><br/><ft:view property="consulting"  layout="html"/>
	</li>
	<li>
		<i><ft:label property="companySocialOrgans" />:</i><br/><ft:view property="companySocialOrgans" layout="html"/>
	</li>
	<li>
		<i><ft:label property="companyPositions" />:</i><br/><ft:view property="companyPositions" layout="html"/>
	</li>
</ul>

<!-- Professional Activity Main Focus -->
<p style="font: bold 13px Verdana, Arial, Helvetica, sans-serif;"><bean:message key="label.viewDepartmentTeachersExpectations.mainFocus"/>:</p>
<div style="padding: 6px 0; padding-left: 40px;"><ft:view property="professionalActivityMainFocus" layout="html" /></div>



