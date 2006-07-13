<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="infoStudentTestQuestionList">
<logic:notEmpty name="infoStudentTestQuestionList" >

<bean:define id="testCode" value='<%=request.getParameter("testCode")%>'/>
<bean:define id="pageType" value='<%=request.getParameter("pageType")%>'/>
<span class="error"><html:errors/></span>
<br/>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="10">
	<logic:iterate id="testQuestion" name="infoStudentTestQuestionList" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion">
		<tr><td><hr></td></tr>
		<bean:define id="question" name="testQuestion" property="question" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion"/>
		<bean:define id="distributedTest" name="testQuestion" property="distributedTest" type="net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest"/>
		<bean:define id="questionCode" name="question" property="idInternal"/>
		<bean:define id="questionOrder" name="testQuestion" property="testQuestionOrder"/>
		<bean:define id="correction" name="testQuestion" property="distributedTest.correctionAvailability" type="net.sourceforge.fenixedu.util.tests.CorrectionAvailability"/>
		<bean:define id="formula" name="testQuestion" property="correctionFormula.formula"/>
		<bean:define id="testType" name="testQuestion" property="distributedTest.testType.type"/>
		<bean:define id="questionType" name="question" property="questionType.type"/>
		
		<bean:define id="mark" name="testQuestion" property="testQuestionMark"/>
		<tr><td><b><bean:message key="label.test.questionOrder" />:</b>&nbsp;<bean:write name="questionOrder"/></td></tr>
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
		<bean:define id="index" value="0"/>
		<bean:define id="imageLabel" value="false"/>
		<logic:iterate id="questionBody" name="question" property="question">
			<bean:define id="questionLabel" name="questionBody" property="label"/>	
			<%if (((String)questionLabel).startsWith("image/")){%>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode=" + questionCode+"&amp;imgCode="+index.toString() +"&amp;imgType="+questionLabel.toString()%>" altKey="questionLabel" bundle="IMAGE_RESOURCES"/>
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
		
		<bean:define id="cardinality" name="question" property="questionType.cardinalityType.type"/>
		<bean:define id="optionOrder" value="<%= (new Integer(Integer.parseInt(questionOrder.toString()) -1)).toString() %>"/>
		<bean:define id="indexOption" value="0"/>
		<bean:define id="correct" value="false"/>
		<logic:iterate id="questionOption" name="question" property="options">
		<logic:iterate id="optionBody" name="questionOption" property="optionContent">
			<bean:define id="optionLabel" name="optionBody" property="label"/>
			<%if (((String)optionLabel).startsWith("image/")){ %>
				<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;imgType="+optionLabel.toString()%>" altKey="optionLabel" bundle="IMAGE_RESOURCES"/>
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
							</td></tr><tr><td>
							<logic:equal name="pageType" value="correction">
								<logic:notEmpty name="question" property="responseProcessingInstructions">
									<logic:iterate id="rp" name="question" property="responseProcessingInstructions">
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
										<input type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" disabled="<%=new Boolean(checkDisable).booleanValue()%>" checked/>
									</logic:equal>
									<logic:equal name="responsed" value="false">
										<input type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
									</logic:equal>
							</logic:equal>
							<logic:equal name="checkDisable" value="false">
									<logic:equal name="responsed" value="true">
										<input type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>" checked/>
									</logic:equal>
									<logic:equal name="responsed" value="false">
										<input type="radio" name="<%=questionValue%>" value="<%= indexOption.toString() %>"/>
									</logic:equal>
							</logic:equal>
						<%}else if(((Integer)cardinality).intValue()==2 ){ %>  <%--CardinalityType.MULTIPLE--%>
					
						<%
						String[] a = (String[])request.getAttribute(questionValue);
						if(a!=null){
							for(int i=0; i<a.length; i++){
								if(a[i].equals(indexOption)){%>
								<bean:define id="responsed" value="true"/>
							<%  }
							}
						}
						%>
							</tr></td><tr><td>
							<logic:equal name="checkDisable" value="true">
								<logic:equal name="responsed" value="true">
									<input type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>" checked>
								</logic:equal>
								<logic:equal name="responsed" value="false">
									<input type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>">
								</logic:equal>
							</logic:equal>
							<logic:equal name="checkDisable" value="false">
								<logic:equal name="responsed" value="true">
									<input type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>" checked>
								</logic:equal>
								<logic:equal name="responsed" value="false">
									<input type="checkbox" name="<%=questionValue%>" value="<%= indexOption.toString()%>">
								</logic:equal>
							</logic:equal>
						<%}%>
					</logic:equal>
					
				<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
					<bean:define id="questionValue" name='<%="question"+ optionOrder%>'/>
					<logic:notEmpty name="question" property="questionType.render.maxchars">
						<bean:define id="maxchars" name="question" property="questionType.render.maxchars"/>
						<logic:notEmpty name="question" property="questionType.render.columns">
							<bean:define id="cols" name="question" property="questionType.render.columns"/>
							<html:text maxlength="<%=maxchars.toString()%>" size="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
						</logic:notEmpty>
						<logic:empty name="question" property="questionType.render.columns">
							<bean:define id="textBoxSize" value="<%=maxchars.toString()%>"/>
							<logic:greaterThan name="textBoxSize" value="100" >
								<bean:define id="textBoxSize" value="100"/>
							</logic:greaterThan>
							<html:text maxlength="<%=maxchars.toString()%>" size="<%=textBoxSize%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
						</logic:empty>	
					</logic:notEmpty>	
					<logic:empty name="question" property="questionType.render.maxchars">
						<logic:notEmpty name="question" property="questionType.render.rows">
							<bean:define id="rows" name="question" property="questionType.render.rows"/>
							<logic:notEmpty name="question" property="questionType.render.columns">
								<bean:define id="cols" name="question" property="questionType.render.columns"/>
								<html:textarea rows="<%=rows.toString()%>" cols="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:notEmpty>
							<logic:empty name="question" property="questionType.render.columns">

								<html:textarea rows="<%=rows.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:empty>
						</logic:notEmpty>
						<logic:empty name="question" property="questionType.render.rows">
							<logic:notEmpty name="question" property="questionType.render.columns">
								<bean:define id="cols" name="question" property="questionType.render.columns"/>
								<html:textarea cols="<%=cols.toString()%>" value="<%=questionValue.toString()%>" property='<%="question"+ optionOrder%>' disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
							</logic:notEmpty>
							<logic:empty name="question" property="questionType.render.columns">
								<html:text property='<%="question"+ optionOrder%>' value="<%=questionValue.toString()%>" disabled="<%=new Boolean(checkDisable).booleanValue()%>"/>
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
		<logic:equal name="imageLabel" value="true">
			</td></tr></table>
			<bean:define id="imageLabel" value="false"/>
		</logic:equal>
		<%if((((Integer)testType).intValue()!=3) &&(((Integer)formula).intValue()==1)){%> <%-- Not TestType.INQUIRY  and CorrectionFormula.FENIX--%>
			<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
				<logic:equal name="pageType" value="correction">
					<logic:notEqual name="correction" property="availability" value="1">
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
					</logic:notEqual>
				</logic:equal>
				</td></tr></table>
			<%}else{%> <%--QuestionType.STR or QuestionType.NUM --%>
				<logic:equal name="pageType" value="correction">
					<logic:notEqual name="correction" property="availability" value="1">
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
						<logic:notEmpty name="testQuestion" property="response.response">
							<logic:notEmpty name="testQuestion" property="response.responseProcessingIndex">
								<logic:notEmpty name="question" property="responseProcessingInstructions">
									<bean:define id="responseProcessingIndex" name="testQuestion" property="response.responseProcessingIndex"/>
									<logic:notEmpty name="question" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
										<logic:iterate id="feedback" name="question" property='<%="responseProcessingInstructions["+responseProcessingIndex+"].feedback"%>'>
											<bean:define id="feedbackLabel" name="feedback" property="label"/>
											<%if (((String)feedbackLabel).startsWith("image/")){%>
												<bean:define id="index" value="<%= (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
												<html:img align="middle" src="<%= request.getContextPath() + "/student/studentTests.do?method=showImage&amp;testCode="+testCode.toString()+"&amp;exerciseCode="+ questionCode +"&amp;imgCode="+index.toString() +"&amp;feedbackCode="+responseProcessingIndex.toString() +" &amp;imgType="+feedbackLabel.toString()%>" altKey="feedbackLabel" bundle="IMAGE_RESOURCES"/>
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
				</logic:notEqual>
			</logic:notEqual>
		<%} else if(((Integer)testType).intValue()==3) {%> <%-- TestType.INQUIRY--%>
			</td></tr>
			<%if(((Integer)questionType).intValue()==1 ){ %> <%--QuestionType.LID--%>
				</table>
			<%}
		}%>
		<tr><td>
	</logic:iterate>
	</td></tr><tr><td><hr></td></tr>
</table><br/><br/>
</logic:notEmpty>
</logic:present>