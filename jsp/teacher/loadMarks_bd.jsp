<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
 
<h2><bean:message key="title.loadMarks"/></h2>

<%--method=post"--%>
 <html:form action="/writeMarks.do?method=loadFile" enctype="multipart/form-data">
 
 	<%--<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>--%>
	<html:file property="theFile" />
	<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString()%>" /> 
	<html:hidden property="siteView" value="<%= pageContext.findAttribute("siteView").toString()%>" /> 
	<br />
	<br />
	<html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
	</html:reset>
 </html:form> 

