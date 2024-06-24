package aspectj;

public class HelloWorld {

    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.sayHello();
        System.exit(0);
    }

    public void sayHello() {
        System.out.println("Hello, World!");
    }
}
