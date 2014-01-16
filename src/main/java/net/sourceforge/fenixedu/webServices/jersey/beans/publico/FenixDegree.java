package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class FenixDegree {
    protected String id;
    protected String name;
    protected String acronym;
    protected List<String> academicTerms;

    private static List<String> getAcademicTerms(final Degree degree) {
        List<String> academicTerms =
                FluentIterable
                        .from(FluentIterable.from(degree.getExecutionDegrees())
                                .transform(new Function<ExecutionDegree, ExecutionYear>() {

                                    @Override
                                    public ExecutionYear apply(ExecutionDegree input) {
                                        return input.getExecutionYear();
                                    }
                                }).toSortedSet(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR))
                        .transform(new Function<ExecutionYear, String>() {

                            @Override
                            public String apply(ExecutionYear input) {
                                return input.getQualifiedName();
                            }
                        }).toList();
        return academicTerms;
    }

    public FenixDegree(Degree degree) {
        this(degree, false);
    }

    public FenixDegree(Degree degree, Boolean withAcademicTerms) {
        setId(degree.getExternalId());
        setName(degree.getName());
        setAcronym(degree.getSigla());
        setAcademicTerms(withAcademicTerms ? getAcademicTerms(degree) : null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    @JsonInclude(Include.NON_NULL)
    public List<String> getAcademicTerms() {
        return academicTerms;
    }

    public void setAcademicTerms(List<String> academicTerms) {
        this.academicTerms = academicTerms;
    }

}
