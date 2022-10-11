package com.example.buddyByte.controller;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.example.buddyByte.bean.Foo;
import com.example.buddyByte.bean.Hello;

import static net.bytebuddy.matcher.ElementMatchers.*;
/**
 * 
 * This class contains some methods in buddy byte
 *  *  
 */

public class BuddyByte {

	public  void createingClassAtRuntime() throws InstantiationException, IllegalAccessException {
		Class<?> unloadedType = new ByteBuddy()
				.subclass(Object.class)
				.name("createADynamicClass")
				.method(ElementMatchers.isToString())
				.intercept(FixedValue.value("Hello World !"))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded();

		System.out.println("Data : " + unloadedType.newInstance().toString());
		System.out.println("Class Name : " + unloadedType.getSimpleName());
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
	InvocationTargetException, NoSuchMethodException {

		BuddyByte bb = new BuddyByte();

		System.out.println("Creating at Run time");
		bb.createingClassAtRuntime();
		System.out.println("-----------------------");
		System.out.println("Dynamic Loading");
		bb.dynamicLoading();
		System.out.println("-----------------------");
		System.out.println("Method field Define ");
		bb.methodFieldDef();
		System.out.println("-----------------------");
		System.out.println("Method Re Define ");
		bb.redefine();
		System.out.println("-----------------------");
		System.out.println("After class redefine the value of foo remain hello");
		System.out.println("Calling from main method");
		Foo foo = new Foo();
		System.out.println(foo.sayFoo());
		System.out.println("-----------------------");
		System.out.println("Multiple Method ");
		bb.multipleMethod();
		System.out.println("-----------------------");
		System.out.println("Delegate Method ");
		bb.delegateMethod();
		System.out.println("-----------------------");
	}

	public void dynamicLoading() throws InstantiationException, IllegalAccessException {
		String r = new ByteBuddy()
				.subclass(Foo.class)
				.method(named("sayFoo")
						.and(isDeclaredBy(Foo.class))
						.and(returns(String.class)))
				.intercept(MethodDelegation.to(Hello.class))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance()
				.sayFoo();
		System.out.println("Say foo method in foo class will be changes into Hello");
		System.out.println(r);
	}

	public void methodFieldDef() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

		Class<?> type = new ByteBuddy()
				.subclass(Object.class)
				.name("My_Dynamically_created_Class")
				.defineMethod("myDynamicallyMethod", Integer.class, Modifier.PUBLIC)
				.intercept(MethodDelegation.to(BuddyByte.class))
				.defineField("x", String.class,Modifier.PUBLIC)
				.make()
				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
		Method m = type.getDeclaredMethod("myDynamicallyMethod",null);
		System.out.println("Data :-> " + m.invoke(type.newInstance()));
		System.out.println("Class name : " + type.getSimpleName());

	}

	public void redefine() {
		Foo f = new Foo();

		System.out.println("data Currently in foo -> " + f.sayFoo());
		ByteBuddyAgent.install();
		new ByteBuddy()
		.redefine(Foo.class)
		.method(named("sayFoo"))
		.intercept(FixedValue.value("The foo has been redefined"))
		.make()
		.load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
		;
		Foo foo = new Foo();
		System.out.println("The class foo has been redefined");
		System.out.println("data Currently in foo -> " + foo.sayFoo());

	}

	public void multipleMethod() throws InstantiationException, IllegalAccessException {
		Foo dynamic = new ByteBuddy()
				.subclass(Foo.class)
				.method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("Bar Method in foo"))
				.method(named("foo")).intercept(FixedValue.value("Foo Data in foo class foo method"))
				.method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Foo Method wit object"))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance();
		System.out.println("Bar method in foo : " + dynamic.bar());
		System.out.println("Foo method : " + dynamic.foo());
		System.out.println("Fo-> with argment" + dynamic.foo(1));

	}

	public void delegateMethod() throws InstantiationException, IllegalAccessException {
		String hai = new ByteBuddy()
				.subclass(Foo.class)
				.method(named("hello"))
				.intercept(MethodDelegation.to(Hello.class))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance()
				.hello("vipin");

		System.out.println("the data in hello will be changed to hello in Hello calss with argument");
		System.out.println(hai);

		String h = new ByteBuddy()
				.subclass(Foo.class)
				.method(named("intercept"))
				.intercept(MethodDelegation.to(Hello.class))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance()
				.intercept(1);
		System.out.println("Integer 1 : > " + h);
	}

	public static int hello(){
		return 1;
	}
}
