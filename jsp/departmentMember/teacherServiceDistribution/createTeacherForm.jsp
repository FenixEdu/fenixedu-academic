<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherService.createTeacher"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
			<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
		</html:link>
		>
		<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
		</html:link>
		>
		<bean:message key="label.teacherService.createTeacher"/>
	</em>
</p>

<ul class="mtop05 mbottom15">
	<li>
		<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
</ul>


<html:form action="/valuationTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createValuationTeacher"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>

<p class="mbottom05"><b><bean:message key="label.teacherService.createTeacher"/></b></p>

	<table class="tstyle5 thlight thright mtop05">
		<tr>
			<td>			
				<bean:message key="label.teacherServiceDistribution.name"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="25" maxlength="240"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacherServiceDistribution.category"/>:
			</td>
			<td>
				<html:select property="category">
					<html:options collection="categoriesList" property="idInternal" labelProperty="shortName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>			
				<bean:message key="label.teacherService.teacher.hours"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.hours" property="hours" size="3" maxlength="4"/>
			</td>
		</tr>
	</table>

	<p>
		<html:submit>
	 		<bean:message key="label.teacherServiceDistribution.create"/> 
		</html:submit>
	</p>
</html:form> 

<p>
	<span class="error">
		<html:errors />
		<logic:present name="creationFailure">
			<bean:message key="label.teacherServiceDistribution.valuationTeacherCreationFailure"/>
		</logic:present>
	</span>
</p>

