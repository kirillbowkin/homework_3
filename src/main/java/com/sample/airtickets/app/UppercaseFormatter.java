package com.sample.airtickets.app;

import io.jmix.ui.component.formatter.Formatter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("ui_UppercaseFormatter")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UppercaseFormatter implements Formatter<String> {
    @Override
    public String apply(String value) {
        return value != null
                ? value.toUpperCase()
                : null;
    }
}
