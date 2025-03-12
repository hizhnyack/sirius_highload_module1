package ru.hpclab.hl.module1.context.find;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.hpclab.hl.module1.bean.FirstBean;

public class FindBeanInOurFirstContext {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("first-context.xml");
        FirstBean firstBeanByBeanName = (FirstBean) context.getBean("firstBean");// Try to find a bean in the context by bean name.
        FirstBean firstBeanByClass = context.getBean(FirstBean.class);// Try to find a bean in the context by class name.

        System.out.println("=============================");
        System.out.println("Greeting from the bean witch was found by name");
        System.out.println(firstBeanByBeanName.getGreeting());
        System.out.println("=============================");

        System.out.println("=============================");
        System.out.println("Greeting from the bean witch was found by class name");
        System.out.println(firstBeanByClass.getGreeting());
        System.out.println("=============================");
    }
}
