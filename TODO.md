## TODO List

- Problems:
    - [ ] ArchUnit can only check the Classes included in the build, students should not be able to access the classes that are not included in the build. &rarr; This already seems to be prohibited when the assignmentSrcDir is set in the build.gradle file of the Test repo
    - [ ] When checking for Transitive Dependencies, all classes are included such as java.lang.Object, java.lang.String which use java.io or java.lang.reflect, which are detected when we prohibit reflection
    - [ ] Tests are easily configurable via conf.json which is then passed to the disabledIf field

- Use Case Scenarios:
    - Create use case scenarios for instructors to better understand what should be done

Take a look at the below violations these were scraped after removing basic packages from dependency path

```java
Architecture Violation [Priority: MEDIUM] - Rule 'no classes should transitively depend on classes that depend on File class' was violated (30 times):
Class <de.tum.cit.ase.aspectj.CustomMidiDevice$2> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomMidiDevice$3> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomMidiDevice> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomSoundbankReader> depends on <java.io.File> in (CustomSoundbankReader.java:0)
Class <de.tum.cit.ase.aspectj.CustomSoundbankReader> transitively depends on <java.io.File> by [javax.sound.midi.spi.SoundbankReader->java.io.File] in (CustomSoundbankReader.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> depends on <java.io.File> in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [java.awt.BorderLayout->java.awt.Container->java.awt.LayoutManager->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JButton->javax.swing.AbstractButton->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JFileChooser->javax.swing.filechooser.FileFilter->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JFrame->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JScrollPane->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JTextArea->javax.swing.text.JTextComponent->javax.swing.JComponent->java.awt.Container->java.awt.LayoutManager->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.SwingUtilities->java.awt.Component->java.awt.Container->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.filechooser.FileNameExtensionFilter->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileInputStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileOutputStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileReader->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.RandomAccessFile->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.channels.FileChannel->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.file.Paths->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.file.spi.FileSystemProvider->java.nio.channels.FileChannel->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.util.Scanner->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [java.util.zip.ZipFile->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [javax.imageio.ImageIO->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample$1> depends on <java.io.File> in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample$1> transitively depends on <java.io.File> by [javax.sound.sampled.spi.AudioFileWriter->java.io.File] in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample> transitively depends on <java.io.File> by [java.util.zip.ZipFile->java.io.File] in (ZipReaderExample.java:0)
java.lang.AssertionError: Architecture Violation [Priority: MEDIUM] - Rule 'no classes should transitively depend on classes that depend on File class' was violated (30 times):
Class <de.tum.cit.ase.aspectj.CustomMidiDevice$2> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomMidiDevice$3> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomMidiDevice> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)
Class <de.tum.cit.ase.aspectj.CustomSoundbankReader> depends on <java.io.File> in (CustomSoundbankReader.java:0)
Class <de.tum.cit.ase.aspectj.CustomSoundbankReader> transitively depends on <java.io.File> by [javax.sound.midi.spi.SoundbankReader->java.io.File] in (CustomSoundbankReader.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> depends on <java.io.File> in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [java.awt.BorderLayout->java.awt.Container->java.awt.LayoutManager->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JButton->javax.swing.AbstractButton->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JFileChooser->javax.swing.filechooser.FileFilter->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JFrame->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JScrollPane->java.awt.LayoutManager->java.awt.Container->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.JTextArea->javax.swing.text.JTextComponent->javax.swing.JComponent->java.awt.Container->java.awt.LayoutManager->java.awt.Component->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.SwingUtilities->java.awt.Component->java.awt.Container->java.io.PrintStream->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.FileChooserExample> transitively depends on <java.io.File> by [javax.swing.filechooser.FileNameExtensionFilter->java.io.File] in (FileChooserExample.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileInputStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileOutputStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.FileReader->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.io.RandomAccessFile->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.channels.FileChannel->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.file.Paths->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.nio.file.spi.FileSystemProvider->java.nio.channels.FileChannel->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.HelloWorld> transitively depends on <java.io.File> by [java.util.Scanner->java.nio.file.Path->java.io.File] in (HelloWorld.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [java.util.zip.ZipFile->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipImageReaderExample> transitively depends on <java.io.File> by [javax.imageio.ImageIO->java.io.File] in (ZipImageReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample$1> depends on <java.io.File> in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample$1> transitively depends on <java.io.File> by [javax.sound.sampled.spi.AudioFileWriter->java.io.File] in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (ZipReaderExample.java:0)
Class <de.tum.cit.ase.aspectj.ZipReaderExample> transitively depends on <java.io.File> by [java.util.zip.ZipFile->java.io.File] in (ZipReaderExample.java:0)
at com.tngtech.archunit.lang.ArchRule$Assertions.assertNoViolation(ArchRule.java:94)
at com.tngtech.archunit.lang.ArchRule$Assertions.check(ArchRule.java:86)
at com.tngtech.archunit.lang.ArchRule$Factory$SimpleArchRule.check(ArchRule.java:165)
at com.tngtech.archunit.lang.syntax.ObjectsShouldInternal.check(ObjectsShouldInternal.java:81)
at testenv.CallLogTest.testTransitiveDependencies(CallLogTest.java:44)
at java.base/java.lang.reflect.Method.invoke(Method.java:568)
at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
```
