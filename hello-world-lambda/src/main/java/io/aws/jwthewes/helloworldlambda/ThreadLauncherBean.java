package io.aws.jwthewes.helloworldlambda;

import io.aws.jwthewes.lambdaextension.ExtensionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadLauncherBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLauncherBean.class);

    public ThreadLauncherBean() throws InterruptedException {
        LOGGER.info("Thread launcher bean created");
        new Thread(new ExtensionMain()).start();
    }
}
