<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h1 class="mtop0"><bean:message key="label.loginRequest" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="SITE_RESOURCES"/></h1>

<p class="mtop2"><strong><bean:message key="label.login.creation.success" bundle="SITE_RESOURCES"/>.</strong></p>

<p><a href="<%= request.getContextPath() + "/privado" %>"><bean:message key="label.loginLink" bundle="SITE_RESOURCES"/></a></p>