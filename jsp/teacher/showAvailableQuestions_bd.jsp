<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showAvailableQuestions"/></h2>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<span class="error"><html:errors/></span>

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
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.showAvailableQuestions.information" /></td>
		</tr>
	</table>
	<br/>
	<br/>
	<table>
	<tr>
		<td class="listClasses-header">
		<div class="gen-button">
		<logic:equal name="order" value="description">
			<logic:equal name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>">
				<bean:message key="label.description"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>">
				<bean:message key="label.description"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="description">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
			<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=description"%>">
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
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>">
				<bean:message key="label.test.materiaPrincipal"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>">
				<bean:message key="label.test.materiaPrincipal"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="mainSubject">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
			<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=mainSubject"%>">
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
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>">
				<bean:message key="label.test.difficulty"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>">
				<bean:message key="label.test.difficulty"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="difficulty">
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
			<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=difficulty"%>">
			<bean:message key="label.test.difficulty"/>
			</html:link>
			<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/gray_square.gif" alt="" />
		</logic:notEqual>
	</div></td>
	<td width="90" class="listClasses-header">
	<div class="gen-button">
		<logic:equal name="order" value="numberOfMembers">
			<logic:equal name="asc" value="true">
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>">
				<bean:message key="label.test.quantidadeExercicios"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers&amp;asc=false"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/upArrow.gif" alt="" />
				</html:link>
			</logic:equal>
			<logic:notEqual name="asc" value="true">
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>">
				<bean:message key="label.test.quantidadeExercicios"/>
				</html:link>
				<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>">
				<img hspace="5" border="0" src="<%= request.getContextPath() %>/images/downArrow.gif" alt="" />
				</html:link>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="order" value="numberOfMembers">
			<html:link page="<%= "/questionsManagement.do?method=showAvailableQuestions&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;testCode=" + pageContext.findAttribute("testCode")+ "&amp;order=numberOfMembers"%>">
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
		<bean:define id="metadataCode" name="metadata" property="idInternal" />
		<td>
			<div class="gen-button">
			<html:link page="<%= "/questionsManagement.do?method=prepareInsertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode+ "&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
			<bean:message key="link.see" />
			</html:link></div>
		</td>
		<td>
			<div class="gen-button">
			<html:link page="<%= "/questionsManagement.do?method=insertTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;testCode=" + pageContext.findAttribute("testCode") + "&amp;metadataCode=" + metadataCode+ "&amp;order="+ pageContext.findAttribute("order")+"&amp;asc="+pageContext.findAttribute("asc")%>">
			<bean:message key="button.insert" />
			</html:link></div>
		</td>
	</tr>
	</logic:iterate>
	</table>
</logic:notEqual>
<br/>
<br/>
	<table>
	<tr><td>
		<html:form action="/testEdition">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="editTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
		</html:form>
	</td>
	<td>
	<html:form action="/testsManagement">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="deleteTest"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
			<html:submit styleClass="inputbutton"><bean:message key="link.removeTest"/></html:submit>
		</html:form>
	</td></tr>
	</table>
</logic:present>