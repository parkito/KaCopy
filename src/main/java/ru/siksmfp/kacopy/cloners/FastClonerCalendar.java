package ru.siksmfp.kacopy.cloners;

import ru.siksmfp.kacopy.api.IDeepCloner;
import ru.siksmfp.kacopy.api.IFastCloner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Artem Karnov @date 3/1/2018.
 * artyom-karnov@yandex.ru
 */
public class FastClonerCalendar implements IFastCloner {
    public Object clone(Object t, IDeepCloner cloner, Map<Object, Object> clones) {
        GregorianCalendar gc = new GregorianCalendar();
        Calendar c = (Calendar) t;
        gc.setTimeInMillis(c.getTimeInMillis());
        gc.setTimeZone((TimeZone) c.getTimeZone().clone());
        return gc;
    }
}
