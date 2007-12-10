<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.Department" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.manageRootGrouping"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	</em>
</p>

<br/>

<html:form action="/tsdTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourse" property="tsdCourse" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdTeacher" property="tsdTeacher" value=""/>

<ul>
	<li>
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='showDepartmentTeachers'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.addTeacher"/>
		</html:link>
	</li>
	<li>	
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='showFormToCreateTeacher'; document.forms[0].submit()">
		  	<bean:message key="label.teacherService.createTeacher"/>
		</html:link>
	</li>
	<li>
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='showDepartmentCourses'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.addCourse"/>
		</html:link>
	</li>
	<li>	
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='showFormToCreateCourse'; document.forms[0].submit()">
		  	<bean:message key="label.teacherServiceDistribution.createCourse"/>
		</html:link>
	</li>
</ul>
<br/>
<br/>

<p class="mtop15 mbottom0">
	<b><bean:message key="label.teacherServiceDistribution.availableTeachersAndCourses"/>:</b>
</p>

<table style="width: 60em;">
<tr valign="top">
<td width="50%">

<table class='tstyle4 mtop05' width="100%">
	<tr>
		<th colspan="4">
			<b><bean:message key="label.teacherServiceDistribution.tsdTeacher"/></b>
		</th>
	</tr>
<logic:iterate name="tsdTeachersList" id="tsdTeacher">
	<bean:define id="tsdTeacher" name="tsdTeacher" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher"/>
	<tr>
		<td>
			<bean:write name="tsdTeacher" property="name"/>
		</td>
		<td class="aright">
			<bean:write name="tsdTeacher" property="category.code"/>
		</td>
		<td class="aright">
			<bean:write name="tsdTeacher" property="department.acronym"/>
		</td>
		<td class="acenter">
			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdTeacher.value=" + ((TSDTeacher) tsdTeacher).getIdInternal() + ";document.forms[0].method.value='removeTeacher'; document.forms[0].submit()" %>'>
				<bean:message key="link.remove"/>
			</html:link>
		</td>			
	</tr>
</logic:iterate>
</table>

<br/>
<br/>

</td>
<td width="50%">
<table class='tstyle4 mtop05' width="100%">
	<tr>
		<th colspan="2">
			<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/></b>
		</th>
	</tr>
<logic:iterate name="tsdCoursesList" id="tsdCourse">
	<bean:define id="tsdCourse" name="tsdCourse" />			
	<tr>
		<td>
			<bean:write name="tsdCourse" property="name"/>
		</td>
		<td class="acenter">
			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdCourse.value=" + ((TSDCourse) tsdCourse).getIdInternal() + ";document.forms[0].method.value='removeCourse'; document.forms[0].submit()" %>'>
				<bean:message key="link.remove"/>
			</html:link>
		</td>						
	</tr>
</logic:iterate>
</table>

</td>
</tr>
</table>
</html:form>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
