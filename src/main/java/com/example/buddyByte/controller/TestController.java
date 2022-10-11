package com.example.buddyByte.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping(value = "" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public Object testByteBuddy() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		Class<?> type =  new ByteBuddy()
				.subclass(Object.class)
				.name("TestBean")
				.defineConstructor(Modifier.PUBLIC)
				.withParameters(String.class,Integer.class,String.class,String.class)
				.intercept(MethodCall
						.invoke(Object.class.getConstructor())
						.andThen(FieldAccessor
								.ofField("name")
								.setsArgumentAt(0)
						).andThen(FieldAccessor
								.ofField("age")
								.setsArgumentAt(1)
						).andThen(FieldAccessor
								.ofField("id")
								.setsArgumentAt(2)
						).andThen(FieldAccessor
								.ofField("phone")
								.setsArgumentAt(3)
						)
				)
				.defineField("name", String.class, Modifier.PUBLIC)
				.defineField("age", Integer.class, Modifier.PUBLIC)
				.defineField("id", String.class, Modifier.PUBLIC)
				.defineField("phone", String.class, Modifier.PUBLIC)
				.make()
				.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();

//		Field field = type.getDeclaredField("name");
//		field.setAccessible(true);


		Object newInstance = type.getConstructor(String.class,Integer.class,String.class,String.class)
				.newInstance("hello",22,"id001","12345678910");

		return newInstance;
	}




}