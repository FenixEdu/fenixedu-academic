<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWorkProposals"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/finalDegreeWorks" focus="executionDegreeOID">
	<html:hidden property="method" value="search"/>
	<html:hidden property="page" value="1"/>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap="nowrap">
				<bean:message key="property.executionPeriod"/>:
			</td>
			<td nowrap="nowrap">
				<html:select property="executionYearOID"
							 size="1"
							 onchange='document.finalDegreeWorksForm.method.value=\'prepareSearch\';document.finalDegreeWorksForm.page.value=\'0\';document.finalDegreeWorksForm.submit();'>
					<html:options property="idInternal" 
								  labelProperty="year" 
								  collection="infoExecutionYears" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<bean:message key="finalDegreeWorkProposalHeader.degree"/>:
			</td>
			<td nowrap="nowrap">
				<html:select property="executionDegreeOID" size="1"
							 onchange='document.finalDegreeWorksForm.method.value=\'search\';document.finalDegreeWorksForm.page.value=\'0\';document.finalDegreeWorksForm.submit();'>
					<html:option value=""/>
					<html:options property="idInternal"
								  labelProperty="infoDegreeCurricularPlan.infoDegree.nome"
								  collection="infoExecutionDegrees" />
				</html:select>
			</td>
		</tr>
	</table>
	<br />
<br />
<logic:present name="publishedFinalDegreeWorkProposalHeaders">
	<bean:size id="numberOfHeaders" name="publishedFinalDegreeWorkProposalHeaders"/>
	<bean:define id="executionDegreeOID" name="finalDegreeWorksForm" property="executionDegreeOID"/>
	<logic:greaterThan name="numberOfHeaders" value="0">

	<bean:define id="selectedBranchOID" name="finalDegreeWorksForm" property="branchOID"/>

		<table>
			<tr>
				<td nowrap="nowrap">
					<bean:message key="finalDegreeWorkProposalHeader.filter.by.branch"/>:
				</td>
				<td nowrap="nowrap">
					<html:select property="branchOID" size="1"
								 onchange='document.finalDegreeWorksForm.method.value=\'filter\';document.finalDegreeWorksForm.page.value=\'0\';document.finalDegreeWorksForm.submit();'>
						<html:option value=""/>
						<html:options property="idInternal"
									  labelProperty="name"
									  collection="branches" />
					</html:select>
				</td>
			</tr>
		</table>

		<table>
			<tr>
				<td bgcolor="#a2aebc" align="center" rowspan="2">
		        	<html:link page="<%= "/finalDegreeWorks.do?method=sortByNumber&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
			        </html:link>
				</td>
				<td bgcolor="#a2aebc" align="center" rowspan="2">
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByTitle&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</html:link>
				</td>
				<td bgcolor="#a2aebc" align="center">
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByOrientatorName&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</html:link>
				</td>
				<td bgcolor="#a2aebc" align="center" rowspan="2">
					<html:link page="<%= "/finalDegreeWorks.do?method=sortByCompanyLink&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</html:link>
				</td>
				<td bgcolor="#a2aebc" align="center" rowspan="2">
					<bean:message key="label.teacher.finalWork.priority.info"/>
				</td>
			</tr>
			<tr>
		        <td bgcolor="#a2aebc" align="center">
			        <html:link page="<%= "/finalDegreeWorks.do?method=sortByCoorientatorName&amp;executionDegreeOID=" + executionDegreeOID + "&amp;branchOID=" + selectedBranchOID %>">
			        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
			        </html:link>
	    	    </td>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="publishedFinalDegreeWorkProposalHeaders">
				<tr>
					<td bgcolor="#eae7e4" align="center" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/>
					</td>
					<td bgcolor="#eae7e4" align="center" rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() %>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td bgcolor="#eae7e4" align="center">
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td bgcolor="#eae7e4" align="center" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
					<td bgcolor="#eae7e4" align="center" rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="branches">
							<table>
								<logic:iterate id="branch" name="finalDegreeWorkProposalHeader" property="branches">
									<tr>
										<td bgcolor="#eae7e4" align="center" >
											<bean:write name="branch" property="name"/>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</logic:present>
					</td>
				</tr>
				<tr>
					<td bgcolor="#eae7e4" align="center">
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:greaterThan>
	<logic:lessEqual name="numberOfHeaders" value="0">
		<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessEqual>	
</logic:present>
</html:form>