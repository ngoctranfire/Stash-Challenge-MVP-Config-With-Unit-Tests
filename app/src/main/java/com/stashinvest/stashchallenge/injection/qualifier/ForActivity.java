package com.stashinvest.stashchallenge.injection.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by ngoctranfire on 9/10/17.
 */
@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ForActivity {
}
