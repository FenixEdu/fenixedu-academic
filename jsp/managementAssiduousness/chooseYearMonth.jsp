<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.exportWorkSheets" /></h2>
<p class="mtop2"><span class="error0"><html:errors /></span></p>


<%
out.write("----");
    out.write(request.getPathInfo());
    out.write("----");
	out.write(request.getParameter("action"));
	out.write("----");
	out.write(request.getRequestURI());
	out.write("----");
	out.write(request.getRemoteAddr());
	out.write("----");
	out.write(request.getServletPath());
	out.write("----");
	out.write(request.getServerName());
	out.write("----");
	out.write(request.getRealPath("/"));
	out.write("----");
	out.write(request.getQueryString());
	out.write("----");
	while (request.getParameterNames().hasMoreElements()){
	    out.write("----");
	    out.write(request.getParameterNames().nextElement().toString());
	}

%>



<fr:form action="/exportAssiduousness.do?method=exportToPDFWorkDaySheet">
	<fr:edit name="yearMonth" schema="choose.date">
		<fr:layout>
			<fr:property name="classes" value="thlight thright" />
		</fr:layout>
	</fr:edit>
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
		<bean:message key="button.export" />
	</html:submit></p>
</fr:form>
