<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="infoStudentTestQuestionList">
	<center>
	<logic:empty name="infoStudentTestQuestionList">
		<h2><bean:message key="message.studentTest.no.available"/></h2>
	</logic:empty>
	
	<logic:notEmpty name="infoStudentTestQuestionList" >
	<html:form action="/studentTests">
	<html:hidden property="method" value="viewDoneTests"/>

	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="DataBeans.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
		
	<bean:define id="objectCode" name="distributedTest" property="infoExecutionCourse.idInternal"/>
	<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>
	<html:hidden property="testCode" value="<%= testCode.toString() %>"/>
	
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>
	</center>
	<br/>
	<br/>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<br/>
	<br/>
	<table width="100%" border="0" cellpadding="0" cellspacing="10">
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion">
			<tr><td><hr></td></tr>
			<bean:define id="question" name="testQuestion" property="question" type="DataBeans.InfoQuestion"/>
			<bean:define id="testQuestionValue" name="testQuestion" property="testQuestionValue"/>
			<bean:define id="questionCode" name="question" property="idInternal"/>
			<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
			<tr>
				<td><b><bean:message key="label.test.questionOrder" />:</b>&nbsp;<bean:write name="questionOrder"/></td>
			</tr>
			<tr>
				<td><b><bean:message key="label.test.questionValue" />:</b>&nbsp;<bean:write name="testQuestionValue"/></td>
			</tr>
			
			
			<bean:define id="mark" name="testQuestion" property="testQuestionMark"/>

			<tr>
				<bean:define id="value" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(mark.toString())).toString()) %>"/>
				<td><b><bean:message key="label.test.classification" />:</b>&nbsp;<bean:write name="value"/></td>	
			</tr>
			<tr>
				<td>
					<bean:define id="index" value="0"/>
				<bean:define id="imageLabel" value="false"/>
				<logic:iterate id="questionBody" name="question" property="question">
				<bean:define id="questionLabel" name="questionBody" property="label"/>
				
				<% if (((String)questionLabel).startsWith("image/")){%>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciceCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>"/>
					
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
				</td>
			</tr><tr>
				<td>
					<bean:define id="cardinality" name="question" property="questionCardinality"/>
			<table><td>
<%--				<bean:define id="index" value="0"/> --%>
				<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
				<bean:define id="indexOption" value="0"/>
				<bean:define id="correct" value="false"/>
				<logic:iterate id="optionBody" name="question" property="options">
					<bean:define id="optionLabel" name="optionBody" property="label"/>
					<% if (((String)optionLabel).startsWith("image/")){ %>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciceCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>"/>
					<% } else if (((String)optionLabel).equals("image_label")){%>
						<bean:write name="optionBody" property="value"/>
						<br/>
					<% } else if (((String)optionLabel).equals("response_label")){ %>				
						<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
						<%	if(cardinality.equals("Single")){ %>
							</td>
							<bean:define id="correction" name="distributedTest" property="correctionAvailability" type="Util.CorrectionAvailability"/>
							
							<bean:define id="responsed" name="testQuestion" property="response"/>
							
							<bean:define id="button" value="true"/>
							<logic:notEqual name="correction" property="availability" value="1">
								<logic:notEqual name="indexOption" value="1">
									<logic:iterate id="correctResponse" name="question" property="correctResponse">
										<logic:equal name="correctResponse" value="<%= (new Integer(Integer.parseInt(indexOption)-1)).toString() %>">
												<logic:equal name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption)-1)).toString() %>">
													<td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="" /></td>
													<bean:define id="correct" value="true"/>
												</logic:equal>
										</logic:equal>
									</logic:iterate>
									<logic:equal name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption)-1)).toString() %>">
										<logic:equal name="correct" value="false">
											<td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="" /></td>
											<bean:define id="correct" value="true"/>
										</logic:equal>
									</logic:equal>
								</logic:notEqual>
							
							
							</tr><tr><td>
								
								<logic:iterate id="correctResponse" name="question" property="correctResponse">
										<logic:equal name="correctResponse" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
											<logic:equal name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
													<img src="<%= request.getContextPath() %>/images/checkSelected.gif" alt="" />
													<bean:define id="button" value="false"/>
												</logic:equal>
												<logic:notEqual name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
													<img src="<%= request.getContextPath() %>/images/checkUnselected.gif" alt="" />
													<bean:define id="button" value="false"/>
												</logic:notEqual>
										</logic:equal>
								</logic:iterate>
								</logic:notEqual>
								<logic:equal name="button" value="true">
									</tr><tr><td>
									<html:radio property='<%="option["+ optionOrder+"]"%>' value="<%= indexOption.toString() %>" disabled="true"/>
									<bean:define id="button" value="true"/>
								</logic:equal>
							</td><td>
						<% }else if(cardinality.equals("Multiple")){ %>
							</td></tr><tr><td>
								<html:multibox property='<%="option["+ optionOrder+"]"%>' disabled="true">
								<bean:write name="indexOption"/> 
								</html:multibox>
							</td><td>
						<%}%>
					<% } else {%>
					<bean:write name="optionBody" property="value"/>
					<% } %>
						
				</logic:iterate>
				<logic:iterate id="correctResponse" name="question" property="correctResponse">
					<logic:equal name="correctResponse" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
						<logic:equal name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
							<td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="" /></td>
							<bean:define id="correct" value="true"/>
						</logic:equal>
					</logic:equal>
					<logic:equal name="responsed" value="<%= (new Integer(Integer.parseInt(indexOption))).toString() %>">
						<logic:equal name="correct" value="false">
							<td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="" /></td>
							<bean:define id="correct" value="true"/>
						</logic:equal>
					</logic:equal>
				</logic:iterate>
				</td></tr></table></td>	
			</tr>
		</logic:iterate>
		<tr><td><hr></td></tr>
	</table>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="button.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.studentTest.no.available"/></h2>
</center>
</logic:notPresent>