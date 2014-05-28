<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

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

				<bean:define id="executionYearId" name="thesisCreationPeriodFactoryExecutor" property="executionYear.externalId"/>
				<bean:define id="url">/scientificCouncilManageThesis.do?method=prepareDefineCreationPeriods&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>

				<html:link page="<%= url %>">
					<bean:message key="link.thesis.set.periods"/>
				</html:link>

   				<fr:view name="executionDegrees" schema="scientificCouncil.thesis.creationPeriod">
       				<fr:layout name="tabular">
           				<fr:property name="classes" value="tstyle1"/>

            			<fr:property name="link(change)" value="<%= String.format("/scientificCouncilManageThesis.do?method=prepareDefineCreationPeriods&amp;executionYearId=%s", executionYearId) %>"/>
            			<fr:property name="key(change)" value="link.thesis.set.period"/>
            			<fr:property name="param(change)" value="externalId/executionDegreeId"/>
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
		
		<bean:define id="executionYearId" name="thesisCreationPeriodFactoryExecutor" property="executionYear.externalId" />

	    <fr:edit id="thesisCreationPeriodFactoryExecutor" name="thesisCreationPeriodFactoryExecutor" 
	    		schema="scientificCouncil.thesis.creation.period.definition"
	    		action="/scientificCouncilManageThesis.do?method=defineCreationPeriods">
    	    <fr:layout name="tabular">
        	    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            	<fr:property name="columnClasses" value=",,tdclear error0"/>
        	</fr:layout>
        	
        	<fr:destination name="invalid" path="/scientificCouncilManageThesis.do?method=defineCreationPeriodsInvalid" />
        	<fr:destination name="cancel" path="<%="/scientificCouncilManageThesis.do?method=listThesisCreationPeriods&executionYearId=" + executionYearId.toString() %>" />
    	</fr:edit>
    	
	</logic:present>

</logic:present>

