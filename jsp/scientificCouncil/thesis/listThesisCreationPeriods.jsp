<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="scientificCouncil"/></em>
<h2><bean:message key="title.scientificCouncil.thesis.manage.thesis.creation.period"/></h2>



<logic:present name="thesisCreationPeriodFactoryExecutor">
	<logic:notPresent name="prepareDefineCreationPeriods">
    	<fr:form action="/scientificCouncilManageThesis.do?method=listThesisCreationPeriods">
	    	<fr:edit id="thesisCreationPeriodFactoryExecutor" name="thesisCreationPeriodFactoryExecutor" schema="scientificCouncil.thesis.creation.period.context.bean">
	        	<fr:layout name="tabular">
	            	<fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	        	    <fr:property name="columnClasses" value=",,tdclear"/>
	    	    </fr:layout>
		    </fr:edit>
	 	</fr:form>

		<logic:present name="thesisCreationPeriodFactoryExecutor" property="executionYear">
			<logic:notPresent name="thesisCreationPeriodFactoryExecutor" property="executionDegree">

				<bean:define id="executionYearId" name="thesisCreationPeriodFactoryExecutor" property="executionYear.idInternal"/>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=prepareDefineCreationPeriods&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>

				<html:link page="<%= url %>">
					<bean:message key="link.thesis.set.periods"/>
				</html:link>

   				<fr:view name="executionDegrees" schema="scientificCouncil.thesis.creationPeriod">
       				<fr:layout name="tabular">
           				<fr:property name="classes" value="tstyle1"/>

            			<fr:property name="link(change)" value="<%= String.format("/scientificCouncilManageThesis.do?method=prepareDefineCreationPeriods&amp;executionYearId=%s", executionYearId) %>"/>
            			<fr:property name="key(change)" value="link.thesis.set.period"/>
            			<fr:property name="param(change)" value="idInternal/executionDegreeId"/>
			            <fr:property name="order(change)" value="1"/>

           			</fr:layout>
           		</fr:view>

			</logic:notPresent>
		</logic:present>
	</logic:notPresent>

	<logic:present name="prepareDefineCreationPeriods">
		<bean:message key="label.executionYear"/>:<bean:write name="thesisCreationPeriodFactoryExecutor" property="executionYear.year"/>
		<br/>
		<logic:present name="thesisCreationPeriodFactoryExecutor" property="executionDegree">
			<bean:message key="label.executionDegree"/>:<bean:write name="thesisCreationPeriodFactoryExecutor" property="executionDegree.degreeCurricularPlan.degree.presentationName"/> (<bean:write name="thesisCreationPeriodFactoryExecutor" property="executionDegree.degreeCurricularPlan.degree.presentationName"/>)
		</logic:present>
		<logic:notPresent name="thesisCreationPeriodFactoryExecutor" property="executionDegree">
			<bean:message key="label.all.executionDegrees"/>
		</logic:notPresent>

	    <fr:form action="/scientificCouncilManageThesis.do?method=defineCreationPeriods">
		    <fr:edit id="thesisCreationPeriodFactoryExecutor" name="thesisCreationPeriodFactoryExecutor" schema="scientificCouncil.thesis.creation.period.definition">
	    	    <fr:layout name="tabular">
	        	    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	            	<fr:property name="columnClasses" value=",,tdclear"/>
	        	</fr:layout>
	    	</fr:edit>

            <html:submit>
                <bean:message key="button.submit"/>
            </html:submit>
            <html:cancel>
                <bean:message key="button.cancel"/>
            </html:cancel>
 		</fr:form>
	</logic:present>

</logic:present>

