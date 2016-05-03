package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Relationship {

	String reverse();
	String idField() default "id";
}
