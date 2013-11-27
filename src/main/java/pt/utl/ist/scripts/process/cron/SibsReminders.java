package pt.utl.ist.scripts.process.cron;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "SibsReminders")
public class SibsReminders extends CronTask {

    @Override
    public void runTask() {
        final SystemSender systemSender = Bennu.getInstance().getSystemSender();
        new Message(systemSender, null, Collections.EMPTY_LIST, "SIBS - Pagamentos",
                "Deve ser efectuado uma importação dos pagamentos efectuados pela SIBS.", "suporte@dot.ist.utl.pt");
    }

}
