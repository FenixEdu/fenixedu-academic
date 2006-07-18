<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.tutor"/></h2>
<span class="error"><html:errors/></span><br/>

<html:form action="/tutorManagement" focus="tutorNumber">

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=  request.getAttribute("executionDegreeId").toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readTutor" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />

<bean:message key="label.tutorNumber"/>:&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.tutorNumber" property="tutorNumber" size="4"/>

<p />
<html:submit><bean:message key="label.submit"/></html:submit>
<p />
<p />
</html:form>