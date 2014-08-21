package pt.utl.ist.scripts.process.updateData.enrolment;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.ist.fenixframework.FenixFramework;

public class SendEmailToFlunkedStudents extends CustomTask {

    private static final String[] FLUNKED_STUDENTS = new String[] { "8306", "21121", "33939", "42200", "45004", "45662", "45778",
            "46349", "47077", "47178", "47597", "47928", "48516", "49080", "49646", "49671", "49877", "50144", "50312", "50526",
            "50683", "50879", "51110", "51331", "51469", "52109", "52327", "52516", "52533", "53035", "53566", "53818", "54184",
            "54424", "54794", "55196", "55220", "55410", "55459", "55817", "55819", "55878", "56545", "56631", "56804", "56849",
            "57123", "57287", "57301", "57349", "57642", "57675", "58030", "58041", "58086", "58205", "58229", "58356", "58366",
            "58392", "58500", "58534", "58705", "58965", "59095", "59099", "62665", "63110", "63238", "63387", "63495", "63546",
            "63703", "63791", "63984", "64216", "64663", "64674", "64725", "64945", "65052", "65264", "65283", "65371", "65545",
            "65592", "65779", "65975", "65985", "66034", "66073", "66089", "66133", "66168", "66244", "66462", "66485", "66717",
            "66918", "66931", "66996", "67016", "67045", "67200", "67326", "67367", "67563", "67688", "67754", "67833", "67907",
            "68140", "68156", "68339", "68620", "68623", "69273", "69477", "69727", "69737", "69790", "69872", "69889", "69904",
            "69936", "69965", "70009", "70034", "70044", "70060", "70069", "70113", "70146", "70151", "70164", "70178", "70294",
            "70296", "70419", "70428", "70450", "70550", "70647", "70714", "70919", "70950", "70957", "70961", "71010", "71036",
            "71071", "71121", "72516", "72538", "73434", "73713", "73757", "73834", "73858", "73934", "74066", "74142", "74243",
            "74282" };

    @Override
    public void runTask() throws Exception {
        User user = User.findByUsername("ist24616");
        Authenticate.mock(user);

        Set<Person> students = new HashSet<Person>();
        for (int iter = 0; iter < FLUNKED_STUDENTS.length; iter++) {

            Student student = Student.readStudentByNumber(Integer.valueOf(FLUNKED_STUDENTS[iter]));
            if (student == null) {
                taskLog("Can't find student -> " + FLUNKED_STUDENTS[iter]);
                continue;
            }
            students.add(student.getPerson());
        }

        createEmail(students);
        taskLog("Done.");
    }

    private void createEmail(final Set<Person> students) {

        final Sender sender = getConcelhoDeGestaoSender();

        final Set<Recipient> tos = new HashSet<Recipient>();
        tos.add(new Recipient(students));

        final Set<String> bccs = new HashSet<String>();
        bccs.add("marta.graca@tecnico.usliboa.pt");

        new Message(sender, null, tos, getSubject(), getBody(), bccs);
        taskLog("Sent: " + students.size() + " emails");
    }

    private Sender getConcelhoDeGestaoSender() {
        return FenixFramework.getDomainObject("4196183080395");
    }

    private String getBody() {
        final StringBuilder body = new StringBuilder();

        //  Mail quando os alunos são retirados da lista de prescristos 
//        body.append("Caro Aluno do IST,\n");
//        body.append("\n");
//        body.append("Após a reavaliação dos currículos académicos dos alunos prescritos em 16 de Agosto de 2013 (com base no lançamento de notas decorrido após esta data) e a apreciação dos recursos apresentados, verificou-se que o seu nome foi excluído da lista final de prescritos para 2013/2014.\n");
//        body.append("\n");
//        body.append("Assim, e se ainda não estiver inscrito em unidades curriculares no 1º semestre do ano lectivo 2013/14, poderá inscrever-se entre 10 e 13 de Setembro de 2013 na Internet (sistema Fénix) e no dia 16 de Setembro de 2013 nos Serviços Académicos.\n");
//        body.append("\n");
//        body.append("IST, 9 de Setembro de 2013\n");
//        body.append("\n");
//        body.append("O Conselho de Gestão do IST\n");

        //Mail quando são postos como prescritos
        body.append("Caro Aluno do IST,\n");
        body.append("\n");
        body.append("Após análise do seu currículo académico, verificou-se estar numa das situações previstas para prescrição no Regulamento de Prescrições do IST (disponível em http://www.ist.utl.pt/pt/alunos).\n");
        body.append("\n");
        body.append("A lista provisória de alunos a prescrever encontra-se afixada, junto dos Serviços Académicos, desde o dia 18 de agosto de 2014. Os alunos sujeitos a prescrição não poderão efectuar a sua inscrição em unidades curriculares no ano lectivo 2014/2015.\n");
        body.append("\n");
        body.append("No caso de existir alguma incorrecção na avaliação da sua situação, deverá requerer a sua correcção junto dos Serviços Académicos dentro dos prazos estabelecidos para recurso, de 18 de agosto a 22 de agosto de 2014.\n");
        body.append("\n");
        body.append("O resultado dos recursos será afixado no dia 8 de Setembro de 2014. No caso de deferimento a inscrição em unidades curriculares no 1º semestre do ano lectivo 2014/2015 poderá ser efectuada entre 9 e 12 de setembro de 2014.\n");
        body.append("\n");
        body.append("Técnico, 18 de Agosto de 2014\n");
        body.append("O Conselho de Gestão do IST\n");

        return body.toString();
    }

    private String getSubject() {
//        return "Levantamento de prescrição para o ano lectivo 2013/2014";
        return "Prescrição para o ano lectivo 2014/2015";
    }
}