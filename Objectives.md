# Objectives

* [ ] Prevents unrestricted access to the file system
* [ ] Prevents command execution
* [ ] Prevents usage of reflective programming
* [ ] Prevents network connections
* [ ] Prevents creating threads
* [ ] Prevents modifying the Security Manager
* [ ] Prevents modifying SSL
* [ ] Prevents modifying authentication
* [ ] Prevents modifying serialisation
* [ ] Prevents modifying management
* [ ] Prevents utilising AWT
* [ ] Prevents bad usage of threads
* [ ] Prevents terminating the test process
* [ ] Prevents unallowed linking libraries
* [ ] Prevents unallowed usage of printer
* [ ] Prevents unallowed usage of classloader
* [ ] Prevents unallowed definitions in packages
* [ ] Prevents unallowed package access

System exit calls!!!
```java
JavaMethodCall{origin=JavaMethod{java.lang.System.exit(int)}, target=target{java.lang.Runtime.exit(int)}, lineNumber=1864}
JavaMethodCall{origin=JavaMethod{java.lang.System.exit(int)}, target=target{java.lang.Runtime.getRuntime()}, lineNumber=1864}
```