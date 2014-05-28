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
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationDistributedTests extends InfoObject {

    private List<DistributedTest> distributedTestsToDo;

    private List<DistributedTest> distributedTestsDone;

    private Registration registration;

    public RegistrationDistributedTests(Registration registration, List<DistributedTest> distributedTestsToDo,
            List<DistributedTest> distributedTestsDone) {
        setRegistration(registration);
        setDistributedTestsToDo(distributedTestsToDo);
        setDistributedTestsDone(distributedTestsDone);
    }

    public List<DistributedTest> getDistributedTestsToDo() {
        return distributedTestsToDo;
    }

    public void setDistributedTestsToDo(List<DistributedTest> distributedTests) {
        this.distributedTestsToDo = distributedTests;
    }

    public List<DistributedTest> getDistributedTestsDone() {
        return distributedTestsDone;
    }

    public void setDistributedTestsDone(List<DistributedTest> distributedTests) {
        this.distributedTestsDone = distributedTests;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

}
