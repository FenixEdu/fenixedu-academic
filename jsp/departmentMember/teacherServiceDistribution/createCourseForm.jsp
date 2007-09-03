<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionPeriod" %>
<%@ page import="net.sourceforge.fenixedu.domain.CurricularYear" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherServiceDistribution.createCourse"/></h2>

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
		<bean:message key="label.teacherServiceDistribution.createCourse"/>
	</em>
</p>

<ul class="mtop05 mbottom15">
	<li>
		<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
</ul>


<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createValuationCompetenceCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCourse" property="valuationCourse"/>

<p class="mbottom05"><b><bean:message key="label.teacherServiceDistribution.createNewCourse"/>:</b></p>

<table class="tstyle5 thlight thright thmiddle mtop05">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>:
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="24" maxlength="240"/> 
			&nbsp;
			<html:submit>
				<bean:message key="label.teacherServiceDistribution.create"/>
			</html:submit>
		</td>
	</tr>
</table>
</html:form>


<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadValuationCompetenceCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<em><html:errors property="name"/></em>


<table class="tstyle5 thlight thright thmiddle mtop05">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.editCourse"/>:
		</th>
		<td>
			<html:select property="valuationCourse">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="competenceCoursesList" property="idInternal" labelProperty="name"/>
			</html:select>
			<html:submit>
			<bean:message key="button.submit"/>
			</html:submit>
		</td>
	<tr>
</table>

</html:form>

 
<bean:define id="valuationGroup" name="valuationTeachersGroupForm" property="valuationGrouping"/>

<html:form action="/valuationCoursesGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addValuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCurricularCourse" property="valuationCurricularCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCourse" property="valuationCourse"/>
	
	<bean:define id="valuationCourse" name="valuationTeachersGroupForm" property="valuationCourse"/>
	
	<logic:notEqual name="valuationTeachersGroupForm" property="valuationCourse" value="0">		

		<p class="mtop1">
			<b><bean:message key="label.teacherServiceDistribution.associatedCurricularPlans"/>:</b>
		</p>


		<logic:present name="curricularCoursesList">
			<table class="tstyle1 thlight tdcenter mvert05">
				<tr>
					<th>
						<bean:message key="label.teacherServiceDistribution.acronym"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.curricularPlan"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.executionPEriod"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.curricularYears"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.theoreticalHoursShort"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.praticalHoursShort"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.theoPratHoursShort"/>
					</th>
					<th>
						<bean:message key="label.teacherServiceDistribution.laboratorialHoursShort"/>
					</th>
					<th>
					</th>
				</tr>
				<logic:iterate name="curricularCoursesList" id="curricularCourse">
				<tr>
					<td>
						<bean:write name="curricularCourse" property="acronym" />
					</td>
					<td>
						<bean:write name="curricularCourse" property="degreeCurricularPlan.name" />
					</td>
					<td>					
						<bean:write name="curricularCourse" property="executionPeriod.name" />
					</td>
					<td>
						<logic:iterate name="curricularCourse" property="curricularYears" id="curricularYear">
							<bean:write name="curricularYear" property="year"/>
						</logic:iterate>
					</td>
					<td>
						<bean:write name="curricularCourse" property="theoreticalHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="praticalHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="theoPratHours"/>
					</td>
					<td>
						<bean:write name="curricularCourse" property="laboratorialHours"/>
					</td>
					<td>
						<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal" />
						<html:link page="<%= "/valuationCoursesGroup.do?method=removeValuationCurricularCourse&valuationCurricularCourse=" + curricularCourseId + "&page=0&valuationGrouping=" + valuationGroup +"&valuationCourse=" + valuationCourse %>">
						<%--<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick='<%= "this.form.method.value='removeValuationCurricularCourse';this.form.valuationCurricularCourse.value=" +  curricularCourseId + ";this.form.page.value=0;this.form.submit();"%>'>
							--%>
						<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</logic:present>
		
		<logic:notPresent name="curricularCoursesList">
			<p>
				<em>
					<bean:message key="label.teacherServiceDistribution.notAvailableCurricularPlans"/>
				</em>
			</p>
		</logic:notPresent>
		
		
		<p class="mtop15"><b><bean:message key="label.teacherServiceDistribution.addCurricularPlan"/>:</b></p>

		<div>
			<html:errors />
		</div>
	
		<table class="tstyle4 thlight">
			<tr>
				<th>
					<bean:message key="label.teacherServiceDistribution.acronym"/>
				</th>
				<th>
					<bean:message key="label.teacherServiceDistribution.curricularPlan"/>
				</th>
				<th>
					<bean:message key="label.teacherServiceDistribution.executionPEriod"/>
				</th>
				<th>
					<bean:message key="label.teacherServiceDistribution.curricularYears"/>
				</th>
				<th>
					<bean:message key="label.teacherServiceDistribution.hours"/>
				</th>
			</tr>
			<tr>
				<td>
					<logic:present name="competenceCourseName">
						<bean:define name="competenceCourseName" id="competenceCourseName" />
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.courseName" property="courseName" size="24" maxlength="240" value="<%= (String)competenceCourseName %>"/>
					</logic:present>
					<logic:notPresent name="competenceCourseName">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.courseName" property="courseName" size="24" maxlength="240" />
					</logic:notPresent>
				</td>
				<td>
					<html:select property="degreeCurricularPlan">
						<html:options collection="curricularPlansList" property="idInternal" labelProperty="name"/>
					</html:select> 
				</td>
				<td>
					<html:select property="executionPeriod">
						<html:options collection="executionPeriodsList" property="idInternal" labelProperty="name"/>
					</html:select> 
				</td>
				<td class="acenter">
					<logic:iterate name="curricularYearsList" id="curricularYear">
						<p class="mvert05">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularYearsArray" property="curricularYearsArray">
								<bean:write name="curricularYear" property="idInternal"/>
							</html:multibox>
							<bean:write name="curricularYear" property="year"/>
						</p>
					</logic:iterate>
				<td>
					<p class="aright">
						<bean:message key="label.teacherServiceDistribution.theoretical"/>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" property="theoreticalHours" size="2" maxlength="4" />
					</p>
					<p class="aright">
						<bean:message key="label.teacherServiceDistribution.pratical"/>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" property="praticalHours" size="2" maxlength="4" />
					</p>
					<p class="aright">
						<bean:message key="label.teacherServiceDistribution.theoPrat"/>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" property="theoPratHours" size="2" maxlength="4" />
					</p>
					<p class="aright">
						<bean:message key="label.teacherServiceDistribution.laboratorial"/>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.laboratorialHours" property="laboratorialHours" size="2" maxlength="4" />
					</p>
				</td>
			</tr>
		</table>
		
		<%-- <html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='addValuationCurricularCourse'; this.form.page.value=3; this.form.submit();">
		--%>
		
		<p>
			<html:submit>
				<bean:message key="button.add"/>
			</html:submit>
		</p>
		
	</logic:notEqual>
	

</html:form> 

