<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<jsp:include page="/departmentMember/quc/viewAuditProcesses_body.jsp"/>