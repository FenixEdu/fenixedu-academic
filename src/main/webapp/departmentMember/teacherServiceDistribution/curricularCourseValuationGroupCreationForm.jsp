<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherServiceDistribution.createTSDCourseGroup"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getExternalId() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<html:link page='<%= "/tsdCourse.do?method=prepareForTSDCourse&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getExternalId().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.tsdCourseService"/>
		</html:link>
		>
		<bean:message key="label.teacherServiceDistribution.createTSDCourseGroup"/>
	</em>
</p>


<logic:empty name="availableTSDCurricularCourseToGroupList">
<br/>
<em>
	<bean:message key="label.teacherServiceDistribution.curricularCoursesToGroupCreationNotAvailable"/>
</em>
</logic:empty>

<logic:notEmpty name="availableTSDCurricularCourseToGroupList">
	<html:form action="/tsdCurricularCourseGroupCreation">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createTSDCurricularCourseGroup"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.competenceCourse" property="competenceCourse"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourseType" property="tsdCourseType"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		
		<table class="tstyle5 thlight thright mbottom0 thmiddle">
			<tr>
				<th>
					<bean:message key="label.teacherServiceDistribution.availableCurricularCoursesToAssociate"/>:
				</th>
				<td>
					<logic:iterate name="availableTSDCurricularCourseToGroupList" id="tsdCurricularCourse">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.tsdCurricularCourseArray" property="tsdCurricularCourseArray">
							<bean:write name="tsdCurricularCourse" property="externalId"/>
						</html:multibox>
						<bean:write name="tsdCurricularCourse" property="curricularCourse.degreeCurricularPlan.degree.sigla"/>		
					</logic:iterate>
				</td>
			</tr>
		</table>
		<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();"><bean:message key="label.teacherServiceDistribution.create"/></html:button>
	</html:form>
	
	<br/>
	
	<span class="error">
		<html:errors/>
	</span>
</logic:notEmpty>
<br/>
<br/>
<html:link page='<%= "/tsdCourse.do?method=prepareForTSDCourse&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getExternalId().toString() %>'>
	<bean:message key="link.back"/>
</html:link>