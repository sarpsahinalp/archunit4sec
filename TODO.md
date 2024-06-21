* Where to store the properties of the config file, a property record 
might be good, this is also how we configure most of the stuff at work
-> Implemented DTO to store the information of parsed config file

* Where to store the implementation of ArchRules
-> I would suggest a Map structure where we have the identifiers of
certain ArchRules, also make BaseArchRules to make the definition of
rules easier, make them customizable
* Create a way to detect all transitive dependencies of a project
-> What is a way????
