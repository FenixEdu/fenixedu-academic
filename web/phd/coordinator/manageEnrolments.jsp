<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<fr:view name="enrolments">
	<fr:schema bundle="PHD_RESOURCES" type="<%= Enrolment.class.getName() %>">
		<fr:property name="name" />
		<fr:property name="ectsCredits" />
		<fr:property name="executionSemester.qualifiedName" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
	</fr:layout>
	
	
	
</fr:view>
