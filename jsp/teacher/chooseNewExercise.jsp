<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.createExercise"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>
<table>
	<tr><td class="infoop"><bean:message key="message.chooseCreateExerciseOrVariation.information"/></td></tr>
</table>
<br/>
<table>
	<tr><td>
		<div class="gen-button">
			<html:link page="<%= "/exercisesManagement.do?method=prepareChooseExerciseType&amp;objectCode=" + pageContext.findAttribute("objectCode")%>">
				<bean:message key="label.newExercise" />
			</html:link>
		</div>
	</td></tr>
</table>
<br/>
<bean:size id="metadatasSize" name="component" property="infoMetadatas"/>
<logic:equal name="metadatasSize" value="0">
	<span class="error"><bean:message key="message.tests.no.exercises"/></span>
</logic:equal>
<logic:notEqual name="metadatasSize" value="0">
	<logic:present name="order">
		<bean:define id="order" name="order"/>
	</logic:present>
	<logic:notPresent name="order">
		<bean:define id="order" value="description"/>
	</logic:notPresent>
	<logic:present name="asc">
		<bean:define id="asc" name="asc"/>
	</logic:present>
	<logic:notPresent name="asc">
		<bean:define id="asc" value="true"/>
	</logic:notPresent>
	<br/>
	<br/>
	<table>
	<tr >
		<td class="listClasses-header">
		<div class="gen-button">
		<logic:equal name="order" value="description">
			<logic:equal name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description&amp;asc=false" %>">
				<bean:message key="label.description"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description&amp;asc=false" %>">
				<img hspace="5" vspace="0" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description"%>">
				<bean:message key="label.description"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description"%>">
				<img hspace="5" vspace="0" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="description">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=description"%>">
				<bean:message key="label.description"/>
				</html:link>
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
		</logic:notEqual>
		</div></td>
		<td class="listClasses-header">
		<div class="gen-button">
		<logic:equal name="order" value="mainSubject">
			<logic:equal name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=mainSubject&amp;asc=false"%>">
				<bean:message key="label.test.materiaPrincipal"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=mainSubject&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=mainSubject"%>">
				<bean:message key="label.test.materiaPrincipal"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=mainSubject"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="mainSubject">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
			<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=mainSubject"%>">
			<bean:message key="label.test.materiaPrincipal"/>
			</html:link>
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
		</logic:notEqual>
		</div></td>
		<td class="listClasses-header">
		<div class="gen-button">
		<logic:equal name="order" value="difficulty">
			<logic:equal name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=difficulty&amp;asc=false"%>">
				<bean:message key="label.test.difficulty"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=difficulty&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=difficulty"%>">
				<bean:message key="label.test.difficulty"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=difficulty"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="difficulty">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
			<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=difficulty"%>">
			<bean:message key="label.test.difficulty"/>
			</html:link>
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
		</logic:notEqual>
		</div></td>
		<td width="90" class="listClasses-header">
		<div class="gen-button">
		<logic:equal name="order" value="numberOfMembers">
			<logic:equal name="asc" value="true">
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=numberOfMembers&amp;asc=false"%>">
				<bean:message key="label.test.quantidadeExercicios"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=numberOfMembers&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=numberOfMembers"%>">
				<bean:message key="label.test.quantidadeExercicios"/>
				</html:link>
				<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=numberOfMembers"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="numberOfMembers">
			<html:link page="<%= "/exercisesManagement.do?method=chooseNewExercise&amp;objectCode=" + pageContext.findAttribute("objectCode")+"&amp;order=numberOfMembers"%>">
			<bean:message key="label.test.quantidadeExercicios"/>
			</html:link>
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
		</logic:notEqual>
		</div></td>
	</tr>
	<logic:iterate id="metadata" name="component" property="infoMetadatas" type="DataBeans.InfoMetadata">
	<tr>
		<logic:notEqual name="metadata" property="description" value="">
			<td class="listClasses"><bean:write name="metadata" property="description"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="description" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="mainSubject" value="">
			<td class="listClasses"><bean:write name="metadata" property="mainSubject"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="mainSubject" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="difficulty" value="">
			<td class="listClasses"><bean:write name="metadata" property="difficulty"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="difficulty" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<logic:notEqual name="metadata" property="numberOfMembers" value="">
			<td class="listClasses"><bean:write name="metadata" property="numberOfMembers"/></td>
		</logic:notEqual>
		<logic:equal name="metadata" property="numberOfMembers" value="">
			<td class="listClasses"><bean:message key="message.tests.notDefined"/></td>
		</logic:equal>
		<bean:define id="exerciseCode" name="metadata" property="idInternal"/>
		<bean:define id="metadataCode" name="metadata" property="idInternal" />
		<td><div class="gen-button">
			<html:link page="<%= "/exercisesManagement.do?method=prepareChooseExerciseType&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;exerciseCode=" + pageContext.findAttribute("exerciseCode")%>">
				<bean:message key="lable.newVariation" />
			</html:link>
		</div></td>
	</tr>
	</logic:iterate>

	</table>
</logic:notEqual>
</logic:present>