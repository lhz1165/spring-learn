package com.lhz.context;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyEditor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: lhz
 * @date: 2020/9/24
 **/
@Configuration
public class PropertyEditorConfig {

    @Bean
    public static CustomEditorConfigurer getConfig() {
        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> map = new HashMap<>();
        map.put(Date.class,DatePropertyEditor.class);
        configurer.setCustomEditors(map);
        return configurer;
    }


}
