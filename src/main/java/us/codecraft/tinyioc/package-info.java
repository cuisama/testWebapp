/**
 * package-info.java’s purpose
 * The package-info.java is a Java file that can be added to any Java source package.  Its purpose is to provide a home for package level documentation and package level annotations. Simply create the package-info.java file and add the package declaration that it relates to in the file.  In fact, the only thing the package-info.java file must contain is the package declaration.
 * Package Annotations
 * Perhaps more importantly to today’s annotation driven programmer, the package-info.java file contains package level annotations. An annotation with ElementType.PACKAGE as one of its targets is a package-level annotation and there are many of them.  Using your favorite IDE’s code assistant (shown in Eclipse below) in a package-info.java file and you will find a number package annotation options.
 * For example, perhaps you want to deprecate all the types in a package. You could annotate each individual type (the classes, interfaces, enums, etc. defined in their .java files) with @Deprecated (as shown below).
 * Created by yxcui on 2017/4/18.
 * 1000行代码读懂spring
 */
@Deprecated
package us.codecraft.tinyioc;