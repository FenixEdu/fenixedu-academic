<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherService.createTeacher"/></h2>

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
		<bean:message key="label.teacherService.createTeacher"/>
	</em>
</p>

<br/>
<html:form action="/tsdTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createTSDTeacher"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<span class="mbottom05"><b><bean:message key="label.teacherService.createTeacher"/>:</b></span>

	<table class="tstyle5 thlight thright mtop0 mbottom05 thmiddle">
		<tr>
			<th>			
				<bean:message key="label.teacherServiceDistribution.name"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="25" maxlength="240"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.category"/>:
			</th>
			<td>
				<html:select property="category">
					<html:options collection="categoriesList" property="idInternal" labelProperty="shortName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th>			
				<bean:message key="label.teacherService.teacher.hours"/>:
			</th>
			<td align="left">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.hours" property="hours" size="3" maxlength="4" />
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
	<logic:present name="creationFailure">
		<bean:message key="label.teacherServiceDistribution.tsdTeacherCreationFailure"/>
	</logic:present>
</span>
<br/>
<br/>

<html:link page='<%= "/tsdTeachersGroup.do?method=prepareForTSDTeachersGroupServices&amp;tsdID=" + ((TSDProcess) request.getAttribute("tsdProcess")).getCurrentTSDProcessPhase().getRootTSD().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
