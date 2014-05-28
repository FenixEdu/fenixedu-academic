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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message key="title.evaluationMethod"/>
</h2>

<logic:notEmpty name="executionCourse" property="evaluationMethod">
	<logic:notEmpty name="executionCourse" property="evaluationMethod.evaluationElements">
		<div class="mtop1 coutput2" style="line-height: 1.5em;">
		<fr:view name="executionCourse" property="evaluationMethod.evaluationElements" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString">
			<fr:layout>
				<fr:property name="escaped" value="false" />
				<fr:property name="newlineAware" value="false" />
			</fr:layout>
		</fr:view>
	</div>
	</logic:notEmpty>

	<logic:empty name="executionCourse" property="evaluationMethod.evaluationElements">
		<logic:notEmpty name="executionCourse" property="evaluationMethodText" >
		<div class="mtop1 coutput2" style="line-height: 1.5em;">
				<fr:view name="executionCourse" property="evaluationMethodText">
					<fr:layout>
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</div>
		</logic:notEmpty>	
		<logic:empty name="executionCourse" property="evaluationMethodText">
			<p>
				<em><bean:message key="message.evaluation.not.available"/></em>
			</p>
		</logic:empty>
	</logic:empty>
</logic:notEmpty>


<logic:empty name="executionCourse" property="evaluationMethod">
	<logic:notEmpty name="executionCourse" property="evaluationMethodText" >
	<div class="mtop1 coutput2" style="line-height: 1.5em;">
			<fr:view name="executionCourse" property="evaluationMethodText">
				<fr:layout>
					<fr:property name="escaped" value="false" />
					<fr:property name="newlineAware" value="true" />
				</fr:layout>
			</fr:view>
		</div>
	</logic:notEmpty>	
	<logic:empty name="executionCourse" property="evaluationMethodText">
		<p>
			<em><bean:message key="message.evaluation.not.available"/></em>
		</p>
	</logic:empty>
</logic:empty>
