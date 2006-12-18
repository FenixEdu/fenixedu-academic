package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;
import java.util.Map;

public interface AutoCompleteSearchService {
	/**
	 * @param type
	 *            the type of the object that we want to search. If the service
	 *            does not know how to search objects of the given type then it
	 *            should signal an exception
	 * @param value
	 *            the value given by the user
	 * @param limit
	 *            the number of intended results. The service may ignore this
	 *            value since the result of the service will filtered
	 *            automatically so that it never contains more elements than the
	 *            requested number. Nevertheless the service may use this value
	 *            to select the most relevant results or to optimize the search.
	 * @param arguments
	 *            a map with any extra arguments that were passed to the
	 *            service. This arguments can be used to configure a generic
	 *            service like, for example, choose the fields we are using for
	 *            the search.
	 */
	public List run(Class type, String value, int limit,
			Map<String, String> arguments);
}
