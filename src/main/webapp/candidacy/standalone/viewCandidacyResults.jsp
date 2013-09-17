<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />

<html:link action='<%= "/caseHandlingStandaloneCandidacyProcess.do?method=listProcesses&amp;processId=" + processId.toString() %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
</html:link>
<br/>
<br/>

<h3><bean:message key="label.candidacy.standalone" bundle="APPLICATION_RESOURCES"/></h3>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<ul>
	<li>
		<html:link action='<%= "/caseHandlingStandaloneCandidacyProcess.do?method=prepareIntroduceCandidacyResults&amp;processId=" + processId.toString() %>'>
			« <bean:message key="label.candidacy.introduce.results" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</li>
</ul>
<br/>

<fr:view name="standaloneIndividualCandidacyResultBeans" schema="StandaloneIndividualCandidacyResultBean.manage.all.students">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 mtop025"/>
	</fr:layout>
</fr:view>
