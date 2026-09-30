package com.github.regyl.unfriendlyjarvis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "LoggingBeanPostProcessor")
@Component
public class LoggingBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    private final Map<String, Long> beanMap = new HashMap<>();

    @Value("${application.development.critical-beans}")
    private List<String> criticalBeanNames;

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (criticalBeanNames.contains(beanName)) {
            log.warn("Bean {} was destroyed", beanName);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        beanMap.put(beanName, System.nanoTime());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        long startupTimeMs = (System.nanoTime() - beanMap.get(beanName)) / 1_000_000;
        if (startupTimeMs >= 5) {
            log.warn("Bean {} was created in {} ms", beanName, startupTimeMs);
        }
        return bean;
    }
}
