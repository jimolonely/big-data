package com.jimo.annotation;

@interface Source {
	public String URL();

	public String mail() default "";
}
