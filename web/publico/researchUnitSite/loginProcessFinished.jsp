<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1 class="mtop0"><bean:message key="label.loginRequest" bundle="SITE_RESOURCES"/></h1>

<p class="mtop2"><strong><bean:message key="label.login.creation.success" bundle="SITE_RESOURCES"/>.</strong></p>

<p><a href="<%= request.getContextPath() + "/privado" %>"><bean:message key="label.loginLink" bundle="SITE_RESOURCES"/></a></p>