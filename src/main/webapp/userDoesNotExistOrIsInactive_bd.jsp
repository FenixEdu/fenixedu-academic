<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<title><bean:message key="title.not.authorized"/></title>
<html:errors/>
<center> <b><bean:message key="message.user.does.not.exist"/></b> </center>