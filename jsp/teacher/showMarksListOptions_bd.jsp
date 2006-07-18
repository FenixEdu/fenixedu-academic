<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p></p>
<br />

<h2><bean:message key="title.evaluation"/></h2>
<br />
<h2><bean:message key="title.evaluation.loadMarks"/></h2>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="infoop"><bean:message key="label.marksOnline.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=loadMarksOnline&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + pageContext.findAttribute("evaluationCode") %>" ><bean:message key="link.loadMarksOnline"/></html:link></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    <td class="infoop"><bean:message key="label.fileUpload.information" /></td>
  </tr>
</table>
<%-- <p><html:link page="<%= "/marksList.do?method=loadFile&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + pageContext.findAttribute("evaluationCode") %>" ><bean:message key="link.loadFileMarks"/></html:link></p>
<br /> --%>
 	<html:form action="/writeMarks.do" enctype="multipart/form-data">
 	
	<logic:messagesPresent>
		<span class="error"><html:errors/></span><br/><br/>
	</logic:messagesPresent>
	
 	<%--<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>--%>
	<html:file bundle="HTMLALT_RESOURCES" altKey="file.theFile" property="theFile" style='margin-left:20px'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadFile" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString()%>" /> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.siteView" property="siteView" value="<%= pageContext.findAttribute("siteView").toString()%>" /> 
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" style='margin-left:20px'>
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
	</html:reset>

 </html:form> 

<br />

<h2><bean:message key="title.evaluation.publish"/></h2>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="infoop"><bean:message key="label.publishMarks.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=preparePublishMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + pageContext.findAttribute("evaluationCode") %>" ><bean:message key="link.publishMarks"/></html:link></p>
<br />
