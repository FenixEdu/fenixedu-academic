<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.EnrolmentPeriod"%>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.edit.enrolment.period" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<fr:edit id="enrolmentPeriod" name="enrolmentPeriod" action="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods">

	<fr:schema bundle="PHD_RESOURCES" type="<%= EnrolmentPeriod.class.getName() %>">
		<fr:slot name="startDate" />
		<fr:slot name="endDate" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	
</fr:edit>
