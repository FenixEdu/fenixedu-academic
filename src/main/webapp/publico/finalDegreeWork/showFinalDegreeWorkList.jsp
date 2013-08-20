<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html:xhtml/>

<h2><bean:message key="title.finalDegreeWorkProposals"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

	<table class="tstyle5 mtop15 mbottom2">
		<tr>
			<td nowrap="nowrap">
				<bean:message key="property.executionPeriod"/>:
			</td>
			<td nowrap="nowrap">
				<html:form action="/finalDegreeWorks" focus="executionDegreeOID">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareSearch"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
					<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID"
								 size="1"
								 onchange="this.form.submit();">
						<html:options property="externalId" 
									  labelProperty="nextExecutionYearYear" 
									  collection="infoExecutionYears" />
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<bean:message key="finalDegreeWorkProposalHeader.degree"/>:
			</td>
			<td nowrap="nowrap">
				<html:form action="/finalDegreeWorks" focus="executionDegreeOID">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<bean:define id="executionYearOID" name="finalDegreeWorksForm" property="executionYearOID"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="select.executionYearOID" property="executionYearOID" value="<%= executionYearOID.toString() %>"/>
					<html:select bundle="HTMLALT_RESOURCES" property="executionDegreeOID" size="1"
								 onchange="this.form.submit();">
						<html:option value=""> <!-- w3c complient --> </html:option>
						<html:options property="externalId"
									  labelProperty="infoDegreeCurricularPlan.name"
									  collection="infoExecutionDegrees" />
					</html:select>
					<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>

<logic:present name="publishedFinalDegreeWorkProposalHeaders">
	<bean:size id="numberOfHeaders" name="publishedFinalDegreeWorkProposalHeaders"/>
	<bean:define id="executionDegreeOID" name="finalDegreeWorksForm" property="executionDegreeOID"/>
	<logic:greaterThan name="numberOfHeaders" value="0">


	<bean:define id="selectedBranchOID" name="finalDegreeWorksForm" property="branchOID"/>

<%--
		<table>
			<tr>
				<td nowrap="nowrap">
					<bean:message key="finalDegreeWorkProposalHeader.filter.by.branch"/>:
				</td>
				<td nowrap="nowrap">
					<html:form action="/finalDegreeWorks" focus="executionDegreeOID">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
						<bean:define id="executionYearOID" name="finalDegreeWorksForm" property="executionYearOID"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="select.executionYearOID" property="executionYearOID" value="<%= executionYearOID.toString() %>"/>
						<bean:define id="executionDegreeOID" name="finalDegreeWorksForm" property="executionDegreeOID"/>
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>
						<html:select bundle="HTMLALT_RESOURCES" property="branchOID" size="1"
									 onchange="this.form.submit();">
							<html:option value=""><!-- w3c complient--> </html:option>
							<html:options property="externalId"
										  labelProperty="name"
										  collection="branches" />
						</html:select>
						<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
					</html:form>
				</td>
			</tr>
		</table>
--%>

		<table class="tstyle4">
			<tr>
				<th rowspan="2">
		        	<html:link page="<%= "/finalDegreeWorks.do?method=sortByNumber&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
			        </html:link>
				</th>
				<th rowspan="2">
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByTitle&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</html:link>
				</th>
				<th>
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByOrientatorName&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</html:link>
				</th>
				<th rowspan="2">
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByCompanyLink&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</html:link>
				</th>
<!--
				<td rowspan="2">
					<bean:message key="label.teacher.finalWork.priority.info"/>
				</td>
-->
				<th rowspan="2">
					<bean:message key="finalDegreeWorkProposal.attribution.byTeacher"/>
				</th>
				<th rowspan="2">
				</th>
			</tr>
			<tr>
		        <th>
			        <html:link page="<%= "/finalDegreeWorks.do?method=sortByCoorientatorName&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
			        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
			        </html:link>
	    	    </th>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="publishedFinalDegreeWorkProposalHeaders">
				<tr>
					<td rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/>
					</td>
					<td rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() %>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td>
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
<%--
					<td rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="branches">
							<bean:size id="numberBranches" name="finalDegreeWorkProposalHeader" property="branches"/>
							<logic:greaterThan name="numberBranches" value="0">
								<table>
									<logic:iterate id="branch" name="finalDegreeWorkProposalHeader" property="branches">
										<tr>
											<td >
												<bean:write name="branch" property="name"/>
											</td>
										</tr>
									</logic:iterate>
								</table>
							</logic:greaterThan>
						</logic:present>
					</td>
--%>
					<td rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="groupAttributedByTeacher">
							<logic:iterate id="groupStudent" name="finalDegreeWorkProposalHeader" property="groupAttributedByTeacher.groupStudents">
								<bean:write name="groupStudent" property="student.number"/><br/>
							</logic:iterate>
						</logic:present>
					</td>
					<td rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="groupAttributed">
							<logic:iterate id="groupStudent" name="finalDegreeWorkProposalHeader" property="groupAttributed.groupStudents">
								<bean:write name="groupStudent" property="student.number"/><br/>
							</logic:iterate>
						</logic:present>

						<logic:equal name="executionDegreeOID" value="67">
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="81">
								739-afa<br/>
								741-afa
							</logic:equal>
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="117">
								743-afa
							</logic:equal>
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="131">
								974-am<br/>
								984-am
							</logic:equal>
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="210">
								742-afa
							</logic:equal>
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="136">
								977-am
							</logic:equal>
							<logic:equal name="finalDegreeWorkProposalHeader" property="proposalNumber" value="228">
								975-am<br/>
								976-am
							</logic:equal>
						</logic:equal>
					</td>
				</tr>
				<tr>
					<td>
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:greaterThan>
	
	<logic:lessEqual name="numberOfHeaders" value="0">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
		</p>
	</logic:lessEqual>	
	
</logic:present>