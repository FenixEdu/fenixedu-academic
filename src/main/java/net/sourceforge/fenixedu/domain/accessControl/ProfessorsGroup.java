package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

@GroupOperator("professors")
public final class ProfessorsGroup extends GroupStrategy {

    private static final long serialVersionUID = -7099165263747393201L;

    @Override
    public Set<User> getMembers() {
        return FluentIterable.from(Bennu.getInstance().getProfessorshipsSet()).transform(new Function<Professorship, User>() {
            @Override
            public User apply(Professorship input) {
                return input.getPerson().getUser();
            }
        }).filter(Predicates.notNull()).toSet();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && user.getPerson() != null && !user.getPerson().getProfessorshipsSet().isEmpty();
    }

}
