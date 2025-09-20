package com.example.handlingformsubmission;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
import java.util.Collection;

public class MessagesNotPresentAttributeTagProcessor extends AbstractAttributeTagProcessor {
    private static final String ATTR_NAME = "messagesNotPresent";
    private static final int PRECEDENCE = 10000;

    public MessagesNotPresentAttributeTagProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateContext context,
            IProcessableElementTag tag,
            AttributeName attributeName,
            String attributeValue,
            IElementTagStructureHandler structureHandler) {

        // Không truyền value => mặc định lấy "errors"
        Object errors = context.getVariable("errors");

        boolean noMessages = (errors == null
                || (errors instanceof java.util.Collection && ((java.util.Collection<?>) errors).isEmpty()));

        if (noMessages) {
            // Không có messages => giữ element, chỉ bỏ attribute custom
            structureHandler.removeAttribute(attributeName);
        } else {
            // Có messages => bỏ element
            structureHandler.removeElement();
        }
    }
}
