package com.example.handlingformsubmission;

import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.thymeleaf.templatemode.TemplateMode;

public class IncludeHtmlAttributeTagProcessor extends AbstractAttributeTagProcessor implements ApplicationContextAware {
    private static final String ATTR_NAME = "include";
    private static final int PRECEDENCE = 10000;
    private ApplicationContext applicationContext;
    private TemplateEngine templateEngine;

    public IncludeHtmlAttributeTagProcessor(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doProcess(
            ITemplateContext context, IProcessableElementTag tag,
            AttributeName attributeName, String attributeValue,
            IElementTagStructureHandler structureHandler) {

        try {
            String path = tag.getAttributeValue("path");

            if (context instanceof IWebContext webCtx) {
                // Lấy TemplateEngine từ ApplicationContext
                TemplateEngine templateEngine = applicationContext.getBean(TemplateEngine.class);
                String rendered = templateEngine.process(path, webCtx);
                structureHandler.replaceWith(rendered, false);
            } else {
                structureHandler.setBody("Context không phải IWebContext", false);
            }

        } catch (Exception e) {
            structureHandler.setBody("Lỗi khi load file: " + e.getMessage(), false);
        }
    }
}
