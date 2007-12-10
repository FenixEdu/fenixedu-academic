<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
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


<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.groupingAreaSupportService"/></h2>

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
		<bean:message key="link.teacherServiceDistribution.groupingAreaSupportService"/>
	</em>
</p>


<html:form action="/tsdSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<br/>
<br/>

<table class="tstyle5 thlight mtop05 thmiddle">
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
		</td>
		<td>
			<html:select property="tsd" onchange="this.form.method.value='loadTeacherServiceDistributions'; this.form.page.value=0; this.form.submit();">
				<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
		<logic:present name="parentGroupingName">
			<td>
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='deleteTeacherServiceDistribution'; this.form.submit();">
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

	<table class="tstyle5 thlight mtop05 thright thmiddle">
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.tsdTeacher"/>:
			</th>
			<logic:empty name="tsdTeacherList">
			<td align="center">
				<em><bean:message key="label.teacherServiceDistribution.groupingTeachersAlreadyIncluded"/></em>
			</logic:empty>
			<logic:notEmpty name="tsdTeacherList">
			<td>
				<html:select property="tsdTeacher">
					<html:options collection="tsdTeacherList" property="idInternal" labelProperty="name"/>
				</html:select>
			</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="tsdTeacherList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateTSDTeacher'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associate"/>
					</html:button>
				</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="tsdTeacherList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateAllTSDTeachers'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associateAll"/>
					</html:button>
				</logic:notEmpty>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.competenceCourse"/>:
			</th>
			<logic:empty name="tsdCourseList">
			<td align="center">
				<em><bean:message key="label.teacherServiceDistribution.groupingCoursesAlreadyIncluded"/></em>
			</logic:empty>
			<logic:notEmpty name="tsdCourseList">
			<td>
				<html:select property="tsdCourse">
					<html:options collection="tsdCourseList" property="idInternal" labelProperty="name"/>
				</html:select>		
			</logic:notEmpty>							
			</td>
			<td>
				<logic:notEmpty name="tsdCourseList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateCompetenceCourse'; this.form.submit()">
						<bean:message key="label.teacherServiceDistribution.associate"/>
					</html:button>
				</logic:notEmpty>
			</td>
			<td>
				<logic:notEmpty name="tsdCourseList">
					<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='associateAllCompetenceCourses'; this.form.submit()">
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


<table class="tstyle5 thlight thright thmiddle mtop05" id="optionsTable" style="display: none;">
	<tr>
		<th>		
			<bean:message key="label.teacherServiceDistribution.changeName"/>:
		</th>

</html:form>
<html:form action="/tsdSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadTeacherServiceDistributions"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>

		<td>
			<fr:edit id="name-validated" name="selectedTeacherServiceDistribution" slot="name" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator" scope="request">
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
			<span class="tdclear tderror1">
				<fr:message for="name-validated"/>
				<fr:hasMessages for="name-validated">
					<script language="JavaScript" type="text/javascript">
						changeSignal('signalImg','optionsTable');
					</script>
				</fr:hasMessages>
			</span>
		</td>
		
</html:form>
<html:form action="/tsdSupport">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourseDissociation" property="tsdCourseDissociation" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdTeacherDissociation" property="tsdTeacherDissociation" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsd" property="tsd"/>

	</tr>
	<tr>
		<th>		
			<bean:message key="label.teacherServiceDistribution.createNewGrouping"/>:
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" maxlength="240" size="44"/>
		</td>
		<td align="center">
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='createTeacherServiceDistribution'; this.form.page.value=1; this.form.submit();">
				&nbsp;<bean:message key="label.teacherServiceDistribution.create"/>&nbsp;
			</html:button>
		</td>
		<td></td>
	</tr>
	<%--<logic:present name="parentGroupingName">
		<logic:notEmpty name="mergeGroupingOptionEntryList">
		<tr>
			<th>
				<b><bean:message key="label.teacherServiceDistribution.mergeTeacherServiceDistributions"/>:
			</th>
			<td>
				<html:select property="otherGrouping">
					<html:options collection="mergeGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
				</html:select>			
			</td>
			<td align="center">
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='mergeTeacherServiceDistributions'; this.form.page.value=0; this.form.submit();">
					<bean:message key="label.teacherServiceDistribution.merge"/>
				</html:button>
			</td>
		</tr>
		</logic:notEmpty>
	</logic:present>--%>
</table>
<br/>

<span class="error0">
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

<table class='tstyle4' width="100%">
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.associatedTSDTeacher"/></b>
		</th>
		<logic:present name="parentGroupingName">
		<th>
		</th>
		</logic:present>
	</tr>
<logic:empty name="tsdTeacherListBelongToGrouping">
	<tr>
		<td colspan="2">
			<em><bean:message key="label.teacherServiceDistribution.notExistsTSDTeachers"/></em>
		</td>
	</tr>	
</logic:empty>		
<logic:iterate name="tsdTeacherListBelongToGrouping" id="tsdTeacher">
	<bean:define id="tsdTeacherId" name="tsdTeacher" property="idInternal"/>
	<tr>
		<td>
			<bean:write name="tsdTeacher" property="name"/>
		</td>
		<logic:present name="parentGroupingName">
			<td class="acenter">
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onmouseover="this.style.color='blue';" onmouseout="this.style.color='black';" styleClass="btn" 
				onclick='<%= "this.form.tsdTeacherDissociation.value=" + tsdTeacherId + ";this.form.method.value='dissociateTSDTeacher'; this.form.submit()" %>'>
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
<table class='tstyle4' width="100%">
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.associatedCompetenceCourse"/></b>
		</th>
		<logic:present name="parentGroupingName">
		<th>
		</th>
		</logic:present>
	</tr>
<logic:empty name="tsdCourseListBelongToGrouping">
	<tr>
		<td colspan="2">
			<em><bean:message key="label.teacherServiceDistribution.notExistsCompetenceCourses"/></em>
		</td>
	</tr>	
</logic:empty>
<logic:iterate name="tsdCourseListBelongToGrouping" id="tsdCourse">
	<bean:define id="tsdCourseId" name="tsdCourse" property="idInternal"/>
	<tr>
		<td>
			<bean:write name="tsdCourse" property="name"/>
		</td>
		<logic:present name="parentGroupingName">
			<td class="acenter">
				<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" styleClass="btn" onclick='<%= "this.form.tsdCourseDissociation.value=" + tsdCourseId + ";this.form.method.value='dissociateCompetenceCourse'; this.form.submit()" %>'>
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

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
