<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<ul>
<logic:present name="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" scope="request">

	<html:form action="/index">
		<!-------------------------------------------------------------------->
			<html:hidden  property="objectCode" value="<%= ""+request.getAttribute("objectCode")%>" />
			<html:hidden  property="ePName" value="<%= ""+request.getAttribute("ePName")%>" />
			<html:hidden  property="eYName" value="<%=""+request.getAttribute("eYName")%>" />
		<!-------------------------------------------------------------------->
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
			    <td nowrap="nowrap" width="125">
			    	<bean:message key="property.executionPeriod"/>:
			    </td>
			</tr>
			<tr>
			    <td nowrap="nowrap">
			    	<html:select property="index" size="1">
						<html:options property="value"
							labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>"/>
					</html:select>
			    </td>
			</tr>
			<tr>
				<td>
					<br />
					<html:hidden property="page" value="1"/>
					<html:hidden property="method" value="choose"/>
				    <html:submit styleClass="inputbutton">
				    	<bean:message key="label.choose"/>
	          		</html:submit>
				</td>
			</tr>
		</table>
	</html:form> 
	<br />
</logic:present>

	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.public.home"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" ><bean:message key="link.exams.consult"/> </html:link></li>

</ul>