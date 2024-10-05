Packages
- [ ] java.io &rarr; prevented by access to java.io.File
- [ ] java.nio.channels &rarr; prevented by java.io.File
- [ ] java.nio.file &rarr; prevented by java.io.File
- [ ] java.nio.file.attribute &rarr; prevented by java.io.File
- [ ] java.nio.file.spi &rarr; prevented by java.io.File
- [ ] java.util.jar &rarr; prevented by java.io.File
- [ ] java.util.zip &rarr; prevented by java.io.File
- [ ] FileSystemProvider &rarr; prevented by java.io.File
- [ ] FileTypeDetector &rarr; prevented by java.io.File
- [ ] javax.imageio.stream &rarr; prevented by java.io.File
- [ ] javax.sound.midi.spi  &rarr; prevented by javax.sound.midi
- [ ] javax.sound.sampled.spi &rarr; prevented by javax.sound.sampled
- [ ] javax.swing.filechooser &rarr; prevented by java.io.File
- [ ] AudioFileReader &rarr; prevented by javax.sound.sampled
- [ ] AudioFileWriter  &rarr; prevented by javax.sound.sampled  
- [ ] SoundbankReader
- [ ] jdk.security.jarsigner


Architecture Violation [Priority: MEDIUM] - Rule 'no classes should transitively depend on classes that depend on File class' was violated (30 times):
Class <de.tum.cit.ase.aspectj.CustomMidiDevice$2> transitively depends on <java.io.File> by [java.io.PrintStream->java.io.File] in (CustomMidiDevice.java:0)