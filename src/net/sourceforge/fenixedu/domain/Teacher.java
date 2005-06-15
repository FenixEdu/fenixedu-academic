package net.sourceforge.fenixedu.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;

/**
 * @author EP15
 * @author Ivo Brandão
 */
public class Teacher extends Teacher_Base {
    private Map creditsMap = new HashMap();

    public String toString() {
        String result = "[Dominio.Teacher ";
        result += ", teacherNumber=" + getTeacherNumber();
        result += ", person=" + getPerson();
        result += ", category= " + getCategory();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITeacher) {
            resultado = getTeacherNumber().equals(((ITeacher) obj).getTeacherNumber());
        }
        return resultado;
    }

    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod) {
        InfoCredits credits = (InfoCredits) creditsMap.get(executionPeriod);
        if (credits == null) {
            credits = InfoCreditsBuilder.build(this, executionPeriod);
            creditsMap.put(executionPeriod, credits);
        }
        return credits;
    }

    public void notifyCreditsChange(CreditsEvent creditsEvent, ICreditsEventOriginator originator) {
        Iterator iterator = this.creditsMap.keySet().iterator();
        while (iterator.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iterator.next();
            if (originator.belongsToExecutionPeriod(executionPeriod)) {
                iterator.remove();
            }
        }
    }

}
