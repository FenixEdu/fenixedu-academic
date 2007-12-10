<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionPeriod" %>
<%@ page import="net.sourceforge.fenixedu.domain.CurricularYear" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherServiceDistribution.createCourse"/></h2>

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
		<bean:message key="label.teacherServiceDistribution.createCourse"/>
	</em>
</p>

<br/>

<html:form action="/tsdCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createTSDCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCurricularCourse" property="tsdCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourse" property="tsdCourse"/>
<span class="mbottom05"><b><bean:message key="label.teacherServiceDistribution.createNewCourse"/>:</b></span>

<table class="tstyle5 thlight thright mtop0 mbottom05 thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>:
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="24" maxlength="240"/> 
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.executionPEriod"/>:
		</th>
		<td>
			<html:select property="executionPeriod">
				<html:options collection="executionPeriodsList" property="idInternal" labelProperty="name"/>
			</html:select>  
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.curricularPlans"/>:
		</td>
		<td>
		<% int brPos = 6, counter = 0; %>
			<table cellpadding="0" cellspacing="0" style="margin: 0em; border-collapse: collapse" border="0">
			<tr>
			<logic:iterate name="curricularPlansList" id="plan">
				<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.planArray" property="curricularPlansArray">
					<bean:write name="plan" property="idInternal"/>
				</html:multibox>
				<bean:write name="plan" property="name"/>
				</td>
				<%= ++counter % brPos == 0 ? "</tr><tr>" : ""  %>
			</logic:iterate>
			</tr></table>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.shiftTypes"/>:
		</td>
		<td>
		<% brPos = 6; counter = 0; %>
			<table cellpadding="0" cellspacing="0" style="margin: 0em; border-collapse: collapse" border="0">
			<tr>
			<logic:iterate name="shiftsList" id="shiftType">
				<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftTypeArray" property="shiftTypesArray">
					<bean:write name="shiftType" property="name"/>
				</html:multibox>
				<bean:write name="shiftType" property="fullNameTipoAula"/>
				</td>
				<%= ++counter == brPos ? "</tr><tr>" : ""  %>
			</logic:iterate>
			</tr></table>
		</td>
	</tr>
</table>			
<html:submit>
	<bean:message key="label.teacherServiceDistribution.create"/>
</html:submit>
	
</html:form>

<br/>
<span class="error">
	<html:errors />
</span>
<br/>
<br/>

<html:link page='<%= "/tsdTeachersGroup.do?method=prepareForTSDTeachersGroupServices&amp;tsdID=" + ((TSDProcess) request.getAttribute("tsdProcess")).getCurrentTSDProcessPhase().getRootTSD().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>

