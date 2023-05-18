package com.implemica.application.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Option.class)
public abstract class Option_ {

	public static volatile SingularAttribute<Option, Car> car;
	public static volatile SingularAttribute<Option, Long> id;
	public static volatile SingularAttribute<Option, String> optionName;

	public static final String CAR = "car";
	public static final String ID = "id";
	public static final String OPTION_NAME = "optionName";

}

