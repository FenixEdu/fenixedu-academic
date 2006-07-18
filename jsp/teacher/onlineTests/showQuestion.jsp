<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="iquestion">

<bean:define id="showResponses" value='<%=request.getParameter("showResponses")%>'/>
<bean:define id="metadataCode" name="metadataId" />
<bean:define id="questionCode" name="iquestion" property="idInternal"/>
<bean:define id="index" value="0"/>
<bean:define id="imageLabel" value="false"/>
<table>
<tr><td>
<logic:iterate id="questionBody" name="iquestion" property="question" indexId="indexQuestion">
	<bean:define id="questionLabel" name="questionBody" property="label"/>
	<% if (((String)questionLabel).startsWith("image/")){%>
		<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
		<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;metadataCode="+metadataCode%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
		<logic:equal name="imageLabel" value="true">
			</td><td>
		</logic:equal>
	<% } else if (((String)questionLabel).equals("image_label")){%>		
		<logic:equal name="imageLabel" value="false">
			<bean:define id="imageLabel" value="true"/>
			<table><tr><td>
		</logic:equal>
		<bean:write name="questionBody" property="value"/>
		<br/>
	<% }else if (((String)questionLabel).equals("flow")){%>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		</td></tr>
		<tr><td>
	<% }else{%>
		<bean:write name="questionBody" property="value"/>
	<% } %>
</logic:iterate>
<logic:equal name="imageLabel" value="true">
	</td></tr></table>
</logic:equal>

<bean:define id="cardinality" name="iquestion" property="questionType.cardinalityType.type"/>
<bean:define id="questionType" name="iquestion" property="questionType.type"/>
<bean:define id="imageLabel" value="false"/>
<bean:define id="firstOptionImage" value="<%=index.toString()%>"/>

<bean:define id="showOptions" value="true"/>
<logic:equal name="showResponses" value="true">
	<%	if(((Integer)questionType).intValue()==1 ){ %> <%-- QuestionType.LID--%>
	<logic:notEmpty name="iquestion" property="responseProcessingInstructions">
		<bean:define id="showOptions" value="false"/>
	</logic:notEmpty>
	<%}%>
</logic:equal>

<logic:equal name="showOptions" value="true">
<logic:iterate id="questionOption" name="iquestion" property="options" indexId="indexOption">
<logic:iterate id="optionBody" name="questionOption" property="optionContent" indexId="optionBodyIndex">
	<bean:define id="optionLabel" name="optionBody" property="label"/>
	<% if (((String)optionLabel).startsWith("image/")){ %>
		<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
		<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;metadataCode="+metadataCode%>" altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
	<% } else if (((String)optionLabel).equals("image_label")){%>
		<logic:equal name="imageLabel" value="false">
		<bean:define id="imageLabel" value="true"/>
			<table><tr><td>
		</logic:equal>
		<bean:write name="optionBody" property="value"/>
		<br/>
	<% } else if (((String)optionLabel).equals("response_label")){ %>
		<%	if(((Integer)questionType).intValue()==1 ){ %> <%-- QuestionType.LID--%>
			<logic:equal name="optionBodyIndex" value="0">
				</td></tr></table><table><tr><td>
			</logic:equal>

			<%	if(((Integer)cardinality).intValue()==1 ){ %> <%-- Cardinality.SINGLE--%>
				</td></tr><tr>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.option" property="option" value="<%= indexOption.toString() %>" disabled="true"/></td>
				<td>
			<% }else if(((Integer)cardinality).intValue()==2){ %> <%-- Cardinality.MULTIPLE--%>
				</td></tr><tr>
				<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.option" property="option" value="<%= new Integer(indexOption.intValue()+1).toString() %>" disabled="true"></html:multibox></td>
				<td>
				
			<%}%>
		<%}else{ %><%-- QuestionType.STR or QuestionType.NUM--%>
			<logic:notEmpty name="iquestion" property="questionType.render.maxchars">
				<bean:define id="maxchars" name="iquestion" property="questionType.render.maxchars"/>
				<logic:notEmpty name="iquestion" property="questionType.render.columns">
					<bean:define id="cols" name="iquestion" property="questionType.render.columns"/>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.option" maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" property="option" value="" disabled="true"/>
				</logic:notEmpty>
				<logic:empty name="iquestion" property="questionType.render.columns">
					<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
					<logic:greaterThan name="textBoxSize" value="100" >
						<bean:define id="textBoxSize" value="100"/>
					</logic:greaterThan>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.option" maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" property="option" value="" disabled="true"/>
				</logic:empty>
			</logic:notEmpty>	
			<logic:empty name="iquestion" property="questionType.render.maxchars">
				<logic:notEmpty name="iquestion" property="questionType.render.rows">
					<bean:define id="rows" name="iquestion" property="questionType.render.rows"/>
					<logic:notEmpty name="iquestion" property="questionType.render.columns">
						<bean:define id="cols" name="iquestion" property="questionType.render.columns"/>
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.option" rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" property="option" disabled="true"/>
					</logic:notEmpty>
					<logic:empty name="iquestion" property="questionType.render.columns">
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.option" rows="<%=rows.toString()%>" property="option" disabled="true"/>
					</logic:empty>
				</logic:notEmpty>
				<logic:empty name="iquestion" property="questionType.render.rows">
					<logic:notEmpty name="iquestion" property="questionType.render.columns">
						<bean:define id="cols" name="iquestion" property="questionType.render.columns"/>
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.option" cols="<%=cols.toString()%>" property="option" disabled="true"/>
					</logic:notEmpty>
					<logic:empty name="iquestion" property="questionType.render.columns">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.option" property="option" value="" disabled="true"/>
					</logic:empty>
				</logic:empty>
			</logic:empty>
		<%}%>
	<% }else if (((String)optionLabel).equals("flow")){%>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		</td></tr>
		<tr><td>
	<% } else {%>
		<bean:write name="optionBody" property="value"/>
	<% } %>
