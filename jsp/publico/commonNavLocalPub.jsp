<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<ul>
	<logic:present name="ePName" scope="request">
		<logic:present name="eYName" scope="request">
			<logic:notEqual parameter="ePName" value="null">
				<logic:notEqual parameter="eYName" value="null">
					<h4><bean:write name="ePName"/> - <bean:write name="eYName"/></h4><br />
				</logic:notEqual>
			</logic:notEqual>
		</logic:present>
	</logic:present>
	
	<li><html:link page="<%= "/index.do?method=prepare&amp;page=0&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.public.home"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.classes.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.executionCourse.consult"/> </html:link></li>
	<li><html:link page="<%= "/prepareConsultRooms.do?ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <bean:message key="link.rooms.consult"/> </html:link></li>
	<li><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" ><bean:message key="link.exams.consult"/> </html:link></li>
</ul>