<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="actionPath" value="/sendEmail.do" toScope="request"/>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<jsp:include page="/commons/communication/sendEmail.jsp"/>
