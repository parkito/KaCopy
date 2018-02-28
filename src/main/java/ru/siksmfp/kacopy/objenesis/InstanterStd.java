package ru.siksmfp.kacopy.objenesis;

import ru.siksmfp.kacopy.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class InstanterStd extends InstanterBase {

    public InstanterStd() {
        super(new StdInstantiatorStrategy());
    }

    public InstanterStd(boolean useCache) {
        super(new StdInstantiatorStrategy(), useCache);
    }
}
