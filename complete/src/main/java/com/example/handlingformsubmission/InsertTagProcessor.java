package com.example.handlingformsubmission;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.TemplateEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.thymeleaf.templatemode.TemplateMode;

public class InsertTagProcessor extends AbstractAttributeTagProcessor implements ApplicationContextAware {
    private static final String ATTR_NAME = "insert";
    private static final int PRECEDENCE = 10000;
    private ApplicationContext applicationContext;

    public InsertTagProcessor(final String dialectPrefix) {
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
            String property = tag.getAttributeValue("property");
            if (context instanceof IWebContext webCtx) {
                TemplateEngine templateEngine = applicationContext.getBean(TemplateEngine.class);
                String rendered = templateEngine.process(property, webCtx);
                String contextPath = "/myapp";
                if (contextPath != null && !contextPath.isEmpty()) {
                    // Thêm context-path cho src/href trong HTML
                    rendered = rendered.replaceAll("(src|href)=\"/(?!/)", "$1=\"" + contextPath + "/");
                    
                }
                structureHandler.replaceWith(rendered, false);
            } else {
                structureHandler.setBody("Context không phải IWebContext", false);
            }
        } catch (Exception e) {
            structureHandler.setBody("Lỗi khi load file: " + e.getMessage(), false);
        }
    }
}
