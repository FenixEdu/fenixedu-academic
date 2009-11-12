<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess"%>

<logic:notEmpty name="process" property="thesisProcess">
<logic:equal name="process" property="activeState.active" value="true">

	<br/>
	<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PhdThesisProcess.view" name="process" property="thesisProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	<bean:define id="thesisProcess" name="process" property="thesisProcess" />
	<ul class="operations">
		<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= PhdThesisProcess.SubmitThesis.class %>">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareSubmitThesis" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.submit.thesis"/>
			</html:link>
		</li>
		</phd:activityAvailable>
	</ul>
</logic:equal>
<br/>
</logic:notEmpty>
