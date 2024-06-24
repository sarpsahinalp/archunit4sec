package aspectj;

public aspect LoggingAspect {
    pointcut helloPointcut(): execution(* aspectj.HelloWorld.sayHello(..));

    before(): helloPointcut() {
        System.out.println("About to say hello");
    }
}
