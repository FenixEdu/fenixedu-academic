<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<ul>
<logic:present name="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" scope="request">

	<html:form action="/index">
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

<logic:present name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY%>" scope="request">
	<bean:define id="infoExecutionPeriod" name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY%>" scope="request"/>
	<bean:define id="ePName" type="java.lang.String" name="infoExecutionPeriod" property="name"/>	
	<bean:define id="eYName" type="java.lang.String" name="infoExecutionPeriod" property="infoExecutionYear.year"/>

	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" +pageContext.findAttribute("eYName") %>" > <bean:message key="link.public.home"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" +pageContext.findAttribute("eYName") %>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" +pageContext.findAttribute("eYName") %>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" +pageContext.findAttribute("eYName") %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;ePName=" + pageContext.findAttribute("ePName") + "&amp;eYName=" +pageContext.findAttribute("eYName") %>" ><bean:message key="link.exams.consult"/> </html:link></li>
</logic:present>
</ul>