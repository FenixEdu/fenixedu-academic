<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:present name="infoStudentTestQuestionList">
<center>
<logic:empty name="infoStudentTestQuestionList">
	<h2><bean:message key="message.test.no.available"/></h2>
</logic:empty>
	
<logic:notEmpty name="infoStudentTestQuestionList" >
	
	<logic:present name="successfulChanged">
		<span class="error"><bean:message key="message.successfulChanged"/></span>
		<br/>
		<table>
		<logic:iterate id="changed" name="successfulChanged">
		<tr><td><bean:write name="changed" property="label"/></td>
		<td><bean:write name="changed" property="value"/></td></tr>
		</logic:iterate>
		</table>
	</logic:present>

	<html:form action="/studentTestManagement">
	<html:hidden property="method" value="showTestMarks"/>

	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion"/>
	<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="DataBeans.InfoDistributedTest"/>
	<bean:define id="testCode" name="distributedTest" property="idInternal"/>
	<bean:define id="student" name="testQuestion" property="student" type="DataBeans.InfoStudent"/>
	<bean:define id="person" name="student" property="infoPerson" type="DataBeans.InfoPerson"/>
	<bean:define id="studentCode" name="person" property="username"/>
	<bean:define id="studentId" name="student" property="idInternal"/>
	
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
	<bean:define id="distributedTestCode" name="distributedTest" property="idInternal"/>
	<html:hidden property="distributedTestCode" value="<%=distributedTestCode.toString() %>"/>
	<table>
	<tr><td class="infoop"><bean:message key="message.showStudentTest.information" /></td></tr>
	</table>
	<br/>
		<h2><bean:write name="distributedTest" property="title"/></h2>
		<b><bean:write name="distributedTest" property="testInformation"/></b>	
	</center>
	<br/>
	<br/>
	<bean:define id="testType" name="distributedTest" property="testType.type"/>
	<%if(((Integer)testType).intValue()!=3){%>
	<b><bean:message key="label.test.totalClassification"/>:</b>&nbsp;<bean:write name="classification"/>
	<%}%>
	<br/>
	<br/>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="10">
		<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="DataBeans.InfoStudentTestQuestion">
			<tr><td><hr></td></tr>
			<bean:define id="question" name="testQuestion" property="question" type="DataBeans.InfoQuestion"/>
			<bean:define id="questionCode" name="question" property="idInternal"/>
			<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
			<bean:define id="formula" name="testQuestion" property="correctionFormula.formula"/>
			<tr><td><div class="gen-button">
				<html:link page="<%= "/testsManagement.do?method=prepareChangeStudentTestQuestion&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;questionCode=" + pageContext.findAttribute("questionCode") +"&amp;distributedTestCode=" + pageContext.findAttribute("distributedTestCode") +"&amp;studentCode=" + pageContext.findAttribute("studentId")%>">
					<bean:message key="label.change" />
				</html:link>
			</div></td></tr>
			<tr>
				<td><b><bean:message key="message.tests.question" /></b>&nbsp;<bean:write name="questionOrder"/></td>
			</tr>
			<%if(((Integer)testType).intValue()!=3){%>
			<tr>
			<bean:define id="testQuestionValue" name="testQuestion" property="testQuestionValue"/>
			<bean:define id="testQuestionValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(testQuestionValue.toString())).toString()) %>"/>
				<td><b><bean:message key="message.tests.questionValue" /></b>&nbsp;<bean:write name="testQuestionValue"/></td>
			</tr>
			<bean:define id="mark" name="testQuestion" property="testQuestionMark"/>
			<tr>
				<bean:define id="value" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(mark.toString())).toString()) %>"/>
				<td><b><bean:message key="label.student.classification" /></b>&nbsp;<bean:write name="value"/></td>	
			</tr>
			<%}%>
			<tr>
			<td>
				<bean:define id="index" value="0"/>
				<bean:define id="imageLabel" value="false"/>
				
				<logic:iterate id="questionBody" name="question" property="question">
					<bean:define id="questionLabel" name="questionBody" property="label"/>
					<% if (((String)questionLabel).startsWith("image/")){%>
						<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
						<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode+"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>"/>
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
			<bean:define id="imageLabel" value="false"/>
			<bean:define id="cardinality" name="question" property="questionType.cardinalityType.type"/>
			<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
			<bean:define id="indexOption" value="0"/>
			<bean:define id="correct" value="false"/>
			<bean:define id="questionType" name="question" property="questionType.type"/>
			<logic:iterate id="optionBody" name="question" property="options">
				<bean:define id="optionLabel" name="optionBody" property="label"/>
				<% if (((String)optionLabel).startsWith("text")){ %>
					<bean:write name="optionBody" property="value"/>
				<%}else if (((String)optionLabel).startsWith("image/")){ %>
					<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
					<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>"/>
					<logic:equal name="imageLabel" value="true">
						</td><td>
					</logic:equal>
				<%} else if (((String)optionLabel).equals("image_label")){%>
					<logic:equal name="imageLabel" value="false">
						<bean:define id="imageLabel" value="true"/>
							<table><tr><td>
					</logic:equal>
					<bean:write name="optionBody" property="value"/>
					<br/>
				<%} else if (((String)optionLabel).equals("response_label")){ %>
					
					<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
						<logic:equal name="indexOption" value="0">
							<table><tr><td>
						</logic:equal>
						<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
						<bean:define id="button" value="true"/>
						<%if(((Integer)testType).intValue()!=3 && ((Integer)formula).intValue()==1){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.FENIX--%>
							<bean:define id="isResponsed" value="false"/>
							<logic:notEmpty name="testQuestion" property="response.response">
								<logic:iterate id="r" name="testQuestion" property="response.response" indexId="responseIndex">
									<logic:equal name="r" value="<%= (new Integer(Integer.parseInt(indexOption)-1)).toString()%>">
										<logic:notEqual name="indexOption" value="1">
											<logic:notEmpty name="testQuestion" property="response.isCorrect">
												<logic:notEmpty name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>'>
													<logic:equal name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
														</td><td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="" />
													</logic:equal>
													<logic:notEqual name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
														</td><td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="" />
													</logic:notEqual>
												</logic:notEmpty>
											</logic:notEmpty>
										</logic:notEqual>
									</logic:equal>
									<logic:equal name="r" value="<%= (new Integer(Integer.parseInt(indexOption))).toString()%>">
										<bean:define id="isResponsed" value="true"/>
									</logic:equal>
								</logic:iterate>								
							</logic:notEmpty>
							</td></tr><tr><td>
							
							<logic:notEmpty name="question" property="responseProcessingInstructions">
								<logic:iterate id="rp" name="question" property="responseProcessingInstructions">
									<logic:equal name="rp" property="fenixCorrectResponse" value="true">
										<logic:notEmpty name="rp" property="responseConditions">
											<logic:iterate id="rc" name="rp" property="responseConditions">
												<logic:notEmpty name="rc">
													<logic:equal name="rc" property="response" value="<%= (new Integer(Integer.parseInt(indexOption))).toString()%>">
														<logic:equal name="rc" property="condition" value="<%=(new Integer(1)).toString()%>">
															<%if(((Integer)cardinality).intValue()==1 ){ %>  <%--CardinalityType.SINGLE--%>
																<logic:equal name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/radioButtonSelected.gif" alt="" />
																</logic:equal>
																<logic:notEqual name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/radioButtonUnselected.gif" alt="" />
																</logic:notEqual>
															<%}else if(((Integer)cardinality).intValue()==2 ){ %>  <%--CardinalityType.MULTIPLE--%>
																<logic:equal name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/checkButtonSelected.gif" alt="" />
																</logic:equal>
																<logic:notEqual name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/checkButtonUnselected.gif" alt="" />
																</logic:notEqual>
															<%}%>
															<bean:define id="button" value="false"/>
														</logic:equal>
													</logic:equal>
												</logic:notEmpty>
											</logic:iterate>
										</logic:notEmpty>
									</logic:equal>
								</logic:iterate>
							</logic:notEmpty>
						<%}else{%>
							</td></tr><tr><td>
						<%}%>
							
						<logic:equal name="button" value="true">
							<%if(((Integer)cardinality).intValue()==1 ){ %>  <%--CardinalityType.SINGLE--%>
								<html:radio property='<%="question["+ optionOrder+"].response"%>' value="<%= indexOption.toString() %>" disabled="true"/>
							<%}else if(((Integer)cardinality).intValue()==2 ){ %>  <%--CardinalityType.MULTIPLE--%>
								<html:multibox property='<%="question["+ optionOrder+"].response"%>' value="<%= indexOption.toString()%>" disabled="true"></td>
								</html:multibox>
							<%}%>
						</logic:equal>
					<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
						<logic:notEmpty name="question" property="questionType.render.maxchars">
							<bean:define id="maxchars" name="question" property="questionType.render.maxchars"/>
							<logic:notEmpty name="question" property="questionType.render.columns">
								<bean:define id="cols" name="question" property="questionType.render.columns"/>
								<html:text maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
							</logic:notEmpty>
							<logic:empty name="question" property="questionType.render.columns">
								<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
								<logic:greaterThan name="textBoxSize" value="100" >
									<bean:define id="textBoxSize" value="100"/>
								</logic:greaterThan>
								<html:text maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
							</logic:empty>
						</logic:notEmpty>	
						<logic:empty name="question" property="questionType.render.maxchars">
							<logic:notEmpty name="question" property="questionType.render.rows">
								<bean:define id="rows" name="question" property="questionType.render.rows"/>
									<logic:notEmpty name="question" property="questionType.render.columns">
										<bean:define id="cols" name="question" property="questionType.render.columns"/>
										<html:textarea rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
									</logic:notEmpty>
							<logic:empty name="question" property="questionType.render.columns">
								<html:textarea rows="<%=rows.toString()%>" property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
							</logic:empty>
						</logic:notEmpty>
						<logic:empty name="question" property="questionType.render.rows">
							<logic:notEmpty name="question" property="questionType.render.columns">
								<bean:define id="cols" name="question" property="questionType.render.columns"/>
								<html:textarea cols="<%=cols.toString()%>" property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
							</logic:notEmpty>
							<logic:empty name="question" property="questionType.render.columns">
								<html:text property='<%="question["+ optionOrder+"].response"%>' disabled="true"/>
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
				<%}else {%>
					<bean:write name="optionBody" property="value"/>
				<%}%>	
			</logic:iterate>
			<logic:equal name="imageLabel" value="true">
				</td></tr></table>
			</logic:equal>
			<%if((((Integer)testType).intValue()!=3) &&(((Integer)formula).intValue()==1)){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.FENIX--%>
				<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
					<logic:notEmpty name="testQuestion" property="response.response">
						<logic:notEqual name="indexOption" value="1">
							<logic:iterate id="r" name="testQuestion" property="response.response" indexId="responseIndex">
								<logic:equal name="r" value="<%= (new Integer(Integer.parseInt(indexOption))).toString()%>">
									<logic:notEmpty name="testQuestion" property="response.isCorrect">
										<logic:notEmpty name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>'>
											<logic:equal name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
												</td><td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="" />
											</logic:equal>
											<logic:notEqual name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
												</td><td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="" />
											</logic:notEqual>
										</logic:notEmpty>
									</logic:notEmpty>	
								</logic:equal>
							</logic:iterate>
						</logic:notEqual>
					</logic:notEmpty>
					</td></tr></table>
				<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
					<logic:notEmpty name="testQuestion" property="response.response">
						<logic:notEmpty name="testQuestion" property="response.isCorrect">
							<logic:equal name="testQuestion" property='<%="response.isCorrect"%>' value="true">
								<img src="<%= request.getContextPath() %>/images/correct.gif" alt="" />
							</logic:equal>
							<logic:notEqual name="testQuestion" property='<%="response.isCorrect"%>' value="true">
								<img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="" />
							</logic:notEqual>
						</logic:notEmpty>
					</logic:notEmpty>
					</td></tr><tr><td>
				<%}%>
			<%} else if((((Integer)testType).intValue()!=3) &&(((Integer)formula).intValue()!=1)){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.IMS--%>
				</td></tr>
				<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
					</table>
				<%}%>
				<tr><td>
				<logic:equal name="distributedTest" property="imsFeedback" value="true">
					<bean:define id="imageLabel" value="false"/>
					<logic:notEmpty name="testQuestion" property="response.response">
						<logic:notEmpty name="testQuestion" property="response.responseProcessingIndex">
							<logic:notEmpty name="question" property="responseProcessingInstructions">
								<bean:define id="responseProcessingIndex" name="testQuestion" property="response.responseProcessingIndex"/>
								<logic:notEmpty name="question" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
									<logic:iterate id="feedback" name="question" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
										<bean:define id="feedbackLabel" name="feedback" property="label"/>
										<%if (((String)feedbackLabel).startsWith("image/")){%>
											<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
											<html:img align="absmiddle" src="<%= request.getContextPath() + "/teacher/testsManagement.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;studentCode="+ studentCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+feedbackLabel.toString()%>"/>
											<logic:equal name="imageLabel" value="true">
												</td><td>
											</logic:equal>
										<%} else if (((String)feedbackLabel).equals("image_label")){%>		
											<logic:equal name="imageLabel" value="false">
												<bean:define id="imageLabel" value="true"/>
												<table><tr><td>
											</logic:equal>
											<bean:write name="feedback" property="value"/>
											<br/>
										<%}else if (((String)feedbackLabel).equals("flow")){%>
											<logic:equal name="imageLabel" value="true">
												</td></tr></table>
												<bean:define id="imageLabel" value="false"/>
											</logic:equal>
											</td></tr><tr><td>
										<%}else{%>
											<bean:write name="feedback" property="value"/>
										<%}%>
									</logic:iterate>
									<logic:equal name="imageLabel" value="true">
										</td></tr></table>
									</logic:equal>
								</logic:notEmpty>
							</logic:notEmpty>
						</logic:notEmpty>
					</logic:notEmpty>
				</logic:equal>
			<%}%>
			</td></tr>
		</logic:iterate>
		<tr><td><hr></td></tr>
	</table>
	<br/>
	<br/>
	<table align="center">
	<tr>
		<td><html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</tr>
	</table>
	</html:form>
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="infoStudentTestQuestionList">
<center>
	<h2><bean:message key="message.test.no.available"/></h2>
</center>
</logic:notPresent>