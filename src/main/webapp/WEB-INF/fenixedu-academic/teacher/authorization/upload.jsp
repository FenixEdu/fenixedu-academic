<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<spring:url var="showRevokedUrl" value="/teacher/authorizations/revoked"></spring:url>
<spring:url var="showCategoriesUrl" value="/teacher/authorizations/categories"></spring:url>
<spring:url var="uploadUrl" value="/teacher/authorizations/upload"></spring:url>
<spring:url var="showActiveAuthorizationsUrl" value="/teacher/authorizations"></spring:url>

<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title"/>
		<small>
			<spring:message code="teacher.authorizations.upload"/>
		</small>
	</h1>
</div>

<section>
	<div class="btn-group" role="group">
		<a class="btn btn-default" href="${showActiveAuthorizationsUrl}"><spring:message code="teacher.authorizations.view.current"/></a>
		<a class="btn btn-default" href="${showRevokedUrl}"><spring:message code="teacher.authorizations.view.revoked"/></a>
		<a class="btn btn-default active" href="${uploadUrl}"><spring:message code="teacher.authorizations.upload"/></a>
		<a class="btn btn-default" href="${showCategoriesUrl}"><spring:message code="teacher.categories"/></a>
	</div>
</section>
<hr />
<section>
	<div class="alert alert-info" role="alert">
		<spring:message code="teacher.authorizations.upload.message.header"/>
		<ol>
			<li>
				<spring:message code="teacher.authorizations.csv.column.1.username" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.upload.message.example" arguments="${currentUser.name};${currentUser.username}" argumentSeparator=";"/>
				</i>
			</li>
			<li>
				<spring:message code="teacher.authorizations.csv.column.2.categoryCode" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.upload.message.example" arguments="${categories[0].name.content};${categories[0].code}" argumentSeparator=";"/>
				</i>
				<ul>
					<c:forEach var="category" items="${categories}">
						<li><c:out value="${category.name.content} - ${category.code}"/></li>
					</c:forEach>
				</ul>
			</li>
			<li>
				<spring:message code="teacher.authorizations.csv.column.3.departmentAcronym" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.upload.message.example" arguments="${departments[0].nameI18n.content};${departments[0].acronym}" argumentSeparator=";"/>
				</i>
				<ul>
					<c:forEach var="department" items="${departments}">
						<li><c:out value="${department.acronym} - ${department.nameI18n.content}"/></li>
					</c:forEach>
				</ul>
			</li>
			<li>
				<spring:message code="teacher.authorizations.csv.column.4.lessonHours" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.csv.column.4.lessonHours.example"/>
				</i>
			</li>
			<li>
				<spring:message code="teacher.authorizations.csv.column.5.contracted" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.csv.column.5.contracted.example"/>
				</i>
			</li>
			<li>
				<spring:message code="teacher.authorizations.csv.column.6.workPercentageInInstitution" />
				<br />
				<i>
					<spring:message code="teacher.authorizations.csv.column.6.workPercentageInInstitution.example"/>
				</i>
			</li>
			
		</ol>
	</div>
</section>
<section>
	<form method="POST" class="form-horizontal" enctype="multipart/form-data">
		${csrf.field()}
		<div class="form-group">
			<label for="period" class="col-sm-1"><spring:message code="teacher.authorizations.period"/></label>
			<div class="col-sm-11">
				<select id="period" name="period" class="form-control">
					<c:forEach var="period" items="${periods}">
						<option value="${period.externalId}">${period.qualifiedName}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="file" class="col-sm-1"><spring:message code="teacher.authorizations.upload.file"/></label>
			<div class="col-sm-11">
				<input type="file" name="csv" id="file" class="form-control" required/>
			</div>
		</div>
		<button class="btn btn-primary"><spring:message code="teacher.authorizations.upload.submit"/></button>	
	</form>
</section>
