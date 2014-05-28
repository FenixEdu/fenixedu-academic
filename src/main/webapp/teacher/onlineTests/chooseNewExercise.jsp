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
<h2><bean:message key="title.createExercise" /></h2>

<logic:present name="metadataList">
	<bean:define id="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>" />
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.chooseCreateExerciseOrVariation.information" /></td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<td>
			<div class="gen-button"><html:link
				page="<%= "/exercisesManagement.do?method=prepareChooseExerciseType&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")%>">
				<bean:message key="label.newExercise" />
			</html:link></div>
			</td>
		</tr>
	</table>
	<br />
	<bean:size id="metadatasSize" name="metadataList" />
	<logic:equal name="metadatasSize" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.tests.no.exercises" /></span>
	</logic:equal>
	<logic:notEqual name="metadatasSize" value="0">
		<logic:present name="order">
			<bean:define id="order" name="order" />
		</logic:present>
		<logic:notPresent name="order">
			<bean:define id="order" value="description" />
		</logic:notPresent>
		<logic:present name="asc">
			<bean:define id="asc" name="asc" />
		</logic:present>
		<logic:notPresent name="asc">
			<bean:define id="asc" value="true" />
		</logic:notPresent>
		<br />
		<br />
		<table>
			<tr>
				<th class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="description">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=description&amp;asc=false" %>">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=description&amp;asc=false" %>">
							<img hspace="5" vspace="0" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=description"%>">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=description"%>">
							<img hspace="5" vspace="0" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="description">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=description"%>">
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
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=mainSubject&amp;asc=false"%>">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=mainSubject&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=mainSubject"%>">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=mainSubject"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="mainSubject">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=mainSubject"%>">
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
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=difficulty&amp;asc=false"%>">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=difficulty&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=difficulty"%>">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=difficulty"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="difficulty">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
					<html:link
						page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=difficulty"%>">
						<bean:message key="label.test.difficulty" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
				<th width="90" class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="numberOfMembers">
					<logic:equal name="asc" value="true">
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=numberOfMembers&amp;asc=false"%>">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=numberOfMembers&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="<bean:message key="upArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=numberOfMembers"%>">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=numberOfMembers"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="<bean:message key="downArrow" bundle="IMAGE_RESOURCES" />" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="numberOfMembers">
					<html:link
						page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+"&amp;order=numberOfMembers"%>">
						<bean:message key="label.test.quantidadeExercicios" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="<bean:message key="gray_square" bundle="IMAGE_RESOURCES" />" />
				</logic:notEqual></div>
				</th>
			</tr>
			<logic:iterate id="metadata" name="metadataList" type="net.sourceforge.fenixedu.domain.onlineTests.Metadata">
				<tr>
					<logic:notEqual name="metadata" property="description" value="">
						<td class="listClasses"><bean:write name="metadata" property="description" /></td>
					</logic:notEqual>
					<logic:equal name="metadata" property="description" value="">
						<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
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
					<bean:define id="exerciseCode" name="metadata" property="externalId" />
					<bean:define id="metadataCode" name="metadata" property="externalId" />
					<td>
					<div class="gen-button"><html:link
						page="<%= "/exercisesManagement.do?method=prepareChooseExerciseType&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode")%>">
						<bean:message key="lable.newVariation" />
					</html:link></div>
					</td>
				</tr>
			</logic:iterate>

		</table>
	</logic:notEqual>
</logic:present>
