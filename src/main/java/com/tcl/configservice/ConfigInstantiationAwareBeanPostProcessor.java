package com.tcl.configservice;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;

import com.tcl.configservice.annotation.ConfigCen;

public class ConfigInstantiationAwareBeanPostProcessor extends
	InstantiationAwareBeanPostProcessorAdapter{

	public boolean postProcessAfterInstantiation(final Object bean,
			String beanName) {
		
		ReflectionUtils.doWithFields(bean.getClass(),
			new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					if(field.isAnnotationPresent(ConfigCen.class)) {
						ConfigCen config = field.getAnnotation(ConfigCen.class);
						String key = config.name();
						if (Modifier.isStatic(field.getModifiers())) {
							throw new IllegalStateException(
									"Config annotation should not use on static Field.");
						}
						ReflectionUtils.makeAccessible(field);
						BeanInfoCache.add(field, key, bean);
					}
				}
			});
		return true;
	}
}
