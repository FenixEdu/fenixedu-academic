/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    public TestScope() {
        setRootDomainObject(Bennu.getInstance());
    }

    public TestScope(ExecutionCourse executionCourse) {
        this();
        setExecutionCourse(executionCourse);
    }

    public static List<DistributedTest> readDistributedTestsByTestScope(ExecutionCourse executionCourse) {
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        TestScope testScope = executionCourse.getTestScope();
        if (testScope != null) {
            result.addAll(testScope.getDistributedTests());
        }
        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Test> getTests() {
        return getTestsSet();
    }

    @Deprecated
    public boolean hasAnyTests() {
        return !getTestsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.DistributedTest> getDistributedTests() {
        return getDistributedTestsSet();
    }

    @Deprecated
    public boolean hasAnyDistributedTests() {
        return !getDistributedTestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
