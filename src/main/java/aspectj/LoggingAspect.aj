package aspectj;

public aspect LoggingAspect {
    pointcut helloPointcut(): call(* java.lang.System.exit(..));

    before(): helloPointcut() {
        System.out.println("About to say hello");
        throw new SecurityException("System.exit() is not allowed");
    }
}
