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
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Susana Fernandes
 */
public class Question extends Question_Base {

    public Question() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setMetadata(null);
        setRootDomainObject(null);
        getStudentTestsQuestionsSet().clear();
        getTestQuestionsSet().clear();
        super.deleteDomainObject();
    }

    public Set<DistributedTest> findDistributedTests() {
        final Set<DistributedTest> distributedTests = new HashSet<DistributedTest>();
        for (final StudentTestQuestion studentTestQuestion : getStudentTestsQuestionsSet()) {
            distributedTests.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTests;
    }

    public List<SubQuestion> getSubQuestions() {
        return ParseSubQuestion.getSubQuestionFor(this);
    }

    // public SubQuestion getSubQuestionByItem(String itemId) {
    // for (SubQuestion subQuestion : getSubQuestions()) {
    // if (itemId != null && subQuestion.getItemId() != null) {
    // if (itemId.equals(subQuestion.getItemId())) {
    // return subQuestion;
    // }
    // } else if (itemId == null && subQuestion.getItemId() == null) {
    // return subQuestion;
    // }
    // }
    // return null;
    // }


}
