<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>

<logic:present name="candidacies">
	<h2>
		<bean:message key="label.selectCandicaciesGrid.Title"/>
	</h2>
	<html:form action="/selectCandidacies.do" method="get">
		<html:select property="seminaryID">
			<html:option value="-1" key="label.seminary.candidaciesGrid.select">
				<bean:message key="label.seminary.candidaciesGrid.select"/>
			</html:option>
			<html:options collection="seminaries" property="idInternal" labelProperty="name"/>
		</html:select>
		<table>
			<tr>
				<td class="listClasses-header">
					Detalhes
				</td>
				<td class="listClasses-header">
					Nº
				</td>
				<td class="listClasses-header">
					Nome
				</td>
				<td class="listClasses-header">
					Média
				</td>
				<td class="listClasses-header">
					Cadeiras Feitas
				</td>
				<td class="listClasses-header">
					E-Mail
				</td>
				<td class="listClasses-header">
					Aceite
				</td>
			</tr>
			<logic:present name="candidacies">
				<logic:notEmpty name="candidacies">
					<logic:iterate name="candidacies" id="candidacy" type="DataBeans.Seminaries.InfoCandidacyDetails">
						<tr>
							<td class="listClasses">
								<html:link page="/candidacyDetails.do" 
									paramId="objectCode" 
									paramName="candidacy" 
									paramProperty="idInternal">
									Ver
								</html:link>
							</td>
							<td class="listClasses">
								<bean:write name="candidacy" property="student.number"/>
							</td>
							<td  class="listClasses" title="<bean:write name="candidacy" property="student.infoPerson.nome"/>">
								<%
								String shortName = candidacy.getStudent().getInfoPerson().getNome();
								String[] names = shortName.split(" ");
								String firstName = names[0];
								String lastName = names[names.length-1];
								out.print(firstName + " " + lastName);
								%>
							</td>
							<td class="listClasses">
								<bean:write name="candidacy" property="infoClassification.aritmeticClassification"/>
							</td>
							<td class="listClasses">
								<bean:write name="candidacy" property="infoClassification.completedCourses"/>
							</td>
							<td class="listClasses">
								<a href=mailto:<%=candidacy.getStudent().getInfoPerson().getEmail()%>>
									<bean:write name="candidacy" property="student.infoPerson.email"/>
								</a>
							</td>
							<td class="listClasses">
								<input type="checkbox" name="selectedStudents" value="<%=candidacy.getIdInternal().toString()%>"
									<%  if (candidacy.getApproved().booleanValue())
											out.print("checked");	
									%>								
								/>
							</td>
						</tr>
					</logic:iterate>
					<tr>
						<td>
							<html:submit property="submition" value="OK"/>
						</td>
					</tr>
				</logic:notEmpty>
			</logic:present>
		</table>
		<logic:iterate name="candidacies" id="candidacy" type="DataBeans.Seminaries.InfoCandidacyDetails">
			<logic:equal name="candidacy" property="approved" value="true">
				<html:hidden property="previousSelected" value="<%=candidacy.getIdInternal().toString()%>"/>
			</logic:equal>
			<logic:notEqual name="candidacy" property="approved" value="true">
				<html:hidden property="previousUnselected" value="<%=candidacy.getIdInternal().toString()%>"/>
			</logic:notEqual>
		</logic:iterate>
		<html:hidden property="method" value="changeSelection"/>
	</html:form>
</logic:present>