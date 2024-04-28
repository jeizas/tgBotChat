package com.jeizas.common.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The type Spring context util.

 */
@Component
public class SpringContext implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置上下文环境
     *
     * @param applicationContext 上线文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name the name
     * @return Object bean
     * @throws BeansException the beans exception
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }
}
