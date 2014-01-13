package pt.utl.ist.scripts.runOnce.delegates;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVote;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "SendVoteResults")
public class SendVoteResults extends CronTask {

    public static class VoteMap extends HashMap<Student, Set<DelegateElectionVote>> {

        private static final Comparator<Entry<Student, Set<DelegateElectionVote>>> RESULT_COMPARATOR =
                new Comparator<Entry<Student, Set<DelegateElectionVote>>>() {

                    @Override
                    public int compare(final Entry<Student, Set<DelegateElectionVote>> entry1,
                            final Entry<Student, Set<DelegateElectionVote>> entry2) {
                        final int count2 = entry1.getValue().size();
                        final int count1 = entry2.getValue().size();

                        final Integer number1 = entry1.getKey().getNumber();
                        final Integer number2 = entry2.getKey().getNumber();

                        return count1 < count2 ? -1 : (count1 == count2 ? number1.compareTo(number2) : 1);
                    }

                };

        public void put(final Student student, final DelegateElectionVote delegateElectionVote) {
            Set<DelegateElectionVote> delegateElectionVotes = get(student);
            if (delegateElectionVotes == null) {
                delegateElectionVotes = new HashSet<DelegateElectionVote>();
                put(student, delegateElectionVotes);
            }
            delegateElectionVotes.add(delegateElectionVote);
        }

        public SortedSet<Entry<Student, Set<DelegateElectionVote>>> getResults() {
            final SortedSet<Entry<Student, Set<DelegateElectionVote>>> results =
                    new TreeSet<Entry<Student, Set<DelegateElectionVote>>>(RESULT_COMPARATOR);
            results.addAll(entrySet());
            return results;
        }
    }

    private static final String fromName = "Comissão Eleitoral de Delegados de Ano";

    private static final String from = "ce-delegados-ano@mlists.ist.utl.pt";

    private static final String subjectPrefix = "Eleição de delegado de ano - Contagem dos votos - ";

    private static final String msgPrefix =
            "Caro(a) Aluno(a), \n\n"
                    + "Terminaram as votações para os Delegados de Ano, pelo que segue abaixo a contagem dos votos do ano e curso respectivos.\n\n\n";

    private static final String msgSuffix = "\n\nO Conselho Pedagógico irá agora, através da aplicação de critérios de "
            + "desempate definidos no Regulamento Eleitoral, proceder ao apuramento dos "
            + "resultados que serão divulgados o mais brevemente possível no website do "
            + "Conselho Pedagógico. Relembramos todos os alunos que forem eleitos que terão "
            + "que proceder à tomada de posse no prazo definido no Calendário Eleitoral. \n\n"
            + "Para mais informações sobre o processo eleitoral, consulta http://wwwcp.ist.utl.pt. \n\n"
            + "Obrigado pela participação, \n\n" + "A Comissão Eleitoral dos Delegados de Ano";

    @Override
    public void runTask() {

        for (final DelegateElection delegateElection : Bennu.getInstance().getDelegateElectionsSet()) {

            DelegateElectionVotingPeriod lastVotingPeriod = delegateElection.getLastVotingPeriod();

            if (delegateElection.getVotingPeriod() != null && lastVotingPeriod != null && lastVotingPeriod.isPastPeriod()
                    && !delegateElection.getSentResultsToCandidates().booleanValue()) {

                delegateElection.setSentResultsToCandidates(Boolean.TRUE);

                final Set<String> emails = new HashSet<String>();
                final VoteMap voteMap = new VoteMap();
                int maxNameSize = 0;

                for (final DelegateElectionVote delegateElectionVote : lastVotingPeriod.getVotesSet()) {
                    if (!delegateElectionVote.hasStudent()) {
                        continue;
                    }
                    final Student student = delegateElectionVote.getStudent();
                    final Person person = student.getPerson();
                    emails.add(person.getEmail());
                    voteMap.put(student, delegateElectionVote);
                    maxNameSize = Math.max(maxNameSize, person.getName().length());
                }

                final SortedSet<Entry<Student, Set<DelegateElectionVote>>> entrySet = voteMap.getResults();
                final StringBuilder stringBuilder = new StringBuilder(msgPrefix);

                write(stringBuilder, "Nº de Aluno", 0);
                stringBuilder.append("   |   ");
                write(stringBuilder, "Nome aluno", maxNameSize);
                stringBuilder.append("   |   ");
                stringBuilder.append("Nº Votos\n");

                for (final Entry<Student, Set<DelegateElectionVote>> entry : entrySet) {
                    final Student student = entry.getKey();
                    if (student == null) {
                        continue;
                    }
                    final Person person = student.getPerson();
                    final String name = StringFormatter.prettyPrint(person.getName());
                    final Set<DelegateElectionVote> votes = entry.getValue();
                    write(stringBuilder, student.getNumber().toString(), 11);
                    stringBuilder.append("   |   ");
                    write(stringBuilder, name, maxNameSize);
                    stringBuilder.append("   |   ");
                    stringBuilder.append(votes.size());
                    stringBuilder.append("\n");
                }

                stringBuilder.append(msgSuffix);
                final String msg = stringBuilder.toString();

                final String subject = subjectPrefix + delegateElection.getDegree().getSigla();

                new Email(fromName, from, null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, emails, subject, msg);
                final Set<String> toMe = new HashSet<String>();
                toMe.add("luis.cruz@ist.utl.pt");
                new Email(fromName, from, null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, toMe, subject, msg);

                System.out.println("Sent: " + emails.size() + " emails for: " + delegateElection.getDegree().getSigla());
            }
        }
    }

    private void write(final StringBuilder stringBuilder, final String string, int fillTo) {
        stringBuilder.append(string);
        for (int i = string.length(); i < fillTo; i++) {
            stringBuilder.append(' ');
        }
    }

}
