package com.example.handlingformsubmission;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationContext;

@Component
public class MyDialect extends AbstractProcessorDialect {
    public MyDialect() {
        super("My Dialect", "my", 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<>();
        processors.add(new UppercaseAttributeTagProcessor(dialectPrefix));
        IncludeHtmlAttributeTagProcessor includeProcessor = new IncludeHtmlAttributeTagProcessor(dialectPrefix);
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        if (ctx != null) {
            includeProcessor.setApplicationContext(ctx);
            InsertTagProcessor insertProcessor = new InsertTagProcessor(dialectPrefix);
            insertProcessor.setApplicationContext(ctx);
            processors.add(insertProcessor);
        }
        processors.add(includeProcessor);
        processors.add(new MessagesNotPresentAttributeTagProcessor(dialectPrefix));
        processors.add(new MessagesPresentAttributeTagProcessor(dialectPrefix));
        return processors;
    }
}
