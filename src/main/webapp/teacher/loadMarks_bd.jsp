<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<h:messages layout="table" errorClass="error"/>
 
<h2><bean:message key="title.loadMarks"/></h2>

<%--method=post"--%>
 <html:form action="/writeMarks.do?method=loadFile" enctype="multipart/form-data">
 
 	<%--<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>--%>
	<html:file bundle="HTMLALT_RESOURCES" altKey="file.theFile" property="theFile" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString()%>" /> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.siteView" property="siteView" value="<%= pageContext.findAttribute("siteView").toString()%>" /> 
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
	</html:reset>
 </html:form> 

