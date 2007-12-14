<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html:xhtml/>

<bean:define id="supportLink" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
<bean:define id="institutionUrl" type="java.lang.String"><bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/></bean:define>

<ul>
	<li class="institution"><a href="<%= institutionUrl %>" target="_blank"><bean:message key="label.institution" bundle="GLOBAL_RESOURCES"/></a></li>
	<li class="support"><a href="<%= supportLink %>"><bean:message key="link.suporte" bundle="GLOBAL_RESOURCES"/></a></li>
	<li class="logout"><!-- HAS_CONTEXT --><a href="<%= request.getContextPath() %>/logoff.do"><bean:message key="link.logout" bundle="GLOBAL_RESOURCES"/></a></li>
</ul>
