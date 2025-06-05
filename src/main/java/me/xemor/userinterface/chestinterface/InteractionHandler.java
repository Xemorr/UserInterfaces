package me.xemor.userinterface.chestinterface;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InteractionHandler {

    char slot();

}
