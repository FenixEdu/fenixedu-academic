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
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.EvaluationType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class OnlineTest extends OnlineTest_Base {

    public static List<OnlineTest> readOnlineTests() {
        List<OnlineTest> result = new ArrayList<OnlineTest>();

        for (Evaluation evaluation : Bennu.getInstance().getEvaluationsSet()) {
            if (evaluation instanceof OnlineTest) {
                result.add((OnlineTest) evaluation);
            }
        }

        return result;
    }

    public OnlineTest() {
        super();
        setGradeScale(GradeScale.TYPE20);
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.ONLINE_TEST_TYPE;
    }

    @Override
    public void delete() {
        logRemove();
        setDistributedTest(null);
        super.delete();
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getString(Bundle.APPLICATION, "label.online.test") + " "
                + getDistributedTest().getEvaluationTitle();
    }

    @Deprecated
    public boolean hasDistributedTest() {
        return getDistributedTest() != null;
    }

}
