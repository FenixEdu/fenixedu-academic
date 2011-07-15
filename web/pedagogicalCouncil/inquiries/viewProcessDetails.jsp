<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL"/></em>
<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:present name="success">
	<p><span class="success0"><bean:message key="label.inquiry.audit.process.success" bundle="INQUIRIES_RESOURCES"/></span></p>
</logic:present>

<p>
	<html:link page="/qucAudit.do?method=searchExecutionCourse">
		<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<jsp:include page="/pedagogicalCouncil/inquiries/viewProcessDetails_body.jsp"/>