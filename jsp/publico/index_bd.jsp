<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  		<tr>
    		<td class="infoop">
    			<html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=classSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" > <h2 style="display: inline;"><bean:message key="link.classes.consult"/></h2></html:link>
    		</td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.timetable.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/chooseContextDA.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>"><h2 style="display: inline;"><bean:message key="link.executionCourse.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.course.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/prepareConsultRooms.do?ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" ><h2 style="display: inline;"><bean:message key="link.rooms.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.room.consult"/>
</p>
<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
  		<tr>
    		<td class="infoop"><html:link page="<%= "/chooseExamsMapContextDA.do?method=prepare&amp;ePName=" + request.getAttribute("ePName") + "&amp;eYName=" +request.getAttribute("eYName") %>" ><h2 style="display: inline;"><bean:message key="link.exams.consult"/></h2></html:link></td>
  		</tr>
	</table>
<br />
<p>
<bean:message key="message.public.index.exam.consult"/>
</p>
<br />
<br />  
<table width="100%">
	<tr>
		<td><img src="<%= request.getContextPath() %>/images/dotist_info.gif" alt="" />
		</td>
		<td class="px9">
		<bean:message key="message.gesdis.info" />
		</td>
	</tr>
</table>