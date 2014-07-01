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

	<bean:define id="evalText" name="executionCourse" property="localizedEvaluationMethodText"/>
	<logic:notEmpty name="evalText">
	<div class="mtop1 coutput2" style="line-height: 1.5em;">
			<fr:view name="evalText">
				<fr:layout>
					<fr:property name="escaped" value="false" />
					<fr:property name="newlineAware" value="true" />
				</fr:layout>
			</fr:view>
		</div>
	</logic:notEmpty>	
	<logic:empty name="evalText">
		<p>
			<em><bean:message key="message.evaluation.not.available"/></em>
		</p>
	</logic:empty>
