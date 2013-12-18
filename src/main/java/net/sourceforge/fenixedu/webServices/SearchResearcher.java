package net.sourceforge.fenixedu.webServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.ResearcherDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.research.Researcher;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.webServices.exceptions.NotAuthorizedException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.xfire.MessageContext;
import org.fenixedu.bennu.core.domain.Bennu;

public class SearchResearcher implements ISearchResearcher {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername = FenixConfigurationManager.getConfiguration().getWebServicesPaymentManagementUsername();
        storedPassword = FenixConfigurationManager.getConfiguration().getWebServicesPaymentManagementPassword();
    }

    @Override
    public ResearcherDTO[] searchByName(String username, String password, String name, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);

        Collection<Person> result = Person.findInternalPersonByNameAndRole(name, RoleType.RESEARCHER);

        List<Researcher> results = new ArrayList<Researcher>();
        for (Person person : result) {
            results.add(person.getResearcher());
        }
        Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);

        return getArrayFromResearchersList(results);
    }

    @Override
    public ResearcherDTO[] searchByKeyword(String username, String password, String keywords, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);

        if (keywords != null) {
            String[] keywordsArray = filterKeywords(keywords.split(" "));

            List<Researcher> results = new ArrayList<Researcher>();
            for (Researcher researcher : Bennu.getInstance().getResearchersSet()) {
                if (researcher.getAllowsToBeSearched() && researcher.hasAtLeastOneKeyword(keywordsArray)) {
                    results.add(researcher);
                }
            }
            Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
            return getArrayFromResearchersList(results);
        }

        return new ResearcherDTO[0];
    }

    @Override
    public ResearcherDTO[] getAvailableResearchers(String username, String password, MessageContext context)
            throws NotAuthorizedException {
        checkPermissions(username, password, context);

        List<Researcher> results = new ArrayList<Researcher>();
        for (Researcher researcher : Bennu.getInstance().getResearchersSet()) {
            if (researcher.getPerson() != null && researcher.getAllowsToBeSearched()) {
                results.add(researcher);
            }
        }
        Collections.sort(results, Researcher.PUBLICATION_VOLUME_COMPARATOR);
        return getArrayFromResearchersList(results);

    }

    private static final int MIN_KEYWORD_LENGTH = 1;

    private String[] filterKeywords(String[] keywords) {
        Collection<String> keywordsList = Arrays.asList(keywords);
        CollectionUtils.filter(keywordsList, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((String) arg0).length() > MIN_KEYWORD_LENGTH;
            }
        });

        return keywordsList.toArray(new String[0]);
    }

    private ResearcherDTO[] getArrayFromResearchersList(List<Researcher> results) {
        ResearcherDTO[] returnResults = new ResearcherDTO[results.size()];
        int i = 0;
        for (Researcher researcher : results) {
            returnResults[i++] = new ResearcherDTO(researcher);
        }

        return returnResults;
    }

    private void checkPermissions(String username, String password, MessageContext context) throws NotAuthorizedException {
        // check user/pass
        if (!storedUsername.equals(username) || !storedPassword.equals(password)) {
            throw new NotAuthorizedException();
        }

        // check hosts accessing this service
        // FIXME Anil: Its urgent to access this webservice for tests
        // if (!FenixConfigurationManager.getHostAccessControl().isAllowed(this, (ServletRequest)
        // context.getProperty("XFireServletController.httpServletRequest"))) {
        // throw new NotAuthorizedException();
        // }
    }

}
