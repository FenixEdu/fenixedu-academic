<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<jsp:include page="/departmentMember/quc/editProcess_body.jsp"/>