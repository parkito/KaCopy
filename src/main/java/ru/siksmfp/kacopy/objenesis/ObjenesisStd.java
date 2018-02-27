package ru.siksmfp.kacopy.objenesis;

import ru.siksmfp.kacopy.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author Artem Karnov @date 2/28/2018.
 * @email artem.karnov@t-systems.com
 */
public class ObjenesisStd extends ObjenesisBase {

    public ObjenesisStd() {
        super(new StdInstantiatorStrategy());
    }

    public ObjenesisStd(boolean useCache) {
        super(new StdInstantiatorStrategy(), useCache);
    }
}
