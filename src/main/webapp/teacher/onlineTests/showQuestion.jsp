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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<logic:present name="iquestion">
<bean:define id="showResponses" value='<%=request.getParameter("showResponses")%>'/>
<bean:define id="questionCode" name="iquestion" property="externalId"/>
<bean:define id="metadataCode" name="metadataId" />

<bean:size id="subQuestionsSize" name="iquestion" property="subQuestions"/>

<logic:iterate id="subQuestion" name="iquestion" property="subQuestions" indexId="itemIndex">
<bean:define id="item" value="<%=itemIndex.toString()%>"/>
<br/>
<bean:define id="index" value="0"/>
<bean:define id="imageLabel" value="false"/>
<table>
<tr><td>
<logic:iterate id="questionBody" name="subQuestion" property="prePresentation" indexId="indexQuestion">
	<bean:define id="questionLabel" name="questionBody" property="label"/>
	<% if (((String)questionLabel).startsWith("image/")){%>
		<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
		<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()+"&amp;metadataCode="+metadataCode%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
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
<tr><td><h2><bean:write name="subQuestion" property="itemId"/></h2></td></tr>
<tr><td><b><bean:write name="subQuestion" property="title"/></b></td></tr>
<tr><td><b>
<logic:greaterThan name="subQuestionsSize" value="1">
	<bean:message key="message.tests.subQuestionValue"/>
</logic:greaterThan>
<logic:lessEqual name="subQuestionsSize" value="1">
	<bean:message key="message.tests.questionValue"/>
</logic:lessEqual>
</b><bean:write name="subQuestion" property="questionValue"/></td></tr>
<tr><td>
<logic:iterate id="questionBody" name="subQuestion" property="presentation" indexId="indexQuestion">
	<bean:define id="questionLabel" name="questionBody" property="label"/>
	<% if (((String)questionLabel).startsWith("image/")){%>
		<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
		<html:img bundle="HTMLALT_RESOURCES" altKey="img.img" align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()+"&amp;metadataCode="+metadataCode%>"/>
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
<logic:notEmpty name="subQuestion" property="options">

<bean:define id="cardinality" name="subQuestion" property="questionType.cardinalityType.type"/>
<bean:define id="questionType" name="subQuestion" property="questionType.type"/>
<bean:define id="imageLabel" value="false"/>
<bean:define id="firstOptionImage" value="<%=index.toString()%>"/>

<logic:iterate id="questionOption" name="subQuestion" property="options" indexId="indexOption">
<logic:iterate id="optionBody" name="questionOption" property="optionContent" indexId="optionBodyIndex">
	<bean:define id="optionLabel" name="optionBody" property="label"/>
	<% if (((String)optionLabel).startsWith("image/")){ %>
		<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
		<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;item="+item.toString()+"&amp;metadataCode="+metadataCode%>"  altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
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
			<bean:define id="newOptionIndex" value="<%= (new Integer(Integer.parseInt(indexOption.toString())+1)).toString() %>"/>
			<%	if(((Integer)cardinality).intValue()==1 ){ %> <%-- Cardinality.SINGLE--%>
				</td></tr><tr>
				<td><b><bean:write name="newOptionIndex"/></b></td>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.option" property="option" value="<%= newOptionIndex.toString() %>" disabled="true"/></td>
				<td>
			<% }else if(((Integer)cardinality).intValue()==2){ %> <%-- Cardinality.MULTIPLE--%>
				</td></tr><tr>
				<td><b><bean:write name="newOptionIndex"/></b></td>
				<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.option" property="option" value="<%=newOptionIndex.toString() %>" disabled="true"></html:multibox></td>
				<td>
				
			<%}%>
		<%}else{ %><%-- QuestionType.STR or QuestionType.NUM--%>
			<logic:notEmpty name="subQuestion" property="questionType.render.maxchars">
				<bean:define id="maxchars" name="subQuestion" property="questionType.render.maxchars"/>
				<logic:notEmpty name="subQuestion" property="questionType.render.columns">
					<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.option" maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" property="option" value="" disabled="true"/>
				</logic:notEmpty>
				<logic:empty name="subQuestion" property="questionType.render.columns">
					<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
					<logic:greaterThan name="textBoxSize" value="100" >
						<bean:define id="textBoxSize" value="100"/>
					</logic:greaterThan>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.option" maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" property="option" value="" disabled="true"/>
				</logic:empty>
			</logic:notEmpty>	
			<logic:empty name="subQuestion" property="questionType.render.maxchars">
				<logic:notEmpty name="subQuestion" property="questionType.render.rows">
					<bean:define id="rows" name="subQuestion" property="questionType.render.rows"/>
					<logic:notEmpty name="subQuestion" property="questionType.render.columns">
						<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.option" rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" property="option" disabled="true"/>
					</logic:notEmpty>
					<logic:empty name="subQuestion" property="questionType.render.columns">
						<html:textarea  bundle="HTMLALT_RESOURCES" altKey="textarea.option" rows="<%=rows.toString()%>" property="option" disabled="true"/>
					</logic:empty>
				</logic:notEmpty>
				<logic:empty name="subQuestion" property="questionType.render.rows">
					<logic:notEmpty name="subQuestion" property="questionType.render.columns">
						<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
						<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.option" cols="<%=cols.toString()%>" property="option" disabled="true"/>
					</logic:notEmpty>
					<logic:empty name="subQuestion" property="questionType.render.columns">
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

</td></tr>
</table>

<br/>
<logic:equal name="showResponses" value="true">
<table border="1" bordercolor="#ccc" cellspacing="0">
<logic:notEmpty name="subQuestion" property="responseProcessingInstructions">
	<logic:iterate id="rp" name="subQuestion" property="responseProcessingInstructions" indexId="rpIndex">
	<bean:define id="imageLabel" value="false"/>
	<tr>
	<logic:equal name="rpIndex" value="0">
		<td><b><bean:message key="label.questionValue"/></b></td>
		<td><b>Resposta</b></td>
		<td><b>Feedback</b></td>
		<td><b>Fenix</b></td>
		<td><b><bean:message key="label.nextSubQuestion"/></b></td>
		</tr><tr>
	</logic:equal>
	<td><bean:write name="rp" property="responseValue"/></td>
	<td>
			<bean:write name="rp" property="completeResponseAsString" />
	</td>
	<td>
		<logic:notEmpty name="rp" property="feedback">
			<bean:define id="feedbackIndex" value="<%=index.toString()%>"/>
			<logic:iterate id="feedback" name="rp" property="feedback">
				<bean:define id="feedbackLabel" name="feedback" property="label"/>
				<%if (((String)feedbackLabel).startsWith("image/")){%>
					<bean:define id="feedbackIndex" value="<%= (new Integer(Integer.parseInt(feedbackIndex)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;exerciseCode=" + questionCode+"&amp;imgCode="+feedbackIndex.toString() +"&amp;feedbackCode="+rpIndex+"&amp;imgType="+feedbackLabel.toString()+"&amp;item="+item.toString()+"&amp;metadataCode="+metadataCode%>" altKey="feedbackLabel" bundle="IMAGE_RESOURCES"/>
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
	<td>
	<logic:empty name="rp" property="nextItem">
		---
	</logic:empty>
	<logic:notEmpty name="rp" property="nextItem">
		<bean:write name="rp" property="nextItem"/>
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
</logic:notEmpty>

</logic:iterate>
</logic:present>