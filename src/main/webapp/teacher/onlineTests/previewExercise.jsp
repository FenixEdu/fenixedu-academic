<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/exerciseCreation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareCreateExercise"/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationQuestion" property="evaluationQuestion"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.author" property="author"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.description" property="description"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.difficulty" property="difficulty"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mainSubject" property="mainSubject"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.secondarySubject" property="secondarySubject"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.learningHour" property="learningHour"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.learningMinute" property="learningMinute"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.level" property="level"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionType" property="questionType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.cardinalityType" property="cardinalityType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.optionNumber" property="optionNumber"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionValue" property="questionValue"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionText" property="questionText"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.secondQuestionText" property="secondQuestionText"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.breakLineBeforeResponseBox" property="breakLineBeforeResponseBox"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.breakLineAfterResponseBox" property="breakLineAfterResponseBox"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.correctFeedbackText" property="correctFeedbackText"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.wrongFeedbackText" property="wrongFeedbackText"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.cols" property="cols"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.rows" property="rows"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.maxChar" property="maxChar"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.caseSensitive" property="caseSensitive"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.integerType" property="integerType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.conditionId" property="conditionId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.maxChar" property="maxChar"/>

<bean:define id="options" name="exerciseCreationForm" property="options" type="java.lang.String[]"/>
<% for(int i=0; i<options.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="options["+new Integer(i)+"]"%>'>
	<bean:define id="valueOptions" name="exerciseCreationForm" property='<%="options["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.options" name="exerciseCreationForm" property="options" value="<%=valueOptions.toString()%>"/>
</logic:notEmpty>
<% }%>
<bean:define id="correctOptions" name="exerciseCreationForm" property="correctOptions" type="java.lang.String[]"/>
<% for(int i=0; i<correctOptions.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i)+"]"%>'>
	<bean:define id="valueCorrectOptions" name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.correctOptions" name="exerciseCreationForm" property="correctOptions" value="<%=valueCorrectOptions.toString()%>"/>
</logic:notEmpty>
<% }%>

<bean:define id="shuffle" name="exerciseCreationForm" property="shuffle" type="java.lang.String[]"/>
<% for(int i=0; i<shuffle.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="shuffle["+new Integer(i)+"]"%>'>
	<bean:define id="valueShuffle" name="exerciseCreationForm" property='<%="shuffle["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shuffle" name="exerciseCreationForm" property="shuffle" value="<%=valueShuffle.toString()%>"/>
</logic:notEmpty>
<% }%>

<bean:define id="signal" name="exerciseCreationForm" property="signal" type="java.lang.Integer[]"/>
<% for(int i=0; i<signal.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="signal["+new Integer(i)+"]"%>'>
	<bean:define id="valueSignal" name="exerciseCreationForm" property='<%="signal["+new Integer(i)+"]"%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.signal" name="exerciseCreationForm" property="signal" value="<%=valueSignal.toString()%>"/>
</logic:notEmpty>


<% }%>

<logic:present name="subQuestion">
<bean:define id="subQuestion" name="subQuestion"/>
<table>
	<tr><td
	<logic:iterate id="questionBody" name="subQuestion" property="presentation">
		<bean:define id="questionLabel" name="questionBody" property="label"/>
		<%if (((String)questionLabel).equals("flow")){%>
			</td></tr><tr><td>
		<% }else{%>
			<bean:write name="questionBody" property="value"/>
		<% } %>
	</logic:iterate>
	
	<bean:define id="cardinality" name="subQuestion" property="questionType.cardinalityType.type"/>
	<bean:define id="questionType" name="subQuestion" property="questionType.type"/>
	
	<logic:iterate id="questionOption" name="subQuestion" property="options" indexId="indexOption">
	<logic:iterate id="optionBody" name="questionOption" property="optionContent" indexId="optionBodyIndex">
		<bean:define id="optionLabel" name="optionBody" property="label"/>
		<% if (((String)optionLabel).equals("response_label")){ %>
			<%	if(((Integer)questionType).intValue()==1 ){ %> <%-- QuestionType.LID--%>
				<logic:equal name="optionBodyIndex" value="0">
					</td></tr></table><table><tr><td>
				</logic:equal>
				<%	if(((Integer)cardinality).intValue()==1 ){ %> <%-- Cardinality.SINGLE--%>
					</td></tr><tr>
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.response" property="response" value="<%= new Integer(indexOption.intValue()+1).toString() %>"/></td>
					<td>
				<% }else if(((Integer)cardinality).intValue()==2){ %> <%-- Cardinality.MULTIPLE--%>
					</td></tr><tr>
					<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.response" property="response" value="<%= new Integer(indexOption.intValue()+1).toString()  %>"></html:multibox></td>
					<td>
				<%}%>
			<%}else{ %><%-- QuestionType.STR or QuestionType.NUM--%>
				<logic:notEmpty name="subQuestion" property="questionType.render.maxchars">
					<bean:define id="maxchars" name="subQuestion" property="questionType.render.maxchars"/>
					<logic:notEmpty name="subQuestion" property="questionType.render.columns">
						<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.response" maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" property="response" value=""/>
					</logic:notEmpty>
					<logic:empty name="subQuestion" property="questionType.render.columns">
						<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
						<logic:greaterThan name="textBoxSize" value="100" >
						<bean:define id="textBoxSize" value="100"/>
						</logic:greaterThan>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.response" maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" property="response" value=""/>
					</logic:empty>
				</logic:notEmpty>	
				<logic:empty name="subQuestion" property="questionType.render.maxchars">
					<logic:notEmpty name="subQuestion" property="questionType.render.rows">
						<bean:define id="rows" name="subQuestion" property="questionType.render.rows"/>
						<logic:notEmpty name="subQuestion" property="questionType.render.columns">
							<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
							<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.response" rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" property="response"/>
						</logic:notEmpty>
						<logic:empty name="subQuestion" property="questionType.render.columns">
							<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.response" rows="<%=rows.toString()%>" property="response"/>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="subQuestion" property="questionType.render.rows">
						<logic:notEmpty name="subQuestion" property="questionType.render.columns">
							<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
							<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.response" cols="<%=cols.toString()%>" property="response"/>
						</logic:notEmpty>
						<logic:empty name="subQuestion" property="questionType.render.columns">
							<html:text bundle="HTMLALT_RESOURCES" altKey="text.response" property="response" value=""/>
						</logic:empty>
					</logic:empty>
				</logic:empty>
			<%}%>
		<% }else if (((String)optionLabel).equals("flow")){%>
			</td></tr><tr><td>
		<% } else {%>
			<bean:write name="optionBody" property="value"/>
		<% } %>
	</logic:iterate>
	</logic:iterate>
</td></tr></table>

</logic:present>	
<br/><br/><br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>