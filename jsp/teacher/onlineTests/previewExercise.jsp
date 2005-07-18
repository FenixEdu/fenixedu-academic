<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/exerciseCreation">
<html:hidden property="page" value="2"/>
<html:hidden property="method" value="prepareCreateExercise"/>

<html:hidden property="objectCode"/>
<html:hidden property="exerciseCode"/>
<html:hidden property="evaluationQuestion"/>
<html:hidden property="author"/>
<html:hidden property="description"/>
<html:hidden property="difficulty"/>
<html:hidden property="mainSubject"/>
<html:hidden property="secondarySubject"/>
<html:hidden property="learningHour"/>
<html:hidden property="learningMinute"/>
<html:hidden property="level"/>
<html:hidden property="questionType"/>
<html:hidden property="cardinalityType"/>
<html:hidden property="optionNumber"/>
<html:hidden property="questionValue"/>
<html:hidden property="questionText"/>
<html:hidden property="secondQuestionText"/>
<html:hidden property="breakLineBeforeResponseBox"/>
<html:hidden property="breakLineAfterResponseBox"/>
<html:hidden property="correctFeedbackText"/>
<html:hidden property="wrongFeedbackText"/>
<html:hidden property="cols"/>
<html:hidden property="rows"/>
<html:hidden property="maxChar"/>
<html:hidden property="caseSensitive"/>
<html:hidden property="integerType"/>
<html:hidden property="conditionId"/>
<html:hidden property="maxChar"/>

<bean:define id="options" name="exerciseCreationForm" property="options" type="java.lang.String[]"/>
<% for(int i=0; i<options.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="options["+new Integer(i)+"]"%>'>
	<bean:define id="valueOptions" name="exerciseCreationForm" property='<%="options["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden name="exerciseCreationForm" property="options" value="<%=valueOptions.toString()%>"/>
</logic:notEmpty>
<% }%>
<bean:define id="correctOptions" name="exerciseCreationForm" property="correctOptions" type="java.lang.String[]"/>
<% for(int i=0; i<correctOptions.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i)+"]"%>'>
	<bean:define id="valueCorrectOptions" name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden name="exerciseCreationForm" property="correctOptions" value="<%=valueCorrectOptions.toString()%>"/>
</logic:notEmpty>
<% }%>

<bean:define id="shuffle" name="exerciseCreationForm" property="shuffle" type="java.lang.String[]"/>
<% for(int i=0; i<shuffle.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="shuffle["+new Integer(i)+"]"%>'>
	<bean:define id="valueShuffle" name="exerciseCreationForm" property='<%="shuffle["+new Integer(i)+"]"%>' type="java.lang.String"/>
	<html:hidden name="exerciseCreationForm" property="shuffle" value="<%=valueShuffle.toString()%>"/>
</logic:notEmpty>
<% }%>

<bean:define id="signal" name="exerciseCreationForm" property="signal" type="java.lang.Integer[]"/>
<% for(int i=0; i<signal.length ;i++){%>
<logic:notEmpty name="exerciseCreationForm" property='<%="signal["+new Integer(i)+"]"%>'>
	<bean:define id="valueSignal" name="exerciseCreationForm" property='<%="signal["+new Integer(i)+"]"%>'/>
	<html:hidden name="exerciseCreationForm" property="signal" value="<%=valueSignal.toString()%>"/>
</logic:notEmpty>


<% }%>

<logic:present name="infoQuestion">
<bean:define id="infoQuestion" name="infoQuestion"/>
<table>
	<tr><td>
	<logic:iterate id="questionBody" name="infoQuestion" property="question">
		<bean:define id="questionLabel" name="questionBody" property="label"/>
		<%if (((String)questionLabel).equals("flow")){%>
			</td></tr><tr><td>
		<% }else{%>
			<bean:write name="questionBody" property="value"/>
		<% } %>
	</logic:iterate>
	
	<bean:define id="cardinality" name="infoQuestion" property="questionType.cardinalityType.type"/>
	<bean:define id="questionType" name="infoQuestion" property="questionType.type"/>
	
	<logic:iterate id="questionOption" name="infoQuestion" property="options" indexId="indexOption">
	<logic:iterate id="optionBody" name="questionOption" property="optionContent" indexId="optionBodyIndex">
		<bean:define id="optionLabel" name="optionBody" property="label"/>
		<% if (((String)optionLabel).equals("response_label")){ %>
			<%	if(((Integer)questionType).intValue()==1 ){ %> <%-- QuestionType.LID--%>
				<logic:equal name="optionBodyIndex" value="0">
					</td></tr></table><table><tr><td>
				</logic:equal>
				<%	if(((Integer)cardinality).intValue()==1 ){ %> <%-- Cardinality.SINGLE--%>
					</td></tr><tr>
					<td><html:radio property="response" value="<%= new Integer(indexOption.intValue()+1).toString() %>"/></td>
					<td>
				<% }else if(((Integer)cardinality).intValue()==2){ %> <%-- Cardinality.MULTIPLE--%>
					</td></tr><tr>
					<td><html:multibox property="response" value="<%= new Integer(indexOption.intValue()+1).toString()  %>"></html:multibox></td>
					<td>
				<%}%>
			<%}else{ %><%-- QuestionType.STR or QuestionType.NUM--%>
				<logic:notEmpty name="infoQuestion" property="questionType.render.maxchars">
					<bean:define id="maxchars" name="infoQuestion" property="questionType.render.maxchars"/>
					<logic:notEmpty name="infoQuestion" property="questionType.render.columns">
						<bean:define id="cols" name="infoQuestion" property="questionType.render.columns"/>
						<html:text maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" property="response" value=""/>
					</logic:notEmpty>
					<logic:empty name="infoQuestion" property="questionType.render.columns">
						<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
						<logic:greaterThan name="textBoxSize" value="100" >
						<bean:define id="textBoxSize" value="100"/>
						</logic:greaterThan>
						<html:text maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" property="response" value=""/>
					</logic:empty>
				</logic:notEmpty>	
				<logic:empty name="infoQuestion" property="questionType.render.maxchars">
					<logic:notEmpty name="infoQuestion" property="questionType.render.rows">
						<bean:define id="rows" name="infoQuestion" property="questionType.render.rows"/>
						<logic:notEmpty name="infoQuestion" property="questionType.render.columns">
							<bean:define id="cols" name="infoQuestion" property="questionType.render.columns"/>
							<html:textarea rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" property="response"/>
						</logic:notEmpty>
						<logic:empty name="infoQuestion" property="questionType.render.columns">
							<html:textarea rows="<%=rows.toString()%>" property="response"/>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="infoQuestion" property="questionType.render.rows">
						<logic:notEmpty name="infoQuestion" property="questionType.render.columns">
							<bean:define id="cols" name="infoQuestion" property="questionType.render.columns"/>
							<html:textarea cols="<%=cols.toString()%>" property="response"/>
						</logic:notEmpty>
						<logic:empty name="infoQuestion" property="questionType.render.columns">
							<html:text property="response" value=""/>
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
<html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
</html:form>