## TODO List

- Problems:
    - [ ] ArchUnit can only check the Classes included in the build, students should not be able to access the classes that are not included in the build. &rarr; This already seems to be prohibited when the assignmentSrcDir is set in the build.gradle file of the Test repo
    - [ ] When checking for Transitive Dependencies, all classes are included such as java.lang.Object, java.lang.String which use java.io or java.lang.reflect, which are detected when we prohibit reflection
    - [ ] Tests are easily configurable via conf.json which is then passed to the disabledIf field