<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.support.faq"/></h2>

<br />
<table width="90%" cellspacing="0" cellpadding="0" style="border: 1px solid #333;">
	<logic:present name="rootInfoFAQEntries">
		<logic:iterate id="infoFAQEntry" name="rootInfoFAQEntries">
			<tr>
				<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
					<bean:message key="property.faq.question"/>: <strong><bean:write name="infoFAQEntry" property="question"/></strong>
				</td>
			</tr>
			<tr>
				<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
					<bean:message key="property.faq.answer"/>: <strong><bean:write name="infoFAQEntry" property="answer"/></strong>
				</td>
			</tr>
		</logic:iterate>
	</logic:present>
	<logic:present name="rootInfoFAQSections">
		<logic:iterate id="rootInfoFAQSection" name="rootInfoFAQSections">
			<tr>
				<td colspan="3" style="background: #333; color:#fff; padding: 5px 0 5px 10px; border: 1px solid #333;">
					<strong><bean:write name="rootInfoFAQSection" property="sectionName"/></strong>
				</td>
			</tr>
			<logic:present name="rootInfoFAQSection" property="subSections">
				<logic:iterate id="subSection" name="rootInfoFAQSection" property="subSections">
					<tr>
						<td colspan="3" style="background: #ccc; padding: 5px 0 0 10px">
							<strong><bean:write name="subSection" property="sectionName"/></strong>
						</td>
					</tr>
					<logic:present name="subSection" property="subSections">
						<logic:iterate id="subSubSection" name="subSection" property="subSections">
							<tr>
								<td colspan="3" style="background: #EBECED; padding: 5px 0 0 10px">
									<strong><bean:write name="subSubSection" property="sectionName"/></strong>
								</td>
							</tr>
							<logic:present name="subSubSection" property="entries">
								<logic:iterate id="infoFAQEntry" name="subSubSection" property="entries">
									<tr>
										<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
											<bean:message key="property.faq.question"/>: <strong><bean:write name="infoFAQEntry" property="question"/></strong>
										</td>
									</tr>
									<tr>
										<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
											<bean:message key="property.faq.answer"/>: <strong><bean:write name="infoFAQEntry" property="answer"/></strong>
										</td>
									</tr>
								</logic:iterate>
							</logic:present>
						</logic:iterate>
					</logic:present>
					<logic:present name="subSection" property="entries">
						<logic:iterate id="infoFAQEntry" name="subSection" property="entries">
							<bean:define id="entryId" name="infoFAQEntry" property="idInternal"/>
							<tr>
								<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
									<bean:message key="property.faq.question"/>: <strong><bean:write name="infoFAQEntry" property="question"/></strong>
								</td>
							</tr>
							<tr>
								<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
									<bean:message key="property.faq.answer"/>: <strong><bean:write name="infoFAQEntry" property="answer"/></strong>
								</td>
							</tr>
						</logic:iterate>
					</logic:present>
				</logic:iterate>
			</logic:present>
			<logic:present name="rootInfoFAQSection" property="entries">
				<logic:iterate id="infoFAQEntry" name="rootInfoFAQSection" property="entries">
					<tr>
						<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
							<bean:message key="property.faq.question"/>: <strong><bean:write name="infoFAQEntry" property="question"/></strong>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="background: #fff; padding: 5px 0 0 10px">
							<bean:message key="property.faq.answer"/>: <strong><bean:write name="infoFAQEntry" property="answer"/></strong>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</logic:iterate>
	</logic:present>
</table>