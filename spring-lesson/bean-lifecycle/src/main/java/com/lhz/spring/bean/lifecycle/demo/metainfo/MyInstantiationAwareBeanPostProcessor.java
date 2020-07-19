package com.lhz.spring.bean.lifecycle.demo.metainfo;

import com.lhz.spring.ioc.domain.SuperUser;
import com.lhz.spring.ioc.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.util.ObjectUtils;

/**
 * @author lhzlhz
 * @create 2020/7/19
 */
class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    /**
     *
     * createBean-->resolveBeforeInstantiation(beanName, mbdToUse);
     * postProcessBeforeInstantiation和postProcessAfterInstantiation一起调用的
     * 如果有对象返回那么直接返回createBean 不会执行doCreateBean()
     * 直接跳过spring的实例化阶段 用我们的实例化
     *
     *
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("superUser2", beanName)&& SuperUser.class.equals(beanClass)) {
            //覆盖掉xml里面的对象
            SuperUser superUser = new SuperUser();
            superUser.setId(33);
            superUser.setName("postProcessBeforeInstantiationUser");
            return superUser;
        }
        return null;//保持ioc容器的操作
    }

    /**
     * doCreateBean(beanName, mbdToUse, args)-->populateBean(beanName, mbd, instanceWrapper)
     * 1384行 调用了这个方法postProcessAfterInstantiation()
     *
     * 如果返回false的话会继续执行
     * 1444行 applyPropertyValues(beanName, mbd, bw, pvs); 来设置配置文件里面的属性 而不采用此方法的赋值属性
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("user2", beanName)&& User.class.equals(bean.getClass())) {
            User bean1 = (User) bean;
            bean1.setId(2);
            //xml里面的配置元信息忽略
            return true;
        }
        return true;
    }

    /**
     *
     * doCreateBean(beanName, mbdToUse, args)-->populateBean(beanName, mbd, instanceWrapper)
     * 1491 行执行postProcessProperties()
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("userHolder2", beanName)&&UserHolder.class.equals(bean.getClass())) {
            //<property name="number" value="22"/> 配置的话 PropertyValues包含这个数据
            //使用api操作
            // !!!!!返回了会忽略到xml的配置使用返回的配置
            MutablePropertyValues propertyValues;
            if (pvs instanceof MutablePropertyValues) {
                propertyValues = (MutablePropertyValues) pvs;
            }else {
                propertyValues = new MutablePropertyValues();
            }

            //如果存在修改
            if (propertyValues.contains("desc")) {
                propertyValues.getPropertyValues();
                propertyValues.removePropertyValue("desc");
                propertyValues.addPropertyValue("desc","postProcessProperties");

            }
            propertyValues.addPropertyValue("number",222);
            //xml里面的配置所有元信息忽略
            return propertyValues;
        }
        return null;
    }

    /**
     *
     * doCreateBean(beanName, mbdToUse, args)-->initializeBean(beanName, exposedObject, mbd)-->applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
     * initializeBean方法就在populateBean下一行
     * 第416行执行此方法，他的修改会覆盖  postProcessProperties（）
     *
     * 并且这里面是先执行aware的回调 再 postProcessBeforeInitialization，再postProcessAfterInitialization
     *
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("userHolder2", beanName)&&UserHolder.class.equals(bean.getClass())) {
            UserHolder userHolder = (UserHolder) bean;
            userHolder.setDesc("postProcessBeforeInitializationUserHolder");
            //这里反回null 或者bean 都会修改bean的引用 所以 暂时我发现效果一样？？？ 不知道有啥区别
            //返回null不会执行第二个postProcessBeforeInitialization重载方法了，所以不能返回null【例如@psotContruct】
            return bean;
        }
        return bean;
    }

    /**
     * bean初始化的最后一个阶段
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("userHolder2", beanName)&&UserHolder.class.equals(bean.getClass())) {
            UserHolder userHolder = (UserHolder) bean;
            userHolder.setDesc("postProcessAfterInitialization");
            return bean;
        }
        return bean;
    }
}
