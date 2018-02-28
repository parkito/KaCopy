package ru.siksmfp.kacopy.cloning.impl;

import ru.siksmfp.kacopy.cloning.api.IDeepCloner;
import ru.siksmfp.kacopy.cloning.api.IFastCloner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Artem Karnov @date 3/1/2018.
 * @email artem.karnov@t-systems.com
 */
public class FastClonerCalendar implements IFastCloner {
    public Object clone(final Object t, final IDeepCloner cloner, final Map<Object, Object> clones) {
        final GregorianCalendar gc = new GregorianCalendar();
        Calendar c = (Calendar) t;
        gc.setTimeInMillis(c.getTimeInMillis());
        gc.setTimeZone((TimeZone) c.getTimeZone().clone());
        return gc;
    }
}
