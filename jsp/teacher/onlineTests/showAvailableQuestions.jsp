<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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
	for (var i=0; i<document.questionForm.metadataCode.length; i++){
		var e = document.questionForm.metadataCode[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}
// -->
</script>
<logic:present name="infoMetadataList">
	<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<span class="error"><html:errors /></span>

	<bean:size id="metadatasSize" name="infoMetadataList" />
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
						<html:hidden property="page" value="0" />
						<html:hidden property="method" value="editTest" />
						<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
						<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
						<html:submit styleClass="inputbutton">
							<bean:message key="button.continue" />
						</html:submit>
					</html:form></td>
					<td><html:form action="/testsManagement">
						<html:hidden property="page" value="0" />
						<html:hidden property="method" value="deleteTest" />
						<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
						<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
						<html:submit styleClass="inputbutton">
							<bean:message key="link.removeTest" />
						</html:submit>
					</html:form></td>
				</tr>
			</table>
			<br />
			<br />
		</logic:greaterThan>
		
		<html:form action="/questionsManagement">
			<html:hidden property="page" value="1" />
			<html:hidden property="method" value="insertTestQuestion" />
			<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
			<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
			<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>" />
			<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>" />
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
						<td><html:submit styleClass="inputbutton">
							<bean:message key="button.insert" />
						</html:submit>
		</html:form>
		</td>
		</tr>
		</table>
		</logic:greaterThan>
		<br />
		<br />
		<table>
			<tr>
				<td class="listClasses-header"></td>
				<td class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="description">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>"
							titleKey="label.orderByDescription">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>"
							titleKey="label.orderByDescription">
							<bean:message key="label.description" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="description">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>"
						titleKey="label.orderByDescription">
						<bean:message key="label.description" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				</logic:notEqual></div>
				</td>
				<td class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="mainSubject">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>"
							titleKey="label.orderByMateriaPrincipal">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>"
							titleKey="label.orderByMateriaPrincipal">
							<bean:message key="label.test.materiaPrincipal" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="mainSubject">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>"
						titleKey="label.orderByMateriaPrincipal">
						<bean:message key="label.test.materiaPrincipal" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				</logic:notEqual></div>
				</td>
				<td class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="difficulty">
					<logic:equal name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>"
							titleKey="label.orderByDifficulty">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>"
							titleKey="label.orderByDifficulty">
							<bean:message key="label.test.difficulty" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="difficulty">
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>"
						titleKey="label.orderByDifficulty">
						<bean:message key="label.test.difficulty" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				</logic:notEqual></div>
				</td>
				<td width="90" class="listClasses-header">
				<div class="gen-button"><logic:equal name="order" value="numberOfMembers">
					<logic:equal name="asc" value="true">
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>"
							titleKey="label.orderByQuantidadeExercicios">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
						</html:link>
					</logic:equal>
					<logic:notEqual name="asc" value="true">
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>"
							titleKey="label.orderByQuantidadeExercicios">
							<bean:message key="label.test.quantidadeExercicios" />
						</html:link>
						<html:link
							page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>">
							<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
						</html:link>
					</logic:notEqual>
				</logic:equal> <logic:notEqual name="order" value="numberOfMembers">
					<html:link
						page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>"
						titleKey="label.orderByQuantidadeExercicios">
						<bean:message key="label.test.quantidadeExercicios" />
					</html:link>
					<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				</logic:notEqual></div>
				</td>
			</tr>
			<logic:iterate id="metadata" name="infoMetadataList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata">
				<tr>
					<bean:define id="metadataCode" name="metadata" property="idInternal" />
					<td class="listClasses"><html:multibox property="metadataCode">
						<bean:write name="metadataCode" />
					</html:multibox></td>
					<logic:notEqual name="metadata" property="description" value="">
						<td class="listClasses">
						<div class="gen-button"><html:link
							page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode+ "&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
							<bean:write name="metadata" property="description" />
						</html:link></div>
						</td>
					</logic:notEqual>
					<logic:equal name="metadata" property="description" value="">
						<td class="listClasses">
						<div class="gen-button"><html:link
							page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode+ "&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
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
					<logic:notEqual name="metadata" property="numberOfMembers" value="">
						<td class="listClasses"><bean:write name="metadata" property="numberOfMembers" /></td>
					</logic:notEqual>
					<logic:equal name="metadata" property="numberOfMembers" value="">
						<td class="listClasses"><bean:message key="message.tests.notDefined" /></td>
					</logic:equal>
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
				<td><html:submit styleClass="inputbutton">
					<bean:message key="button.insert" />
				</html:submit> </html:form></td>
			</tr>
		</table>
		
	</logic:notEqual>
	<logic:equal name="metadatasSize" value="0">
		<span class="error"><bean:message key="message.tests.no.exercises" /></span>
	</logic:equal>

	<br />
	<br />
	<table>
		<tr>
			<td><html:form action="/testEdition">
				<html:hidden property="page" value="0" />
				<html:hidden property="method" value="editTest" />
				<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
				<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
				<html:submit styleClass="inputbutton">
					<bean:message key="button.continue" />
				</html:submit>
			</html:form></td>
			<td><html:form action="/testsManagement">
				<html:hidden property="page" value="0" />
				<html:hidden property="method" value="deleteTest" />
				<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
				<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>" />
				<html:submit styleClass="inputbutton">
					<bean:message key="link.removeTest" />
				</html:submit>
			</html:form></td>
		</tr>
	</table>

</logic:present>
