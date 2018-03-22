package ru.siksmfp.kacopy.instanter.types;

import ru.siksmfp.kacopy.instanter.strategy.StdInstantiatorStrategy;

/**
 * @author Artem Karnov @date 2/28/2018.
 * artyom-karnov@yandex.ru
 */
public class InstanterStd extends InstanterBase {

    public InstanterStd() {
        super(new StdInstantiatorStrategy());
    }

    public InstanterStd(boolean useCache) {
        super(new StdInstantiatorStrategy(), useCache);
    }
}
