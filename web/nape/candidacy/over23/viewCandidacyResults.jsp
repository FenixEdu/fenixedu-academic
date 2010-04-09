<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />

<html:link action='<%= "/caseHandlingOver23CandidacyProcess.do?method=listProcesses&amp;processId=" + processId.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
</html:link>
<br/>
<br/>

<h3><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></h3>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<ul>
	<li>
		<html:link action='<%= "/caseHandlingOver23CandidacyProcess.do?method=prepareIntroduceCandidacyResults&amp;processId=" + processId.toString() %>'>
			« <bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</li>
</ul>
<br/>

<fr:view name="over23IndividualCandidacyResultBeans" schema="Over23IndividualCandidacyResultBean.manage.all.students">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 mtop025"/>
	</fr:layout>
</fr:view>
