# Java Project with Manual JAR Dependencies

This is a simple Java project that uses external libraries (JAR files) stored in the `lib/` folder. You need to manually add these libraries to your IDE after cloning the project.

## 📁 Project Structure

```shell
MyJavaProject/
├── src/              # Your Java source code
│   └── Main.java
├── lib/              # Folder containing JAR dependencies
│   ├── gson.jar
│   └── mysql-connector-java.jar
├── README.md
```

## 🛠 Requirements

- Java Development Kit (JDK 8 or later)
- IntelliJ IDEA or Eclipse IDE

## 🚀 How to Run the Project

✅ 1. Clone the Repository

```shell
git clone https://github.com/anda-g/istad-java-mini-project.git
```
```shell
cd your-project
```

✅ 2. Open the Project in Your IDE

Open the project folder in IntelliJ IDEA or Eclipse.

✅ 3. Add JARs from the `lib/` Folder

For IntelliJ IDEA:
1. Go to File → Project Structure → Modules → Dependencies.
2. Click the + icon → choose "JARs or directories".
3. Select all the `.jar` files inside the `lib/` folder.
4. Choose "Classes" when prompted.
5. Click OK or Apply to finish.

For Eclipse:
1. Right-click your project → Build Path → Configure Build Path.
2. Go to the Libraries tab.
3. Click "Add JARs..." (if `lib/` is inside the project).
4. Select the JAR files from the `lib/` folder.
5. Click Apply and Close.

✅ 4. Run the Project

Now you can run the project normally.

