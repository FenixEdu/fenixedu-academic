<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<html:xhtml/>

<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="title.phd.referee.letters" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="processId" >
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<fr:view name="process" property="candidacyProcess.candidacyReferees">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee" >
		<fr:slot name="name" />
		<fr:slot name="institution" />
		<fr:slot name="letterAvailable" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
		
		<fr:link name="view" label="link.view,APPLICATION_RESOURCES" condition="letterAvailable"
			link="<%= "/phdIndividualProgramProcess.do?method=viewLetter&amp;refereeId=${externalId}&amp;processId=" + processId %>" />
	</fr:layout>
</fr:view>

</logic:present>