package edu.escuelaing.arep.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * @author valentina.siabatto
 *
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Web {
	 String value();
}