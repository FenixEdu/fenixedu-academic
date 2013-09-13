<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<title><bean:message key="title.not.authorized"/></title>
<html:errors/>
<center> <b><bean:message key="message.user.does.not.exist"/></b> </center>