</logic:iterate>
</logic:iterate>
</logic:equal>	
</td></tr>
</table>

<br/>
<logic:equal name="showResponses" value="true">
<table border="1" bordercolor="#ccc" cellspacing="0">

<logic:notEmpty name="iquestion" property="responseProcessingInstructions">
	<logic:iterate id="rp" name="iquestion" property="responseProcessingInstructions" indexId="rpIndex">
	<bean:define id="imageLabel" value="false"/>
	<bean:define id="index" value="<%=firstOptionImage.toString()%>"/>
	<tr>
	<logic:equal name="rpIndex" value="0">
		<td><b>Cota��o</b></td>
		<td><b>Resposta</b></td>
		<td><b>Feedback</b></td>
		<td><b>Fenix</b></td>
		</tr><tr>
	</logic:equal>
	<td><bean:write name="rp" property="responseValue"/></td>
	<td>
		<%if(((Integer)questionType).intValue()==1 ){ %> <%-- QuestionType.LID--%>
			<table><tr><td>
			<logic:iterate id="questionOption" name="iquestion" property="options" indexId="indexOption">
			<logic:iterate id="optionBody" name="questionOption" property="optionContent">
			<bean:define id="optionLabel" name="optionBody" property="label"/>
			<%if (((String)optionLabel).startsWith("image/")){ %>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;metadataCode="+metadataCode%>" altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
			<%} else if (((String)optionLabel).equals("image_label")){%>
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="optionBody" property="value"/>
				<br/>
			<%} else if (((String)optionLabel).equals("response_label")){ %>
				</td></tr><tr>
				<bean:define id="button" value="true"/>
				<%if(((Integer)cardinality).intValue()==1 ){ %> <%-- Cardinality.SINGLE--%>
					<logic:iterate id="correctResponse" name="rp" property="responseConditions">
						<bean:define id="button" value="true"/>
						<logic:notEmpty name="correctResponse">
							<logic:equal name="correctResponse" property="response" value="<%= new Integer(indexOption.intValue()+1).toString() %>">
								<td><img src="<%= request.getContextPath() %>/images/radioButtonUnselected.gif" alt="<bean:message key="radioButtonUnselected" bundle="IMAGE_RESOURCES" />" /></td>
								<bean:define id="button" value="false"/>
							</logic:equal>
						</logic:notEmpty>
						<logic:equal name="button" value="true">
							<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.option" property="option" value="<%= new Integer(indexOption.intValue()+1).toString() %>" disabled="true"/></td>
						</logic:equal>
					</logic:iterate>
				<%}else if(((Integer)cardinality).intValue()==2){ %> <%-- Cardinality.MULTIPLE--%>
					<bean:define id="button" value="true"/>
					<logic:iterate id="correctResponse" name="rp" property="responseConditions">
						<logic:notEmpty name="correctResponse">
							<bean:define id="condition" name="correctResponse" property="condition"/>
								<%if(((Integer)condition).intValue() ==1){%>
									<logic:equal name="correctResponse" property="response" value="<%= new Integer(indexOption.intValue()+1).toString() %>">
										<td><img src="<%= request.getContextPath() %>/images/checkButtonUnselected.gif" alt="<bean:message key="checkButtonUnselected" bundle="IMAGE_RESOURCES" />" /></td>
										<bean:define id="button" value="false"/>
									</logic:equal>
								<%}%>
						</logic:notEmpty>
					</logic:iterate>
					<logic:equal name="button" value="true">
						<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.option" property="option" value="<%= new Integer(indexOption.intValue()+1).toString() %>" disabled="true"/></td>
					</logic:equal>
				<%}%>
			
				<td>
			<%}else if (((String)optionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr>
				<tr><td>
			<% } else {%>
				<bean:write name="optionBody" property="value"/>
			<% } %>
			</logic:iterate>
			</logic:iterate>
			</td></tr></table>
		<%}else{ %><%-- QuestionType.STR or QuestionType.NUM--%>
			<bean:define id="action" name="rp" property="action"/>
			<bean:define id="oneCorrectResponse" value=""/>
			<logic:iterate id="correctResponse" name="rp" property="responseConditions">
				<bean:define id="response2" name="correctResponse" property="response"/>
				<bean:define id="responseCondition" name="correctResponse" property="condition"/>
				<%if(((Integer)responseCondition).intValue() >1 || ((Integer)responseCondition).intValue()<14){%>
					<bean:define id="conditionSignal" name="correctResponse" property="conditionSignal"/>
					<logic:notEqual name="oneCorrectResponse" value="">
						<bean:define id="oneCorrectResponse" value='<%=oneCorrectResponse.toString().concat(" e ")%>'/>
					</logic:notEqual>
					<bean:define id="oneCorrectResponse" value="<%=oneCorrectResponse.toString().concat(conditionSignal.toString().concat(response2.toString()))%>"/>
				<%}else{%>
					<bean:define id="oneCorrectResponse" value="<%=response2.toString()%>"/>
				<%}%>
			</logic:iterate>
			<bean:define id="value" value="<%=oneCorrectResponse.toString()%>"/>
			<bean:write name="value" />
		<%}%>
	</td>
	<td>
		<logic:notEmpty name="rp" property="feedback">
			<logic:iterate id="feedback" name="rp" property="feedback">
				<bean:define id="feedbackLabel" name="feedback" property="label"/>
				<%if (((String)feedbackLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;feedbackCode="+rpIndex+"&amp;imgType="+feedbackLabel.toString()+"&amp;metadataCode="+metadataCode%>" altKey="feedbackLabel" bundle="IMAGE_RESOURCES"/>
					<logic:equal name="imageLabel" value="true">
						</td><td>
					</logic:equal>
				<% } else if (((String)feedbackLabel).equals("image_label")){%>		
					<logic:equal name="imageLabel" value="false">
						<bean:define id="imageLabel" value="true"/>
						<table><tr><td>
					</logic:equal>
					<bean:write name="feedback" property="value"/>
					<br/>
				<% }else if (((String)feedbackLabel).equals("flow")){%>
					<logic:equal name="imageLabel" value="true">
						</td></tr></table>
						<bean:define id="imageLabel" value="false"/>
					</logic:equal>
					</td></tr><tr><td>
				<% }else{%>
					<bean:write name="feedback" property="value"/>
				<% } %>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="rp" property="feedback">
			---
		</logic:empty>
	</td>
	<td>
		<logic:notEmpty name="rp" property="fenixCorrectResponse">
			<logic:equal name="rp" property="fenixCorrectResponse" value="true">
				<bean:message key="label.yes"/>
			</logic:equal>
			<logic:notEqual name="rp" property="fenixCorrectResponse" value="true">
				<bean:message key="label.no"/>
			</logic:notEqual>
		</logic:notEmpty>
	</td>
	</tr>
	</logic:iterate>
</logic:notEmpty>

<%--
<bean:define id="index" value="<%=firstOptionImage.toString()%>"/>
<logic:iterate id="optionBody" name="iquestion" property="options" indexId="optionBodyIndex">	
</logic:iterate>
	--%>
</table>
</logic:equal>
</logic:present>