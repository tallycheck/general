package com.taoswork.tallycheck.general.web.view.thymeleaf;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

public class CsrfProcessor extends AbstractAttrProcessor {
    //<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    public static final String ATTR_NAME = "csrf";

//	protected static final String CSRF_TOKEN_NAME = "${_csrf.parameterName}";
//	protected static final String CSRF_TOKEN_VALUE = "${_csrf.token}";

    public CsrfProcessor() {
        super(ATTR_NAME);
    }
    /*
    protected final ExploitProtectionService eps;

	public CsrfProcessor(ExploitProtectionService eps) {
		super(ATTR_NAME);
		this.eps = eps;
	}
	*/

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
        return null;
    }
//	@Override
//	protected ProcessorResult processAttribute(Arguments arguments,
//			Element element, String attributeName) {
//        final String attributeValue = element.getAttributeValue(attributeName);
//
//        final Configuration configuration = arguments.getConfiguration();
//        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
//
//        final IStandardExpression expression = expressionParser.parseExpression(configuration, arguments, attributeValue);
//        final Object value = expression.execute(configuration, arguments);
//
// //       try {
////			String csrfToken = eps.getCSRFToken((String)(value));
//			Element csrfNode = new Element("input");
//			csrfNode.setAttribute("type", "hidden");
//			csrfNode.setAttribute("th:name", CSRF_TOKEN_NAME);
//			csrfNode.setAttribute("th:value", CSRF_TOKEN_VALUE);
//		element.addChild(csrfNode);
//
//		element.removeAttribute(attributeName);
//		return ProcessorResult.OK;
//	}

    @Override
    public int getPrecedence() {
        return 1000;
    }

}
