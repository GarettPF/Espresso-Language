# Espresso-Language

![Espresso-Language](https://cdn-icons-png.flaticon.com/512/5228/5228061.png)

Espresso-Language is a programming language designed for creating and running expressive and efficient code. This repository contains the source code and documentation for the Espresso-Language project.

## Features

- **Expressive Syntax**: Espresso-Language aims to have a clean and expressive syntax, allowing developers to write code that is easy to read and understand.

- **Efficiency**: The language is designed to be highly efficient, making it suitable for a wide range of applications, from small scripts to large-scale projects.

- **Static Typing**: Espresso-Language includes a static typing system that helps catch errors at compile time, improving the reliability of your code.

- **Extensible**: The language is designed to be easily extensible, allowing developers to create their own libraries and modules to enhance the functionality of Espresso-Language.

## Prerequisites
To use Espresso-Language, you need to have the following software installed:

- Java Development Kit (JDK) 8 or above
- Git
- Bash (Command-line shell) or UNIX shell
- Jasmin Assembler. [Install Guide](https://jasmin.sourceforge.net/)

## Installation

To use Espresso-Language, you'll need to follow these steps:

1. Clone this repository to your local machine using the following command:
   ```
   git clone https://github.com/GarettPF/Espresso-Language.git
   ```

2. Make sure you have a compatible version of the Java Development Kit (JDK) installed on your machine.

3. Build the project using ```ant``` to invoke the build file.

4. Once the project is built successfully, you can run Espresso-Language code using the provided execution script or by invoking the necessary classes manually.

## Usage

To run Espresso-Language code:

1. Using the provided execution script:
   ```
   ./espressoc <filename>.java
   ```

   Replace `<filename>` with the path to your Espresso-Language source file.
   This should have created the Jasmin assembly code files `<filename>.j`.
   There are multiple testing code to use located in the Tests Folder with each version of the espresso language

2. Invoking the necessary Jasmin assembly files manually:
   ```
   jasmin <filename>.j
   ```
   or
   ```
   jasmin *.j
   ```

   Replace `<filename>` with the path to your Espresso-Language source file.
   There should now be Java Byte Code that was generated.
   
3. Run the Java byte code using the espresso script
   ```
    ./espresso <filename>
   ```
   
   Replace `<filename` with the entry class or the class containing the main function.
   Using your JVM instead of the `espresso` script might cause importing errors as espresso's import is less technical than Java's import system. The `espresso` script should solve those import issues.
  
## Example Code

There is an example code that simulates the Tower of Hanoi disc puzzle.

To run the example:
```
ant example
```

This produces the Jasmin Assembly files. Create the Java byte code using.
```
jasmin *.j
```

Now run the Espresso program.
```
./espresso Pegs
```


## Contributing

Contributions to Espresso-Language are welcome! If you find a bug or have a feature request, please open an issue on the [issue tracker](https://github.com/GarettPF/Espresso-Language/issues).

If you would like to contribute code, fork the repository, create a new branch for your changes, and submit a pull request. Please ensure your code adheres to the project's coding conventions and include appropriate tests.

## License

Espresso-Language is released under the [MIT License](https://github.com/GarettPF/Espresso-Language/blob/main/LICENSE). See the `LICENSE` file for more information.

## Acknowledgments

- The Espresso-Language project was inspired by various programming languages and their communities.
- Thanks to all the contributors who have helped improve Espresso-Language.

## Contact

If you have any questions or feedback regarding Espresso-Language, feel free to reach out to the project maintainer:

GarettPF - garettpfolster677@gmail.com
