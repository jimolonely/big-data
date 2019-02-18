package com.jimo.variance;

import java.util.Arrays;
import java.util.List;

/**
 * @author jimo
 * @date 19-2-18 下午3:39
 */
public class CovariantTest {

	static abstract class Animal {
		public abstract String name();
	}

	static class Cat extends Animal {

		@Override
		public String name() {
			return "cat";
		}
	}

	public static void main(String[] args) {
		Cat c1 = new Cat();
		List<Cat> cats = Arrays.asList(c1, c1);
//		printAnimal(cats);
	}

	public static void printAnimal(List<Animal> animals) {
		for (Animal animal : animals) {
			System.out.println(animal.name());
		}
	}
}
