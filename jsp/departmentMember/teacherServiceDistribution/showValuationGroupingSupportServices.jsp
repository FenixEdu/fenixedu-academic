<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<script language="JavaScript">
function changeSignal(i, t)
{
img = document.getElementById(i);
table = document.getElementById(t);

	if (table.style.display == "none")
	  {
	  table.style.display = "";
	  img.src = "<%= request.getContextPath() %>/images/toggle_minus10.gif";
	  }
	else
	  {
	  table.style.display = "none";
	  img.src = "<%= request.getContextPath() %>/images/toggle_plus10.gif";
	  }
}
</script>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>

	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.groupingAreaSupportService"/>
</h3>

<html:form action="/valuationGroupingSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<br/>
<br/>

<table class="search">
	<tr>
		<td>
			&nbsp;<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
		</td>
		<td>
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGroupings'; this.form.page.value=0; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
		<logic:present name="parentGroupingName">
			<td>
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='deleteValuationGrouping'; this.form.submit();">
					<bean:message key="label.teacherServiceDistribution.delete"/>
				</html:button>
			</td>
		</logic:present>
		<logic:notPresent name="parentGroupingName">
			<td>
			</td>
		</logic:notPresent>
	</tr>
</table>

<logic:present name="parentGroupingName">
<br/>
<br/>

	<bean:message key="label.teacherServiceDistribution.parentGrouping"/>: <b><bean:write name="parentGroupingName"/></b>

	<table class="search">
		<tr>
			<td>
				<b><bean:message key="label.teacherServiceDistribution.valuationTeacher"/></b>
			</td>
			<logic:empty name="valuationTeacherList">
			<td align="center">
				<bean:message key="label.teacherServiceDistribution.groupingTeachersAlreadyIncluded"/>
			</logic:empty>
			<logic:notEmpty name="valuationTeacherList">
			<td>
				<html:select property="valuationTeacher">
					<html:options collection="valuationTeacherList" property="idInternal" labelProperty="name"/>
				</html:select>
			</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="valuationTeacherList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateValuationTeacher'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associate"/>
					</html:button>
				</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="valuationTeacherList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateAllValuationTeachers'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associateAll"/>
					</html:button>
				</logic:notEmpty>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/></b>
			</td>
			<logic:empty name="valuationCompetenceCourseList">
			<td align="center">
				<bean:message key="label.teacherServiceDistribution.groupingCoursesAlreadyIncluded"/>
			</logic:empty>
			<logic:notEmpty name="valuationCompetenceCourseList">
			<td>
				<html:select property="valuationCompetenceCourse">
					<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
				</html:select>		
			</logic:notEmpty>							
			</td>
			<td>
				<logic:notEmpty name="valuationCompetenceCourseList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateValuationCompetenceCourse'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associate"/>
					</html:button>
				</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="valuationCompetenceCourseList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateAllValuationCompetenceCourses'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associateAllFemale"/>
					</html:button>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
</logic:present>


<br>
<br>

<img src="<%= request.getContextPath() %>/images/toggle_plus10.gif" id="signalImg" onclick="changeSignal('signalImg','optionsTable'); return false;" /> 
&nbsp;<b><bean:message key="label.teacherServiceDistribution.operations"/></b>
<br>


<table class="search" id="optionsTable" style="display: none;">
	<tr>
		<td>		
			&nbsp;<b><bean:message key="label.teacherServiceDistribution.changeName"/></b>
		</td>

</html:form>
<html:form action="/valuationGroupingSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadValuationGroupings"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>

		<td>
			<fr:edit id="name-validated" name="selectedValuationGrouping" slot="name" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator" scope="request">
				<fr:layout>
					<fr:property name="size" value="44"/>
					<fr:property name="maxLength" value="240" />
				</fr:layout>
			</fr:edit>
		</td>
		<td align="center">
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();">
				<bean:message key="link.change"/>
			</html:button>
		</td>
		<td>
			<span class="error">
				<fr:message for="name-validated"/>
				<fr:hasMessages for="name-validated">
					<script language="JavaScript" type="text/javascript">
						changeSignal('signalImg','optionsTable');
					</script>
				</fr:hasMessages>
			</span>
		</td>
		
