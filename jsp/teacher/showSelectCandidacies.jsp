<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<logic:present name="candidacies">
	<h2>
		<bean:message key="label.selectCandicaciesGrid.Title"/>
	</h2>
	<html:form action="/selectCandidacies.do">
		<html:hidden property="method" value="prepare"/>
		<html:select property="seminaryID">
			<html:option value="-1" key="label.seminary.candidaciesGrid.select">
				<bean:message key="label.seminary.candidaciesGrid.select"/>
			</html:option>
			<html:options collection="seminaries" property="idInternal" labelProperty="name"/>
		</html:select>
		<html:submit styleClass="button" value="OK" property="submition"/>
	</html:form>
	
	<html:form action="/selectCandidacies.do">
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.details"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.Number"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.Name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.Average"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.courses.done"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.Email"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.seminaries.showCandidacy.Student.Candidacy.Accepted" />
				</td>
			</tr>
			<logic:present name="candidacies">
				<logic:notEmpty name="candidacies">
					<logic:iterate name="candidacies" id="candidacy" type="DataBeans.Seminaries.CandidacyDTO">
						<tr>
							<td class="listClasses">
								<html:link page="/candidacyDetails.do" 
									paramId="objectCode" 
									paramName="candidacy" 
									paramProperty="candidacyId">
									<bean:message key="label.seminaries.showCandidacy.See"/>
								</html:link>
							</td> 
							<td class="listClasses">
								<html:link page="/viewCandidateCurriculum.do" 
										paramId="username" 
										paramName="candidacy" 
										paramProperty="username">
									<bean:write name="candidacy" property="number"/>
								</html:link>
							</td> 
							<td  class="listClasses" title="<bean:write name="candidacy" property="name"/>">
								<html:link page="/viewCandidateCurriculum.do" 
										paramId="username" 
										paramName="candidacy" 
										paramProperty="username">
									<%
									String shortName = candidacy.getName();
									String[] names = shortName.split(" ");
									String firstName = names[0];
									String lastName = names[names.length-1];
									out.print(firstName + " " + lastName);
									%>
								</html:link>
							</td>
							<td class="listClasses">
								<bean:write name="candidacy" property="infoClassification.aritmeticClassification"/>
							</td>
							<td class="listClasses">
								<bean:write name="candidacy" property="infoClassification.completedCourses"/>
							</td>
							<td class="listClasses">
								&nbsp;
								<a href=mailto:<%=candidacy.getEmail()%>>
									<bean:write name="candidacy" property="email"/>
								</a>
							</td> 
							<td class="listClasses">
								<input type="checkbox" name="selectedStudents" value="<%=candidacy.getCandidacyId().toString()%>"
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
		<logic:iterate name="candidacies" id="candidacy" type="DataBeans.Seminaries.CandidacyDTO">
			<logic:equal name="candidacy" property="approved" value="true">
				<html:hidden property="previousSelected" value="<%=candidacy.getCandidacyId().toString()%>"/>
			</logic:equal>
			<logic:notEqual name="candidacy" property="approved" value="true">
				<html:hidden property="previousUnselected" value="<%=candidacy.getCandidacyId().toString()%>"/>
			</logic:notEqual>
		</logic:iterate> 
		<html:hidden property="seminaryID"/>
		<html:hidden property="method" value="changeSelection"/> 
	</html:form>

</logic:present>