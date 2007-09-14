<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
	<logic:present name="seminaries">
		<logic:present name="cases">
			<logic:present name="modalities">
				<logic:present name="cases">

						<em><bean:message key="label.portal.seminaries"/></em>
						<h2><bean:message key="label.candicaciesGrid.Title"/></h2>

						<html:form action="/showCandidacies.do">

							<div class="infoop2">
								<bean:message key="message.candidaciesGridHints"/>
							</div>

							<table class="tstyle5">
								<tr>
									<td>
										<bean:message key="label.seminary.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.seminaryID" property="seminaryID">
												<html:option value="-1" key="label.seminary.candidaciesGrid.select">
													<bean:message key="label.seminary.candidaciesGrid.select"/>
												</html:option>
											<html:options collection="seminaries" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.degree.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeID" property="degreeID">
											<html:option value="-1" key="label.degree.candidaciesGrid.select">
												<bean:message key="label.degree.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="degrees" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.course.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.courseID" property="courseID">
											<html:option value="-1" key="label.course.candidaciesGrid.select">
												<bean:message key="label.course.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="courses" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.modality.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.modalityID" property="modalityID">
											<html:option value="-1" key="label.modality.candidaciesGrid.select">
												<bean:message key="label.modality.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="modalities" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.theme.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.themeID" property="themeID">
											<html:option value="-1" key="label.theme.candidaciesGrid.select">
												<bean:message key="label.theme.candidaciesGrid.select"/>
											</html:option>
											<html:options collection="themes" property="idInternal" labelProperty="name"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case1.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.case1ID" property="case1ID">
											<html:option value="-1" key="label.case1.candidaciesGrid.select">
												<bean:message key="label.case1.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case2.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.case2ID" property="case2ID">
											<html:option value="-1" key="label.case2.candidaciesGrid.select">
												<bean:message key="label.case2.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case3.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.case3ID" property="case3ID">
											<html:option value="-1" key="label.case3.candidaciesGrid.select">
												<bean:message key="label.case3.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case4.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.case4ID" property="case4ID">
											<html:option value="-1" key="label.case4.candidaciesGrid.select">
												<bean:message key="label.case4.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.case5.candidaciesGrid"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.case5ID" property="case5ID">
											<html:option value="-1" key="label.case5.candidaciesGrid.select">
												<bean:message key="label.case5.candidaciesGrid.select"/>
											</html:option>
											<logic:iterate name="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
												<option value="<bean:write name="caseStudy" property="idInternal"/>"
														title="<bean:write name="caseStudy" property="name"/>">
													<bean:write name="caseStudy" property="code"/>
												</option>
											</logic:iterate>
										</html:select>
									</td>
								</tr>
								<tr>
									<td>
										<bean:message key="label.onlyApproved"/>:
									</td>
									<td>
										<html:select bundle="HTMLALT_RESOURCES" altKey="select.approved" property="approved">
											<html:option value="-1" key="label.approved.all">
												<bean:message key="label.approved.all"/>
											</html:option>
											<html:option value="true" key="label.approved.yes">
												<bean:message key="label.approved.yes"/>
											</html:option>
											<html:option value="false" key="label.approved.no">
												<bean:message key="label.approved.no"/>
											</html:option>
										</html:select>
									</td>
								</tr>
							</table>
							
							<p>							
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" styleClass="button" value="&nbsp;&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;&nbsp;" property="submition"/>
								<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="button" value="Limpar"/>
							</p>
							

									<%Map parameters = request.getParameterMap();
									request.setAttribute("parameters",parameters);%>
									<bean:define id="args" type="java.util.Map" name="parameters"/>
								<p class="mtop15">
									<html:link page="/getTabSeparatedCandidacies.do" name="args">
										<bean:message key="link.getExcelSpreadSheet"/>
									</html:link>
								</p>
								<%Map sendMailParameters = new TreeMap(request.getParameterMap());
						              sendMailParameters.put("method","prepareCandidaciesSend");
						              sendMailParameters.put("candidaciesSend","true");
									  request.setAttribute("sendMailParameters",sendMailParameters);
								%>
								<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
								<p>
									<html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
										<bean:message key="link.sendEmailToAllStudents"/>
									</html:link>
								</p>
								<bean:define name="candidacies" type="java.util.List" id="candidacies"/>
								<p class="mbottom15">
									<%=candidacies.size()%> <bean:message key="message.enrolledStudents"/>
								</p>


							
							
							<table class="tstyle4 tdcenter">
								<tr>
									<th>
										Detalhes
									</th>
									<th>
										Nº
									</th>
									<th>
										Nome
									</th>
									<th>
										Média
									</th>
									<th>
										Cadeiras Feitas
									</th>
									<th>
										Aceite
									</th>
									<th>
										E-Mail
									</th>
									<th>
										Seminário
									</th>
									<th>
										Curso
									</th>
									<th>
										Disciplina
									</th>
									<th>
										Modalidade
									</th>
									<th>
										Tema
									</th>
									<th>
										Motivação
									</th>
									<th>
										Caso 1
									</th>
									<th>
										Caso 2
									</th>
									<th>
										Caso 3
									</th>
									<th>
										Caso 4
									</th>
									<th>
										Caso 5
									</th>
								</tr>
								
				<logic:present name="candidacies">
						<logic:notEmpty name="candidacies">
							<logic:iterate name="candidacies" id="candidacy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCandidacyDetails">
								<tr>
									<td>
										<html:link page="/candidacyDetails.do" 
												paramId="objectCode" 
												paramName="candidacy" 
												paramProperty="idInternal">
											Ver
										</html:link>
									</td>
									<td>
										<html:link page="/viewCandidateCurriculum.do" 
												paramId="username" 
												paramName="candidacy" 
												paramProperty="student.infoPerson.username">
											<bean:write name="candidacy" property="student.number"/>
										</html:link>
									</td>
									<td  title="<bean:write name="candidacy" property="student.infoPerson.nome"/>">
										<html:link page="/viewCandidateCurriculum.do" 
											paramId="username" 
											paramName="candidacy" 
											paramProperty="student.infoPerson.username">
										<%
										String shortName = candidacy.getStudent().getInfoPerson().getNome();
										String[] names = shortName.split(" ");
										String firstName = names[0];
										String lastName = names[names.length-1];
										out.print(firstName + " " + lastName);
										%>
										</html:link>
									</td>
									<td>
										<bean:write name="candidacy" property="infoClassification.aritmeticClassification"/>
									</td>
									<td>
										<bean:write name="candidacy" property="infoClassification.completedCourses"/>
									</td>
									<td>
										<logic:equal name="candidacy" property="approved" value="true">
											Sim
										</logic:equal>
										<logic:notEqual name="candidacy" property="approved" value="true">
											Não
										</logic:notEqual>
									</td>
									<td>
										&nbsp;
										<a href="mailto:<%=candidacy.getStudent().getInfoPerson().getEmail()%>">
											<bean:write name="candidacy" property="student.infoPerson.email"/>
										</a>
									</td>
									<td>
										<html:link page="/showCandidacies.do" 
											  paramId="seminaryID" 
											  paramName="candidacy" 
											  paramProperty="seminary.idInternal">
											<bean:write name="candidacy" property="seminary.name"/>
										</html:link>
									</td>
									<td  title="<bean:write name="candidacy" property="curricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/>">
										<html:link page="/showCandidacies.do"
											  paramId="degreeID"
											  paramName="candidacy"
											  paramProperty="curricularCourse.infoDegreeCurricularPlan.idInternal">
											<bean:write name="candidacy" property="curricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
										</html:link>
									</td>
									<td>
										<html:link page="/showCandidacies.do"
											  paramId="courseID"
											  paramName="candidacy"
											  paramProperty="curricularCourse.idInternal">
											<bean:write name="candidacy" property="curricularCourse.name"/>
										</html:link>
									</td>
									<td>
										<html:link page="/showCandidacies.do"
											  paramId="modalityID"
											  paramName="candidacy"
											  paramProperty="modality.idInternal">
											<bean:write name="candidacy" property="modality.name"/>
										</html:link>
									</td>
									<td>
										<logic:notEmpty name="candidacy" property="theme">
											<html:link page="/showCandidacies.do"
													paramId="themeID"
													paramName="candidacy"
													paramProperty="theme.idInternal">
												<bean:write name="candidacy" property="theme.name"/>
											</html:link>
										</logic:notEmpty>
										<logic:empty name="candidacy" property="theme">
												N/A
										</logic:empty>
									</td>
									<td>
										&nbsp;
										<%
                
											String motivationDigest = new String();
											String[] motivationVector = candidacy.getMotivation().split(" ");
											
											for (int i= 0; i < motivationVector.length && i < 15; i++)
												motivationDigest += motivationVector[i] + " ";
											if (motivationVector.length > 15)
												motivationDigest+= "[...]";

											out.print(motivationDigest);

										%>
									</td>
										<bean:size id="selectedCasesSize" name="candidacy" property="cases"/>
									<logic:iterate indexId="index" name="candidacy" property="cases" id="caseStudy" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy">
										<td  title="<bean:write name="caseStudy" property="name"/>">
										<html:link page="/showCandidacies.do"
														paramId="<%="case" + (index.intValue()+1) +"ID"%>"
														paramName="caseStudy"
														paramProperty="idInternal">
												<bean:write name="caseStudy" property="code"/>
											</html:link>
										</td> 
									</logic:iterate>

									<%
									for (int toStuff = 5 - selectedCasesSize.intValue();toStuff>=1;toStuff--)
									{
										out.print("<td class=\"listClasses\">N/A</td>");
									}
									%> 

								</tr>
							</logic:iterate>
					</logic:notEmpty>
				</logic:present>
				</table>
			</html:form>
			</logic:present>
		</logic:present>
	</logic:present>
</logic:present>