</html:form>
<html:form action="/valuationGroupingSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCompetenceCourseDissociation" property="valuationCompetenceCourseDissociation" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationTeacherDissociation" property="valuationTeacherDissociation" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>

	</tr>
	<tr>
		<td>		
			&nbsp;<b><bean:message key="label.teacherServiceDistribution.createNewGrouping"/></b>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" maxlength="240" size="44"/>
		</td>
		<td align="center">
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='createValuationGrouping'; this.form.page.value=1; this.form.submit();">
				&nbsp;<bean:message key="label.teacherServiceDistribution.create"/>&nbsp;
			</html:button>
		</td>
	</tr>
	<logic:present name="parentGroupingName">
		<logic:notEmpty name="mergeGroupingOptionEntryList">
		<tr>
			<td>
				&nbsp;<b><bean:message key="label.teacherServiceDistribution.mergeValuationGroupings"/></b>
			</td>
			<td>
				<html:select property="otherGrouping">
					<html:options collection="mergeGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
				</html:select>			
			</td>
			<td align="center">
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='mergeValuationGroupings'; this.form.page.value=0; this.form.submit();">
					<bean:message key="label.teacherServiceDistribution.merge"/>
				</html:button>
			</td>
		</tr>
		</logic:notEmpty>
	</logic:present>
</table>

<br/>
<span class="error">
	<html:errors property="name"/>
	<logic:messagesPresent property="name">
		<script language="JavaScript" type="text/javascript">
			changeSignal('signalImg','optionsTable');
		</script>
	</logic:messagesPresent>
</span>
<br/>
<br/>

<table style="width: 60em;">
<tr valign="top">
<td width="50%">

<table class='vtsbc' width="100%">
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.associatedValuationTeacher"/></b>
		</th>
		<logic:present name="parentGroupingName">
		<th>
		</th>
		</logic:present>
	</tr>
<logic:empty name="valuationTeacherListBelongToGrouping">
	<tr>
		<td colspan="2">
			<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
		</td>
	</tr>	
</logic:empty>		
<logic:iterate name="valuationTeacherListBelongToGrouping" id="valuationTeacher">
	<bean:define id="valuationTeacherId" name="valuationTeacher" property="idInternal"/>
	<tr>
		<td class="courses">
			<bean:write name="valuationTeacher" property="name"/>
		</td>
		<logic:present name="parentGroupingName">
			<td>
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onmouseover="this.style.color='blue';" onmouseout="this.style.color='black';" styleClass="btn" 
				onclick='<%= "this.form.valuationTeacherDissociation.value=" + valuationTeacherId + ";this.form.method.value='dissociateValuationTeacher'; this.form.submit()" %>'>
					<bean:message key="label.teacherServiceDistribution.dissociate"/>
				</html:button>
			</td>
		</logic:present>
	</tr>
</logic:iterate>
</table>
<br/>
<br/>
</td>

<td width="50%">
<table class='vtsbc' width="100%">
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.associatedCompetenceCourse"/></b>
		</th>
		<logic:present name="parentGroupingName">
		<th>
		</th>
		</logic:present>
	</tr>
<logic:empty name="valuationCompetenceCourseListBelongToGrouping">
	<tr>
		<td colspan="2">
			<bean:message key="label.teacherServiceDistribution.notExistsCompetenceCourses"/>
		</td>
	</tr>	
</logic:empty>
<logic:iterate name="valuationCompetenceCourseListBelongToGrouping" id="valuationCompetenceCourse">
	<bean:define id="valuationCompetenceCourseId" name="valuationCompetenceCourse" property="idInternal"/>
	<tr>
		<td class="courses">
			<bean:write name="valuationCompetenceCourse" property="name"/>
		</td>
		<logic:present name="parentGroupingName">
			<td>
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" styleClass="btn" onclick='<%= "this.form.valuationCompetenceCourseDissociation.value=" + valuationCompetenceCourseId + ";this.form.method.value='dissociateValuationCompetenceCourse'; this.form.submit()" %>'>
					<bean:message key="label.teacherServiceDistribution.dissociate"/>
				</html:button>
			</td>
		</logic:present>
	</tr>
</logic:iterate>
</table>

</td>
</tr>
</table>

</html:form>
<br/>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
