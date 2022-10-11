package com.example.buddyByte.bean;

public class Hello {

    public static String sayHello(){
        return "Hi from Hello!.";
    }

    public static String hello(String name) {
        Foo foo = new Foo();
        System.out.println("Calling a method : ");
        System.out.println(foo.sayFoo());
        return "Hello :-> " + name;
    }

    public static String intercept(String name) { return "Hello " + name + "!"; }
    public static String intercept(int i) { return Integer.toString(i); }
    public static String intercept(Object o) { return o.toString(); }
}
