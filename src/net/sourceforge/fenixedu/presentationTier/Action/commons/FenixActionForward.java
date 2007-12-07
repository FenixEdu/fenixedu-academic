package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;

import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;

public class FenixActionForward extends ActionForward {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;

	public FenixActionForward(HttpServletRequest request, ActionForward forward) {
		super(forward);
		
		this.request = request;
	}

	@Override
	public String getPath() {
		String current = super.getPath();
		
		String mark = "";
		if (current.indexOf("?") == -1) {
			mark = "?";
		}
		
		String amp = "";
		if (mark.length() == 0) {
			amp = "&";
		}
		
		String module = getPathModule();

		String context = request.getContextPath();
		String checksum = ChecksumRewriter.calculateChecksum(context + module + current);
		
		return String
				.format("%s%s%s%s=%s", current, mark, amp, ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME, checksum);
	}

	private String getPathModule() {
		String currentModule = getModule();
		
		if (currentModule != null) {
			return currentModule;
		}
		
		ModuleConfig module = ModuleUtils.getInstance().getModuleConfig(this.request);
		if (module == null) {
			return "";
		}
		
		return module.getPrefix();
	}
	
}
