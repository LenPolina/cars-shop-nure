package com.implemica.application.domain;

import com.implemica.application.domain.enumeration.BodyType;
import com.implemica.application.domain.enumeration.Brand;
import com.implemica.application.domain.enumeration.GearBoxType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Car.class)
public abstract class Car_ {

	public static volatile SingularAttribute<Car, GearBoxType> carGearboxType;
	public static volatile SingularAttribute<Car, Brand> carBrand;
	public static volatile SingularAttribute<Car, String> carImageUrl;
	public static volatile SingularAttribute<Car, BodyType> carBodyType;
	public static volatile SingularAttribute<Car, Float> carEngineVolume;
	public static volatile SetAttribute<Car, Option> options;
	public static volatile SingularAttribute<Car, Long> id;
	public static volatile SingularAttribute<Car, String> carDescription;
	public static volatile SingularAttribute<Car, Integer> carYear;
	public static volatile SingularAttribute<Car, String> carModel;

	public static final String CAR_GEARBOX_TYPE = "carGearboxType";
	public static final String CAR_BRAND = "carBrand";
	public static final String CAR_IMAGE_URL = "carImageUrl";
	public static final String CAR_BODY_TYPE = "carBodyType";
	public static final String CAR_ENGINE_VOLUME = "carEngineVolume";
	public static final String OPTIONS = "options";
	public static final String ID = "id";
	public static final String CAR_DESCRIPTION = "carDescription";
	public static final String CAR_YEAR = "carYear";
	public static final String CAR_MODEL = "carModel";

}

