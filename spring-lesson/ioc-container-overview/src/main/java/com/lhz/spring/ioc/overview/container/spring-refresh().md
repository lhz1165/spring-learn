# spring refresh()的流程

1. prepareRefresh()      前期准备
2. prepareBeanFactory(beanFactory);     初始化beanfactory，加入内建的bean或飞bean对象
3. postProcessBeanFactory(beanFactory);    进一步初始化，自定义实现beanFactory
4. invokeBeanFactoryPostProcessors(beanFactory); ， 执行这些扩展
5.  registerBeanPostProcessors(beanFactory);     扩展bean
6. initMessageSource();   国际化
7. initApplicationEventMulticaster();     时间广播