<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<center>
<h4><bean:message key="message.successful.enrolment"/></h4>
<html:link page="/enrollment.do"><bean:message key="button.back.to.begining"/></html:link>
</center>
