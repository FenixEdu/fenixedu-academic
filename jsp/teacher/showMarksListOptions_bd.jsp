<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
	<html:file property="theFile" style='margin-left:20px'/>
	<html:hidden property="method" value="loadFile" />
	<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString()%>" /> 
	<html:hidden property="siteView" value="<%= pageContext.findAttribute("siteView").toString()%>" /> 
	<br />
	<br />
	<html:submit styleClass="inputbutton" style='margin-left:20px'>
	<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
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
