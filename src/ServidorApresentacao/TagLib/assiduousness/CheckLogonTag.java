package ServidorApresentacao.TagLib.assiduousness;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import constants.assiduousness.Constants;

/**
 * Check for a valid User logged on in the current session. If there is no such
 * user, forward control to the logon page.
 * 
 * @author Craig R. McClanahan
 * @author Marius Barduta
 * @version $Revision$ $Date$
 */

public final class CheckLogonTag extends TagSupport {

    // --------------------------------------------------- Instance Variables

    /**
     * The key of the session-scope bean we look for.
     */
    private String name = Constants.USERNAME;

    /**
     * The page to which we should forward for the user to log on.
     */
    private String page = "../loginPage.jsp";

    // ----------------------------------------------------------- Properties

    /**
     * Return the bean name.
     */
    public String getName() {

        return (this.name);
    }

    /**
     * Set the bean name.
     * 
     * @param name
     *            The new bean name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the forward page.
     */
    public String getPage() {

        return (this.page);

    }

    /**
     * Set the forward page.
     * 
     * @param page
     *            The new forward page
     */
    public void setPage(String page) {

        this.page = page;

    }

    // ------------------------------------------------------- Public Methods

    /**
     * Defer our checking until the end of this tag is encountered.
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        return (SKIP_BODY);

    }

    /**
     * Perform our logged-in user check by looking for the existence of a
     * session scope bean under the specified name. If this bean is not present,
     * control is forwarded to the specified logon page.
     * 
     * @exception JspException
     *                if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Is there a valid user logged on?
        boolean valid = false;
        HttpSession session = pageContext.getSession();
        if ((session != null) && (session.getAttribute(name) != null))
            valid = true;

        // Forward control based on the results
        if (valid) {
            return (EVAL_PAGE);
        }

        try {
            pageContext.forward(page);
        } catch (Exception e) {
            throw new JspException(e.toString());
        }
        return (SKIP_PAGE);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        this.name = Constants.USERNAME;
        this.page = "../loginPage.jsp";

    }
}