package ru.hpclab.hl.module1.context.find;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.hpclab.hl.module1.bean.FirstBean;
import ru.hpclab.hl.module1.bean.SecondBean;

public class FindBeanInOurSecondContext {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("second-context.xml");
        FirstBean firstBeanByBeanName = (FirstBean) context.getBean("firstBean");// Try to get a bean by name from the privios example.
        FirstBean firstBeanByClass = context.getBean(SecondBean.class);// Try to find a bean in the context by class name.

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
