<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="studentTestQuestionList">
<logic:notEmpty name="studentTestQuestionList" >

<bean:define id="testCode" value='<%=request.getParameter("testCode")%>'/>
<bean:define id="pageType" value='<%=request.getParameter("pageType")%>'/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="10">
<bean:define id="order" value="1"/>
	<logic:iterate id="testQuestion" name="studentTestQuestionList" type="net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion">
		<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.domain.onlineTests.Question"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.domain.onlineTests.DistributedTest"/>
		<bean:define id="testScope" name="distributedTest" property="testScope"/>
		<bean:define id="domainObject" name="testScope" property="domainObject"/>
		<bean:define id="objectCode" name="domainObject" property="idInternal"/>
		<bean:define id="questionCode" name="question" property="idInternal"/>
		<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
		<bean:define id="correction" name="testQuestion" property="distributedTest.correctionAvailability" type="net.sourceforge.fenixedu.util.tests.CorrectionAvailability"/>
		<bean:define id="formula" name="testQuestion" property="correctionFormula.formula"/>
		<bean:define id="testType" name="testQuestion" property="distributedTest.testType.type"/>

	<logic:iterate id="subQuestion" name="testQuestion" property="studentSubQuestions" type="net.sourceforge.fenixedu.domain.onlineTests.SubQuestion" indexId="itemIndex">
	<bean:define id="item" value="<%=itemIndex.toString()%>"/>
	<%if(testQuestion.getStudentSubQuestions().size()<=1 || (testQuestion.getItemId()!=null && subQuestion.getItemId()!=null && testQuestion.getItemId().equals(subQuestion.getItemId()))){%>
	
	<%if(testQuestion.getStudentSubQuestions().size()<=1 || itemIndex.equals(new Integer(0))){%>
		<tr><td><hr></td></tr>
		<tr><td><b><bean:message key="label.test.questionOrder" />:</b>&nbsp;<bean:write name="order"/></td></tr>
		<bean:define id="order" value="<%= (new Integer(Integer.parseInt(order)+1)).toString() %>"/>
	<%}
	 if(testQuestion.getStudentSubQuestions().size()>1){
	 	if(itemIndex.equals(new Integer(0))){%>
	 		<tr><td><span class="error">Esta pergunta é uma pergunta com alíneas. Após responder poderá surgir uma nova alínea para responder.</span></td></tr>
	 	<%}
	 }%>
	 <tr><td>
		<bean:define id="index" value="0"/>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionPresentation" name="subQuestion" property="prePresentation">
			<bean:define id="questionLabel" name="questionPresentation" property="label"/>	
			<%if (((String)questionLabel).startsWith("image/")){%>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>"/>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%} else if (((String)questionLabel).equals("image_label")){%>				
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="questionPresentation" property="value"/>
				<br/>
			<%}else if (((String)questionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else{%>
				<bean:write name="questionPresentation" property="value"/>
			<%}%>
		</logic:iterate>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
	<%if(testQuestion.getStudentSubQuestions().size()>1){%>	
		<tr><td><br/><b><bean:write name="subQuestion" property="title"/></b></td></tr>
	<%}%>
		
	<bean:define id="questionType" name="subQuestion" property="questionType.type"/>

	<bean:define id="mark" name="testQuestion" property="testQuestionMark"/>
		
		<%if(((Integer)testType).intValue()!=3){%>
			<bean:define id="testQuestionValue" name="testQuestion" property="testQuestionValue"/>
			<bean:define id="testQuestionValue" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(testQuestionValue.toString())).toString()) %>"/>
			<tr><td><b><bean:message key="label.test.questionValue" />:</b>&nbsp;<bean:write name="testQuestionValue"/></td></tr>
			<logic:equal name="pageType" value="correction">		
				<bean:define id="mark" name="testQuestion" property="testQuestionMark"/>
				<bean:define id="value" value="<%= (new java.text.DecimalFormat("#0.##").format(Double.parseDouble(mark.toString())).toString()) %>"/>
				<tr><td><b><bean:message key="label.test.classification" />:</b>&nbsp;<bean:write name="value"/></td></tr>
			</logic:equal>
		<%}%>
		
		
		<tr><td>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionBody" name="subQuestion" property="presentation">
			<bean:define id="questionLabel" name="questionBody" property="label"/>	
			<%if (((String)questionLabel).startsWith("image/")){%>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()+"&amp;item="+item.toString()%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%} else if (((String)questionLabel).equals("image_label")){%>				
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="questionBody" property="value"/>
				<br/>
			<%}else if (((String)questionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else{%>
				<bean:write name="questionBody" property="value"/>
			<%}%>
		</logic:iterate>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		
		<bean:define id="cardinality" name="subQuestion" property="questionType.cardinalityType.type"/>
		<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
		<bean:define id="indexOption" value="0"/>
		<bean:define id="correct" value="false"/>
		<logic:iterate id="questionOption" name="subQuestion" property="options">
		<logic:iterate id="optionBody" name="questionOption" property="optionContent">
			<bean:define id="optionLabel" name="optionBody" property="label"/>
			<%if (((String)optionLabel).startsWith("image/")){ %>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()+"&amp;item="+item.toString()%>" altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
				<logic:equal name="imageLabel" value="true">
					</td><td>
				</logic:equal>
			<%}else if (((String)optionLabel).equals("image_label")){%>
				<logic:equal name="imageLabel" value="false">
					<bean:define id="imageLabel" value="true"/>
					<table><tr><td>
				</logic:equal>
				<bean:write name="optionBody" property="value"/>
				<br/>
			<%}else if (((String)optionLabel).equals("response_label")){ %>
				<bean:define id="checkDisable" value="false"/>
				<%if(((Integer)testType).intValue()==1){%>
					<logic:equal name="pageType" value="doTest">
					<logic:notEmpty name="testQuestion" property="response">
					<logic:equal name="testQuestion" property="response.responsed" value="true">
						<bean:define id="checkDisable" value="true"/>	
						</logic:equal>
					</logic:notEmpty>
					</logic:equal>
					<logic:notEqual name="pageType" value="doTest">
						<bean:define id="checkDisable" value="true"/>
					</logic:notEqual>
				<%}%>
				
				<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
					<logic:equal name="indexOption" value="0">
						<table><tr><td>
					</logic:equal>
					<bean:define id="indexOption" value="<%= (new Integer(Integer.parseInt(indexOption)+1)).toString() %>"/>
					<bean:define id="button" value="true"/>
					
					<logic:equal name="pageType" value="correction">
						<logic:notEqual name="correction" property="availability" value="1">
							<%if(((Integer)testType).intValue()!=3 && ((Integer)formula).intValue()==1){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.FENIX--%>
							<bean:define id="isResponsed" value="false"/>
							<logic:notEmpty name="testQuestion" property="response">
							<logic:notEmpty name="testQuestion" property="response.response">
								<logic:iterate id="r" name="testQuestion" property="response.response" indexId="responseIndex">
									<logic:equal name="r" value="<%= (new Integer(Integer.parseInt(indexOption)-1)).toString()%>">
										<logic:notEqual name="indexOption" value="1">
										<logic:notEmpty name="testQuestion" property="response.isCorrect">
											<logic:notEmpty name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>'>
												<logic:equal name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
													</td><td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="<bean:message key="correct" bundle="IMAGE_RESOURCES" />" />
												</logic:equal>
												<logic:notEqual name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
													</td><td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="<bean:message key="incorrect" bundle="IMAGE_RESOURCES" />" />
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
							</logic:notEmpty>
							</td></tr><tr><td>
							<logic:equal name="pageType" value="correction">
								<logic:notEmpty name="subQuestion" property="responseProcessingInstructions">
									<logic:iterate id="rp" name="subQuestion" property="responseProcessingInstructions">
										<logic:equal name="rp" property="fenixCorrectResponse" value="true">
											<logic:notEmpty name="rp" property="responseConditions">
												<logic:iterate id="rc" name="rp" property="responseConditions">
													<logic:equal name="rc" property="response" value="<%= (new Integer(Integer.parseInt(indexOption))).toString()%>">
														<logic:equal name="rc" property="condition" value="<%=(new Integer(1)).toString()%>">
															<%if(((Integer)cardinality).intValue()==1 ){ %>  <%--CardinalityType.SINGLE--%>
																<logic:equal name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/radioButtonSelected.gif" alt="<bean:message key="radioButtonSelected" bundle="IMAGE_RESOURCES" />" />
																</logic:equal>
																<logic:notEqual name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/radioButtonUnselected.gif" alt="<bean:message key="radioButtonUnselected" bundle="IMAGE_RESOURCES" />" />
																</logic:notEqual>
															<%}else if(((Integer)cardinality).intValue()==2 ){ %>  <%--CardinalityType.MULTIPLE--%>
																<logic:equal name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/checkButtonSelected.gif" alt="<bean:message key="checkButtonSelected" bundle="IMAGE_RESOURCES" />"" />
																</logic:equal>
																<logic:notEqual name="isResponsed" value="true">
																	<img src="<%= request.getContextPath() %>/images/checkButtonUnselected.gif" alt="<bean:message key="checkButtonUnselected" bundle="IMAGE_RESOURCES" />" />
																</logic:notEqual>
															<%}%>
															<bean:define id="button" value="false"/>
														</logic:equal>
													</logic:equal>
												</logic:iterate>
											</logic:notEmpty>
										</logic:equal>
									</logic:iterate>
								</logic:notEmpty>
							</logic:equal>
							<%}else{%>
							</td></tr><tr><td>
							<%}%>
						</logic:notEqual>
					</logic:equal>
					<logic:equal name="pageType" value="doTest">
					</td></tr><tr><td>
					</logic:equal>
					<logic:equal name="button" value="true">
						<bean:define id="questionValue" value='<%="question"+ optionOrder%>'/>
						<bean:define id="responsed" value="false"/>
						<%if(((Integer)cardinality).intValue()==1 ){ %>  <%--CardinalityType.SINGLE--%>
						<logic:equal name="<%=questionValue%>" value="<%= indexOption.toString()%>">
							<bean:define id="responsed" value="true"/>
						</logic:equal>
							</tr></td><tr><td>
							<logic:equal name="checkDisable" value="true">
									<logic:equal name="responsed" value="true">
										<input alt="<%=questionValue%>" type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" disabled="<%=new Boolean(checkDisable).booleanValue()%>" checked/>
									</logic:equal>
									<logic:equal name="responsed" value="false">
										<input alt="<%=questionValue%>" type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
									</logic:equal>
							</logic:equal>
							<logic:equal name="checkDisable" value="false">
									<logic:equal name="responsed" value="true">
										<input alt="<%=questionValue%>" type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" checked/>
									</logic:equal>
									<logic:equal name="responsed" value="false">
										<input alt="<%=questionValue%>" type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>"/>
									</logic:equal>
							</logic:equal>
						<%}else if(((Integer)cardinality).intValue()==2 ){ %>  <%--CardinalityType.MULTIPLE--%>
					
						<%
						Object object = request.getAttribute(questionValue);
					
						if(object!=null && object!=""){
							String[] a = (String[])object;
							for(int i=0; i<a.length; i++){
								if(a[i]!=null && a[i].equals(indexOption.toString())){%>
								<bean:define id="responsed" value="true"/>
							<%  }
							}
						}
						%>
							</tr></td><tr><td>
							<logic:equal name="checkDisable" value="true">
								<logic:equal name="responsed" value="true">
									<input alt="<%=questionValue%>" type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>" checked>
								</logic:equal>
								<logic:equal name="responsed" value="false">
									<input alt="<%=questionValue%>" type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>">
								</logic:equal>
							</logic:equal>
							<logic:equal name="checkDisable" value="false">
								<logic:equal name="responsed" value="true">
									<input alt="<%=questionValue%>" type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" checked>
								</logic:equal>
								<logic:equal name="responsed" value="false">
									<input alt="<%=questionValue%>" type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>">
								</logic:equal>
							</logic:equal>
						<%}%>
					</logic:equal>
					
				<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
					<bean:define id="questionValue" name='<%="question"+ optionOrder%>'/>
					<logic:notEmpty name="subQuestion" property="questionType.render.maxchars">
						<bean:define id="maxchars" name="subQuestion" property="questionType.render.maxchars"/>
						<logic:notEmpty name="subQuestion" property="questionType.render.columns">
							<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
							<html:text alt="<%="question"+ optionOrder%>" maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
						</logic:notEmpty>
						<logic:empty name="subQuestion" property="questionType.render.columns">
							<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
							<logic:greaterThan name="textBoxSize" value="100" >
								<bean:define id="textBoxSize" value="100"/>
							</logic:greaterThan>
							<html:text alt="<%="question"+ optionOrder%>" maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
						</logic:empty>	
					</logic:notEmpty>	
					<logic:empty name="subQuestion" property="questionType.render.maxchars">
						<logic:notEmpty name="subQuestion" property="questionType.render.rows">
							<bean:define id="rows" name="subQuestion" property="questionType.render.rows"/>
							<logic:notEmpty name="subQuestion" property="questionType.render.columns">
								<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
								<html:textarea alt="<%="question"+ optionOrder%>" rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:notEmpty>
							<logic:empty name="subQuestion" property="questionType.render.columns">

								<html:textarea alt="<%="question"+ optionOrder%>" rows="<%=rows.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:empty>
						</logic:notEmpty>
						<logic:empty name="subQuestion" property="questionType.render.rows">
							<logic:notEmpty name="subQuestion" property="questionType.render.columns">
								<bean:define id="cols" name="subQuestion" property="questionType.render.columns"/>
								<html:textarea alt="<%="question"+ optionOrder%>" cols="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:notEmpty>
							<logic:empty name="subQuestion" property="questionType.render.columns">
								<html:text  alt='<%="question"+ optionOrder%>' property='<%="question"+ optionOrder%>' value="<%=questionValue.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:empty>
						</logic:empty>
					</logic:empty>
				<%}%>
			<%}else if (((String)optionLabel).equals("flow")){%>
				<logic:equal name="imageLabel" value="true">
					</td></tr></table>
					<bean:define id="imageLabel" value="false"/>
				</logic:equal>
				</td></tr><tr><td>
			<%}else {%>
				<bean:write name="optionBody" property="value"/>
			<%}%>
		</logic:iterate>
		</logic:iterate>
		<logic:equal name="pageType" value="doTest">
		<%if(testQuestion.getStudentSubQuestions().size()>1){%>
			<logic:empty name="testQuestion" property="response">
				<tr><td>
				<html:link page="<%= "/studentTests.do?method=prepareToGiveUp&amp;objectCode="+objectCode.toString()+"&amp;testCode="+testCode.toString()+ "&amp;exerciseCode="+ questionCode +"&amp;item="+item.toString()%>">
				<bean:message key="link.giveUp" />
				</html:link>
				</td></tr>
			</logic:empty>
			<logic:notEmpty name="testQuestion" property="response">
				<logic:equal name="testQuestion" property="response.responsed" value="false">
					<tr><td>
					<html:link page="<%= "/studentTests.do?method=prepareToGiveUp&amp;objectCode="+objectCode.toString()+"&amp;testCode="+testCode.toString()+ "&amp;exerciseCode="+ questionCode +"&amp;item="+item.toString()%>">
					<bean:message key="link.giveUp" />
					</html:link>
					</td></tr>
				</logic:equal>
			</logic:notEmpty>
		<%}%>			
		</logic:equal>
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		<%if((((Integer)testType).intValue()!=3) &&(((Integer)formula).intValue()==1)){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.FENIX--%>
			<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
				<logic:equal name="pageType" value="correction">
					<logic:notEqual name="correction" property="availability" value="1">
						<logic:notEmpty name="testQuestion" property="response">
						<logic:notEmpty name="testQuestion" property="response.response">
							<logic:notEqual name="indexOption" value="1">
								<logic:iterate id="r" name="testQuestion" property="response.response" indexId="responseIndex">
									<logic:equal name="r" value="<%= (new Integer(Integer.parseInt(indexOption))).toString()%>">
									<logic:notEmpty name="testQuestion" property="response.isCorrect">
										<logic:notEmpty name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>'>
											<logic:equal name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
												</td><td><img src="<%= request.getContextPath() %>/images/correct.gif" alt="<bean:message key="correct" bundle="IMAGE_RESOURCES" />" />
											</logic:equal>
											<logic:notEqual name="testQuestion" property='<%="response.isCorrect["+responseIndex+"]"%>' value="true">
												</td><td><img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="<bean:message key="incorrect" bundle="IMAGE_RESOURCES" />" />
											</logic:notEqual>
										</logic:notEmpty>
									</logic:notEmpty>
									</logic:equal>
								</logic:iterate>
							</logic:notEqual>
						</logic:notEmpty>
						</logic:notEmpty>
					</logic:notEqual>
				</logic:equal>
				</td></tr></table>
			<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
				<logic:equal name="pageType" value="correction">
					<logic:notEqual name="correction" property="availability" value="1">
						<logic:notEmpty name="testQuestion" property="response">
						<logic:notEmpty name="testQuestion" property="response.response">
							<logic:notEmpty name="testQuestion" property="response.isCorrect">
								<logic:equal name="testQuestion" property='<%="response.isCorrect"%>' value="true">
									<img src="<%= request.getContextPath() %>/images/correct.gif" alt="<bean:message key="correct" bundle="IMAGE_RESOURCES" />" />
								</logic:equal>
								<logic:notEqual name="testQuestion" property='<%="response.isCorrect"%>' value="true">
									<img src="<%= request.getContextPath() %>/images/incorrect.gif" alt="<bean:message key="incorrect" bundle="IMAGE_RESOURCES" />" />
								</logic:notEqual>
							</logic:notEmpty>
						</logic:notEmpty>
						</logic:notEmpty>
					</logic:notEqual>
				</logic:equal>
				</td></tr><tr><td>
			<%}%>
		<%} else if((((Integer)testType).intValue()!=3) &&(((Integer)formula).intValue()!=1)){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.IMS--%>
			</td></tr>
			<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
				</table>
			<%}%>
			<tr><td>
			<logic:notEqual name="pageType" value="doTest">
				<logic:notEqual name="correction" property="availability" value="1">
					<logic:equal name="distributedTest" property="imsFeedback" value="true">
						<bean:define id="imageLabel" value="false"/>
						<logic:notEmpty name="testQuestion" property="response">
						<logic:notEmpty name="testQuestion" property="response.response">
							<logic:notEmpty name="testQuestion" property="response.responseProcessingIndex">
								<logic:notEmpty name="subQuestion" property="responseProcessingInstructions">
									<bean:define id="responseProcessingIndex" name="testQuestion" property="response.responseProcessingIndex"/>
									<logic:notEmpty name="subQuestion" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
										<logic:iterate id="feedback" name="subQuestion" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
											<bean:define id="feedbackLabel" name="feedback" property="label"/>
											<%if (((String)feedbackLabel).startsWith("image/")){%>
												<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
												<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;feedbackCode="+responseProcessingIndex.toString() +" &amp;imgType="+feedbackLabel.toString()+"&amp;item="+item.toString()%>" altKey="feedbackLabel" bundle="IMAGE_RESOURCES"/>
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
						</logic:notEmpty>
					</logic:equal>
				</logic:notEqual>
			</logic:notEqual>
		<%} else if(((Integer)testType).intValue()==3) {%> <%-- TestType.INQUIRY--%>
			</td></tr>
			<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
				</table>
			<%}
		}%>
		<%}%>
		</logic:iterate>
		<tr><td>
	</logic:iterate>
	</td></tr><tr><td><hr/></td></tr>
</table><br/><br/>
</logic:notEmpty>
</logic:present>