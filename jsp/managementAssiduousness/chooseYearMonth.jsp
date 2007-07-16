<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<bean:define id="nextAction" name="action" type="java.lang.String"/>
<h2><bean:message key="<%= "link." + nextAction %>" /></h2>
<p class="mtop2"><span class="error0"><html:errors /></span></p>

<span class="error0 mtop0">
	<html:messages id="message" message="true">
		<bean:write name="message" />
	</html:messages>
</span>

<logic:present name="assiduousnessExportChoices">
	<fr:form action="<%="/exportAssiduousness.do?method="+nextAction%>">
		
		<logic:equal name="assiduousnessExportChoices" property="canChooseDateType" value="true">
			<fr:edit id="assiduousnessExportChoicesDatesTypes" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice.datesType">
				<fr:hidden slot="action" value="<%=nextAction %>" />
				<fr:destination name="assiduousnessExportChoicesPostBack" path="/exportAssiduousness.do?method=chooseAssiduousnessExportChoicesPostBack" />
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mvert0" />
					<fr:property name="columnClasses" value="width12em,inobullet width20em,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
		</logic:equal>
		
		<logic:notEmpty name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType">
			<logic:equal name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType" value="MONTHS">
				<fr:edit id="assiduousnessExportChoicesDates" name="assiduousnessExportChoices" schema="choose.assiduousnessExportChoicesDates.yearMonth">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
						<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>

			<logic:equal name="assiduousnessExportChoices" property="assiduousnessExportChoicesDatesType" value="DATES">
				<fr:edit id="assiduousnessExportChoicesDates" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice.dates">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
						<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>

			<fr:edit id="assiduousnessExportChoices" name="assiduousnessExportChoices" schema="choose.assiduosunessExportChoice">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thright mvert0" />
					<fr:property name="columnClasses" value="width12em,width20em,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
		</logic:notEmpty>
		
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.export" />
			</html:submit>
		</p>
	</fr:form>
</logic:present>
<logic:present name="yearMonth">
	<logic:present name="assignedEmployees">
		<fr:form action="/exportAssiduousness.do?method=exportAssignedEmployees">
			<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
				<fr:layout>
					<fr:property name="classes" value="thlight thright" />
				</fr:layout>
			</fr:edit>		
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
					<bean:message key="button.export" />
				</html:submit>
			</p>
		</fr:form>
	</logic:present>
	<logic:notPresent name="assignedEmployees">
		<fr:form action="<%="/exportExtraWork.do?method="+nextAction%>">
			<html:hidden property="action" value="<%=nextAction %>" />
			<logic:present name="chooseMonth">
				<bean:define id="chooseMonth" name="chooseMonth" type="java.lang.String"/>
				<html:hidden property="chooseMonth" value="<%=chooseMonth %>" />
				<fr:edit id="yearMonth" name="yearMonth" schema="choose.year">
					<fr:layout>
						<fr:property name="classes" value="thlight thright" />
					</fr:layout>
				</fr:edit>
			</logic:present>
			<logic:notPresent name="chooseMonth">
				<fr:edit id="yearMonth" name="yearMonth" schema="choose.date">
					<fr:layout>
						<fr:property name="classes" value="thlight thright" />
					</fr:layout>
				</fr:edit>
			</logic:notPresent>
			<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
				<bean:message key="button.export" />
			</html:submit></p>
		</fr:form>
	</logic:notPresent>
</logic:present>
