package com.taoswork.tallybook.general.web.view.thymeleaf;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class TallyBookThymeleafServletContextTemplateResolver extends
        ServletContextTemplateResolver {

    @Override
    public IResourceResolver getResourceResolver() {
        // TODO Auto-generated method stub
        return super.getResourceResolver();
    }

    @Override
    public TemplateResolution resolveTemplate(
            TemplateProcessingParameters templateProcessingParameters) {
        // TODO Auto-generated method stub
        return super.resolveTemplate(templateProcessingParameters);
    }

    @Override
    protected boolean computeResolvable(
            TemplateProcessingParameters templateProcessingParameters) {
        // TODO Auto-generated method stub
        return super.computeResolvable(templateProcessingParameters);
    }

}
