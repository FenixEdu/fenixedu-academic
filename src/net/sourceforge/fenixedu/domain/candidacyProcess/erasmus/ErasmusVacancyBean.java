package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

public class ErasmusVacancyBean implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private UniversityUnit university;
    private Country country;
    private Degree degree;

    private Integer numberOfVacancies;

    private ErasmusVacancy vacancy;

    public ErasmusVacancyBean() {

    }

    public ErasmusVacancyBean(ErasmusVacancy vacancy) {
	this.vacancy = vacancy;
    }

    public UniversityUnit getUniversity() {
        return university;
    }

    public void setUniversity(UniversityUnit university) {
        this.university = university;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Integer getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public void setNumberOfVacancies(Integer numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public ErasmusVacancy getVacancy() {
	return this.vacancy;
    }

    public void setVacancy(final ErasmusVacancy vacancy) {
	this.vacancy = vacancy;
    }
}