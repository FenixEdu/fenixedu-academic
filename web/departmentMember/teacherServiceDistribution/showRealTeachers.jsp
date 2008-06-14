<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherServiceDistribution.addTeacher"/></h2>

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
		<html:link page='<%= "/tsdTeachersGroup.do?method=prepareForTSDTeachersGroupServices&amp;tsdID=" + ((TSDProcess) request.getAttribute("tsdProcess")).getCurrentTSDProcessPhase().getRootTSD().getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
		</html:link>
		>
		<bean:message key="label.teacherServiceDistribution.addTeacher"/>
	</em>
</p>


<html:form action="/tsdTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDepartmentTeachers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacher" property="teacher"/>

	<b><bean:message key="label.teacherServiceDistribution.department"/>:</b>
	<html:select property="department" title="Departamento" onchange="this.form.submit();">
		<html:options collection="departmentList" property="idInternal" labelProperty="realName"/>
	</html:select>

<br/>
<br/>
<br/>

<logic:present name="teachersList">
	<b><bean:message key="label.teacherServiceDistribution.availableTeachers"/>:</b>
	<br/>
	
	<table class='tstyle4 mtop05'>
	<tr>
		<th>
			<b><bean:message key="label.teacherService.teacher.name"/></b>
		</th>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.category"/></b>
		</th>
		<th>
		</th>
	<logic:iterate id="teacher" name="teachersList">
		<tr>
			<td>
				<bean:write name="teacher" property="person.name" />
    		</td>
    		<td>
    			<bean:write name="teacher" property="category.shortName" />
    		</td>
    		<td>
    			<bean:define id="teacherID" name="teacher" property="idInternal"/>
    			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].method.value='addTeacher'; document.forms[0].teacher.value=" + teacherID.toString() + "; document.forms[0].submit()" %>'>
    				<bean:message key="button.add"/>
    			</html:link>
    		</td>
    	</tr>
	</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="teachersList">
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.teachersAlreadyIncluded"/>
		</em>
	</p>
</logic:notPresent>
</html:form>

<br/>
<html:link page='<%= "/tsdTeachersGroup.do?method=prepareForTSDTeachersGroupServices&amp;tsdID=" + ((TSDProcess) request.getAttribute("tsdProcess")).getCurrentTSDProcessPhase().getRootTSD().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
