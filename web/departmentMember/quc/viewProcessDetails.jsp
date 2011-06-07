<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" type="text/css" />

<em><bean:message key="label.departmentMember"/></em>
<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<jsp:include page="viewProcessDetails_body.jsp"/>