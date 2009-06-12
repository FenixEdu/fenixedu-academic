<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="Português">
	<a href="http://www.ist.utl.pt/en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="#">IST</a> &gt;
	<a href="#"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href="#"><bean:message key="title.application" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=candidacyIntro" %>'><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application"/>
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:write name="application.name" bundle="APPLICATION_RESOURCES"/></h1>

<em><bean:message key="message.application.submission.period.ended" bundle="CANDIDATE_RESOURCES"/>.</em>

<p class="mtop15">
	<bean:message key="message.any.question.application.submission" bundle="CANDIDATE_RESOURCES"/>.
</p>

<div class="mtop15" id="contacts"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>	
