<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<h2><bean:message key="title.showAvailableQuestions" /></h2>
<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}

	if(document.forms[2].metadataCode.type=='checkbox'){
		var e = document.forms[2].metadataCode;
		e.checked = select;
	}else{
		for (var i=0; i<document.forms[2].metadataCode.length; i++){
			var e = document.forms[2].metadataCode[i];
			e.checked = select;
		}
	}
}
// -->
</script>
<logic:present name="metadataList">

	<bean:define id="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
	<bean:size id="metadatasSize" name="metadataList" />

	<logic:notEqual name="metadatasSize" value="0">

		<logic:present name="order">
			<bean:define id="order" name="order" />
		</logic:present>
		<logic:present name="asc">
			<bean:define id="asc" name="asc" />
		</logic:present>
		<logic:notPresent name="asc">
			<bean:define id="asc" value="true" />
		</logic:notPresent>

		<table>
			<tr>
				<td class="infoop"><bean:message key="message.showAvailableQuestions.information" /></td>
			</tr>
		</table>
			
		<br />
		<br />
			
		<logic:greaterThan name="metadatasSize" value="10">
			<table>
				<tr>
					<td><html:form action="/testEdition">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTest" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message key="button.continue" />
						</html:submit>
					</html:form></td>
					<td><html:form action="/testsManagement">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteTest" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message key="link.removeTest" />
						</html:submit>
					</html:form></td>
				</tr>
			</table>
			<br />
			<br />
		</logic:greaterThan>
		
		<html:form action="/questionsManagement">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertTestQuestion" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
			<logic:greaterThan name="metadatasSize" value="10">
				<table>
					<tr>
						<td colspan="2">
						<div class="gen-button"><html:link href="javascript:invertSelect()" titleKey="label.selectAllExercises">
							<bean:message key="label.selectAllExercises" />
						</html:link>
						<div>
						</td>
					</tr>
					<tr>
						<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message key="button.insert" />
						</html:submit>
					</td>
					</tr>
				</table>
			</logic:greaterThan>

		<br />
		<br />
		<table>
			<tr>
				<td class="listClasses-header"></td>
				<th class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="description">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>"
							titleKey="label.orderByDescription">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>"
							titleKey="label.orderByDescription">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="description">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>"
						titleKey="label.orderByDescription">
						<bean:message key="label.description" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
				<th class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="mainSubject">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>"
							titleKey="label.orderByMateriaPrincipal">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>"
							titleKey="label.orderByMateriaPrincipal">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="mainSubject">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>"
						titleKey="label.orderByMateriaPrincipal">
						<bean:message key="label.test.materiaPrincipal" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
				<th class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="difficulty">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>"
							titleKey="label.orderByDifficulty">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>"
							titleKey="label.orderByDifficulty">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="difficulty">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>"
						titleKey="label.orderByDifficulty">
						<bean:message key="label.test.difficulty" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
				<th width="90" class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="numberOfMembers">
					<logic:equal name="asc" value="true">
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>"
							titleKey="label.orderByQuantidadeExercicios">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>"
							titleKey="label.orderByQuantidadeExercicios">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="numberOfMembers">
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>"
						titleKey="label.orderByQuantidadeExercicios">
						<bean:message key="label.test.quantidadeExercicios" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
			</tr>
			
			<logic:iterate id="metadata" name="metadataList" type="net.sourceforge.fenixedu.domain.onlineTests.Metadata">
			
				<tr>
					<bean:define id="metadataId" name="metadata" property="externalId" />
						
					<td class="listClasses">
					
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.metadataCode" property="metadataCode">
					
					<bean:write name="metadataId" />
					
					</html:multibox>
					
					
					</td>
						
					<logic:notEqual name="metadata" property="description" value="">
						<td class="listClasses">
						<div class="gen-button"><html:link
							page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataId+"&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
							<bean:write name="metadata" property="description" />
						</html:link></div>
						</td>
					</logic:notEqual>
					<logic:equal name="metadata" property="description" value="">
						<td class="listClasses">
						<div class="gen-button"><html:link
							page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataId+ "&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
							<bean:message key="message.tests.notDefined" />
						</html:link></div>
						</td>
					</logic:equal>
				
					<logic:notEqual name="metadata" property="mainSubject" value="">
						<td class="listClasses"><bean:write name="metadata" property="mainSubject" /></td>
					</logic:notEqual>
					<logic:equal name="metadata" property="mainSubject" value="">
						<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
					</logic:equal>
					<logic:notEqual name="metadata" property="difficulty" value="">
						<td class="listClasses"><bean:write name="metadata" property="difficulty" /></td>
					</logic:notEqual>
					<logic:equal name="metadata" property="difficulty" value="">
						<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
					</logic:equal>
					<bean:size id="numberOfMembers" name="metadata" property="visibleQuestions"/>
					<td class="listClasses"><bean:write name="numberOfMembers" /></td>
				</tr>
				
			</logic:iterate>
		</table>
			
		<br />
		<br />
		<table>
			<tr>
				<td colspan="2">
				<div class="gen-button"><html:link href="javascript:invertSelect()" titleKey="label.selectAllExercises">
					<bean:message key="label.selectAllExercises" />
				</html:link>
				<div>
				</td>
			</tr>
			<tr>
				<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.insert" />
				</html:submit> </td>
			</tr>
		</table>
	</html:form>
	</logic:notEqual>
	<logic:equal name="metadatasSize" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.tests.no.exercises" /></span>
	</logic:equal>

	<br />
	<br />
	<table>
		<tr>
			<td><html:form action="/testEdition">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editTest" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.continue" />
				</html:submit>
			</html:form></td>
			<td><html:form action="/testsManagement">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteTest" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="link.removeTest" />
				</html:submit>
			</html:form></td>
		</tr>
	</table>

</logic:present>
