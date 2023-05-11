package io.aws.jwthewes.helloworldvanillalambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.aws.jwthewes.helloworldvanillalambda.lambdaextension.ExtensionMain;

public class HelloWorldVanillaLambda implements RequestHandler<String, String> {

    static {
        new Thread(new ExtensionMain()).start();
    }

    @Override
    public String handleRequest(String s, Context context) {
        return new StringBuilder(s).reverse().toString();
    }
}
