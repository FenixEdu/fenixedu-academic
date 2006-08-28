<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>

<logic:present name="infoDegreeCurricularPlan">
	
	<div class="breadcumbs mvert0"><a href="http://www.ist.utl.pt/index.shtml">IST</a>
		&nbsp;&gt;&nbsp;<a href="http://www.ist.utl.pt/html/ensino/index.shtml">Ensino</a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp; 
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("degreeID").toString()%>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="label.degreeEvaluation"/>
	</div>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

	<h1>
		Avalia&ccedil;&atilde;o do Funcionamento das Disciplinas
	</h1>
	<h2>
		<c:if test="${degreeType == 'DEGREE'}">
			Licenciatura
		</c:if>
		<c:if test="${degreeType == 'MASTER_DEGREE'}">
			Mestrado
		</c:if>
		em
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h2>
		
	<logic:present name="executionPeriodList">
		<table class="invisible">
			<tr>
				<td class="invisible">
					<html:form action="/showDegreeSite">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewDegreeEvaluation"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeID" property="degreeID" value='<%= request.getAttribute("degreeID").toString() %>'/>	
						<p>Per&iacute;odo de Execu&ccedil;&atilde;o: 
							
							<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId" onchange="this.form.submit()">
								<html:option value="">Escolher</html:option>		
								<logic:iterate id="executionPeriod" name="executionPeriodList" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod"> 
									<bean:define    id="currentPeriodId"   name="executionPeriod" property="idInternal"/>
									<html:option value="<%= currentPeriodId.toString() %>">
										<bean:write name="executionPeriod" property="semester"/>&ordm; Semestre
										&nbsp;-&nbsp;
										<bean:write name="executionPeriod" property="infoExecutionYear.year"/>
									</html:option>  
								</logic:iterate>
							</html:select>
							<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
								<bean:message key="button.submit"/>
							</html:submit>
						</p>
					</html:form>
				</td>
				<td class="invisible">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%= request.getContextPath() %>/images/OldInquiry.jpg">Ver inqu&eacute;rito</a>
				</td>
			</tr>
		</table>
	
	</logic:present>

	<br/>
	
	<logic:present name="emptyOldInquiriesSummaries">
		<hr/></hr>
		<h2>N&atilde;o foram encontrados dados</h2>
	</logic:present>
	
	<logic:present name="oldInquiriesSummaries">
		<hr/>
		<%
		final String[] classes = { "inquiries_red", "inquiries_orange", "inquiries_yellow", "inquiries_green", "inquiries_grey" };
		final String defaultClass = "inquiries_grey";
		
		final double[] valuesDoubleAnswers = { 0, 2, 2.8, 3.3, 4, 5 };
		final String[] valuesHoursAnswers = { "+15h", "11-15h", "0-1h", "6-10h", "2-5h" };
		final double[] valuesRepQuota = { 0, 10, 20, 30, 40, 100};
		final double[] valuesAvalApprQuota = { 0, 50, 60, 70, 80, 100};
		final String[] valuesAssidAnswers = { "<50%", "50-75%", "75-85%", "85-95%", ">95%"};
		
		final Double appCouses = NumberUtils.formatNumber((Double)request.getAttribute("averageAppreciationCourses"), 2);
		final Double appTeachers = NumberUtils.formatNumber((Double)request.getAttribute("averageAppreciationTeachers"), 2);
		
		final Double minRepQuota = new Double(10);
		%>

		<table>
			<tr>
				<td class="invisible">
					<h3>
						<bean:message key="label.inquiries.degree.courses.appreciation" bundle="INQUIRIES_RESOURCES"/>
					</h3>
				</td>
				<%
				out.print(
					"<td class=\"" + InquiriesUtil.getTdClass(appCouses, classes, defaultClass, valuesDoubleAnswers) + "\">" +
						InquiriesUtil.formatAnswer(appCouses) +
					"</td>");
				%>
			</tr>			
			<tr>
				<td class="invisible">
					<h3>
						<bean:message key="label.inquiries.degree.teachers.appreciation" bundle="INQUIRIES_RESOURCES"/>
					</h3>
				</td>
				<%
				out.print(
					"<td class=\"" + InquiriesUtil.getTdClass(appTeachers, classes, defaultClass, valuesDoubleAnswers) + "\">" +
						InquiriesUtil.formatAnswer(appTeachers) +
					"</td>");
				%>				
			</tr>
		</table>


		<br/><br/>
		<table class="invisible">
			<tr>
				<th rowspan="2" class="box_header">Ano</th>
				<th rowspan="2" class="box_header">Nome da Disciplina</th>
				<th colspan="7" class="box_header">Grupo 2 - Disciplina</th>
				<th colspan="9" class="box_header">Grupos 3, 4 e 5 - Docentes</th>
				<th colspan="3" class="box_header">Grupo 6 - Sala</th>
				<th rowspan="2" class="box_header">Inscritos</th>
				<th rowspan="2" class="box_header">Respostas</th>
				<th rowspan="2" class="box_header">Avaliados</th>
				<th rowspan="2" class="box_header">Aprovados</th>
			</tr>
			
			<tr>
				<th class="box_header">2</th>
				<th class="box_header">3</th>
				<th class="box_header">4</th>
				<th class="box_header">5</th>
				<th class="box_header">6</th>
				<th class="box_header">7</th>
				<th class="box_header">8</th>

				<th class="box_header">3</th>
				<th class="box_header">4</th>
				<th class="box_header">5</th>
				<th class="box_header">6</th>
				<th class="box_header">7</th>
				<th class="box_header">8</th>
				<th class="box_header">9</th>
				<th class="box_header">10</th>
				<th class="box_header">11</th>

				<th class="box_header">1</th>
				<th class="box_header">2</th>
				<th class="box_header">3</th>

			</tr>
				
			<logic:iterate id="inquirySummary" name="oldInquiriesSummaries" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary">

				<bean:define id="gepCourseName" name="inquirySummary" property="gepCourseName" type="java.lang.String"/>

				<bean:define id="average2_2" name="inquirySummary" property="average2_2" type="java.lang.Double"/>
				<bean:define id="average2_3" name="inquirySummary" property="average2_3" type="java.lang.Double"/>
				<bean:define id="average2_4" name="inquirySummary" property="average2_4" type="java.lang.Double"/>
				<bean:define id="average2_5" name="inquirySummary" property="average2_5" type="java.lang.Double"/>
				<bean:define id="average2_6" name="inquirySummary" property="average2_6" type="java.lang.Double"/>
				<bean:define id="average2_7_interval" name="inquirySummary" property="average2_7_interval" type="java.lang.String"/>
				<bean:define id="average2_8" name="inquirySummary" property="average2_8" type="java.lang.Double"/>

				<bean:define id="average3_3_interval" name="inquirySummary" property="average3_3_interval" type="java.lang.String"/>
				<bean:define id="average3_4_interval" name="inquirySummary" property="average3_4_interval" type="java.lang.String"/>
				<bean:define id="average3_5" name="inquirySummary" property="average3_5" type="java.lang.Double"/>
				<bean:define id="average3_6" name="inquirySummary" property="average3_6" type="java.lang.Double"/>
				<bean:define id="average3_7" name="inquirySummary" property="average3_7" type="java.lang.Double"/>
				<bean:define id="average3_8" name="inquirySummary" property="average3_8" type="java.lang.Double"/>
				<bean:define id="average3_9" name="inquirySummary" property="average3_9" type="java.lang.Double"/>
				<bean:define id="average3_10" name="inquirySummary" property="average3_10" type="java.lang.Double"/>
				<bean:define id="average3_11" name="inquirySummary" property="average3_11" type="java.lang.Double"/>

				<bean:define id="average6_1" name="inquirySummary" property="average6_1" type="java.lang.Double"/>
				<bean:define id="average6_2" name="inquirySummary" property="average6_2" type="java.lang.Double"/>
				<bean:define id="average6_3" name="inquirySummary" property="average6_3" type="java.lang.Double"/>

				<bean:define id="numberEnrollments" name="inquirySummary" property="numberEnrollments" type="java.lang.Integer"/>
				<bean:define id="numberAnswers" name="inquirySummary" property="numberAnswers" type="java.lang.Integer"/>
				<bean:define id="numberEvaluated" name="inquirySummary" property="numberEvaluated" type="java.lang.Integer"/>
				<bean:define id="numberApproved" name="inquirySummary" property="numberApproved" type="java.lang.Integer"/>

				<bean:define id="repQuota" name="inquirySummary" property="representationQuota" type="java.lang.Double"/>

				<tr>
					<td class="box_cell">
						<bean:write name="inquirySummary" property="curricularYear"/>
					</td>
					<td class="box_cell">
						<%= InquiriesUtil.formatAnswer(gepCourseName) %>
					</td>

					<%
					final Double av2_2 = NumberUtils.formatNumber(average2_2, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_2, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_2, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av2_3 = NumberUtils.formatNumber(average2_3, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_3, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_3, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av2_4 = NumberUtils.formatNumber(average2_4, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_4, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_4, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av2_5 = NumberUtils.formatNumber(average2_5, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_5, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_5, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av2_6 = NumberUtils.formatNumber(average2_6, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_6, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_6, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(average2_7_interval, classes, defaultClass, valuesHoursAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(average2_7_interval, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av2_8 = NumberUtils.formatNumber(average2_8, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av2_8, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av2_8, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(average3_3_interval, classes, defaultClass, valuesAssidAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(average3_3_interval, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(average3_4_interval, classes, defaultClass, valuesAssidAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(average3_4_interval, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_5 = NumberUtils.formatNumber(average3_5, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_5, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_5, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_6 = NumberUtils.formatNumber(average3_6, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_6, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_6, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_7 = NumberUtils.formatNumber(average3_7, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_7, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_7, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_8 = NumberUtils.formatNumber(average3_8, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_8, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_8, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_9 = NumberUtils.formatNumber(average3_9, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_9, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_9, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_10 = NumberUtils.formatNumber(average3_10, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_10, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_10, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av3_11 = NumberUtils.formatNumber(average3_11, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av3_11, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av3_11, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av6_1 = NumberUtils.formatNumber(average6_1, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av6_1, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av6_1, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av6_2 = NumberUtils.formatNumber(average6_2, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av6_2, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av6_2, repQuota, minRepQuota) +
						"</td>");
					%>

					<%
					final Double av6_3 = NumberUtils.formatNumber(average6_3, 1);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(av6_3, classes, defaultClass, valuesDoubleAnswers, repQuota, minRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(av6_3, repQuota, minRepQuota) +
						"</td>");
					%>
						
					<%
					out.print(
						"<td class=\"inquiries_grey\">" +
							InquiriesUtil.formatAnswer(numberEnrollments) +
						"</td>");
					%>

					<%
					final Double repQuotaRound = NumberUtils.formatNumber(repQuota, 0);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(repQuotaRound, classes, defaultClass, valuesRepQuota) + "\">" +
							InquiriesUtil.formatAnswer(repQuotaRound) +
							(repQuota.doubleValue() > 0 ? "%" : "") +
						"</td>");
					%>
					<%
					final Double evaluatedQuota = NumberUtils.formatNumber(
												new Double((numberEvaluated.doubleValue() /numberEnrollments.doubleValue())*100.0), 0);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(evaluatedQuota, classes, defaultClass, valuesAvalApprQuota) + "\">" +
							InquiriesUtil.formatAnswer(evaluatedQuota) +
							(evaluatedQuota.doubleValue() > 0 ? "%" : "") +
						"</td>");
					%>
					<%
					final Double approvedQuota = NumberUtils.formatNumber(
												new Double((numberApproved.doubleValue() /numberEvaluated.doubleValue())*100.0), 0);
					out.print(
						"<td class=\"" + InquiriesUtil.getTdClass(approvedQuota, classes, defaultClass, valuesAvalApprQuota) + "\">" +
							InquiriesUtil.formatAnswer(approvedQuota) +
							(approvedQuota.doubleValue() > 0 ? "%" : "") +
						"</td>");
					%>

				</tr>
			</logic:iterate>
		</table>
		
		<%-- Legendas --%>
		
		<br/>
		<h2>LEGENDA:</h2>
		<table class="invisible">

			<td>
				<table class="box">
					<tr><th colspan="3">
						Opini&atilde;o dos Alunos - Grupos 2 a 6, excepto as  quest&otilde;es 7 do grupo 2, 3 e 4 do grupo 3
					</th></tr>
					<%
					int i = 0;
					for(; i < classes.length-1; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesDoubleAnswers[i] %>&nbsp;;&nbsp;<%= valuesDoubleAnswers[i+1] %>&nbsp;[</td>
						</tr>
					<%
					}
					%>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesDoubleAnswers[i] %>&nbsp;;&nbsp;<%= valuesDoubleAnswers[i+1] %>&nbsp;]</td>
						</tr>
				</table>
			</td>

			<td>
				<table class="box">
					<tr><th colspan="3">
						Quest&atilde;o 7 do grupo 2 - N&ordm; m&eacute;dio de horas semanais despendido na disciplina
					</th></tr>
					<%
					for(i = 0; i < classes.length; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white"><%= valuesHoursAnswers[i] %></td>
						</tr>
					<%
					}
					%>
				</table>
			</td>

			<td>
				<table class="box">
					<tr><th colspan="3">
						Quest&otilde;es 3 e 4 do grupo 3 - Assiduidade do aluno e do docente, respectivamente
					</th></tr>
					<%
					for(i = 0; i < classes.length; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white"><%= valuesAssidAnswers[i] %></td>
						</tr>
					<%
					}
					%>
				</table>
			</td>

			<td>
				<table class="box">
					<tr><th colspan="3">
						Respostas - Percentagem de inqu&eacute;ritos respondidos face ao n&ordm; de alunos inscritos
					</th></tr>
					<%
					for(i = 0; i < classes.length-1; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesRepQuota[i] %>%&nbsp;;&nbsp;<%= valuesRepQuota[i+1] %>%&nbsp;[</td>
						</tr>
					<%
					}
					%>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesRepQuota[i] %>%&nbsp;;&nbsp;<%= valuesRepQuota[i+1] %>%&nbsp;]</td>
						</tr>
				</table>
			</td>

			<td>
				<table class="box">
					<tr><th colspan="3">
						Avaliados - Percentagem de alunos examinados face ao n&ordm; de alunos inscritos
					</th></tr>
					<%
					for(i = 0; i < classes.length-1; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesAvalApprQuota[i] %>%&nbsp;;&nbsp;<%= valuesAvalApprQuota[i+1] %>%&nbsp;[</td>
						</tr>
					<%
					}
					%>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesAvalApprQuota[i] %>%&nbsp;;&nbsp;<%= valuesAvalApprQuota[i+1] %>%&nbsp;]</td>
						</tr>
				</table>
			</td>

			<td>
				<table class="box">
					<tr><th colspan="3">
						Aprovados - Percentagem de alunos aprovados face ao n&ordm; de alunos examinados
					</th></tr>
					<%
					for(i = 0; i < classes.length-1; i++) { %>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesAvalApprQuota[i] %>%&nbsp;;&nbsp;<%= valuesAvalApprQuota[i+1] %>%&nbsp;[</td>
						</tr>
					<%
					}
					%>
						<tr>
							<td class="inquiries_white">&nbsp;</td>
							<td class='<%= classes[i]%>'>&nbsp;</td>
							<td class="inquiries_white">[&nbsp;<%= valuesAvalApprQuota[i] %>%&nbsp;;&nbsp;<%= valuesAvalApprQuota[i+1] %>%&nbsp;]</td>
						</tr>
				</table>
			</td>



		</table>
		
	</logic:present>

	
</logic:present>