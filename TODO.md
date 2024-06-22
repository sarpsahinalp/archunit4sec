## TODO List

* ```java
	@ArchTest
	void checkGWTAccess(JavaClasses classes) {
		LayeredArchitecture layers = Architectures.layeredArchitecture()
			.layer("Client").definedBy("..client..")
			.layer("Shared").definedBy("..shared..")
			... <many entries omitted here> ...
			;

		ArchRule rule = layers
			.whereLayer("Client").mayOnlyAccessLayers("Shared", "DomDtos", "DomBase", "DomConfig",  								
			                                          ... <many entries omitted here> ...
			                                         );
			;
		rule.check(classes);

```