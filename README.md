# 📦 ISTAD Java Mini Project - Group 2

This is a mini Java console application developed by students of **ISTAD Group 2**. The project uses PostgreSQL as its database and demonstrates a simple Product & User Management System with input validation and role-based access (admin/user).

## 👨‍💻 Team Members

- Leng Narak (Leader)
- Tong Bora
- Kong Sisovandara
- But Seavthong
- Kung Sovannda
- Khim Sokha
- Sorn Sophamarinet

**🧑‍🏫 Mentor**: Kim Chansokpheng

## 🗂️ Project Structure

```
mini-project/
├── src/
│   ├── repository/        # Repositories (Interfaces + Implementations)
│   ├── service/           # Services (Interfaces + Implementations)
│   ├── utils/             # Utility classes (e.g., InputValidator)
│   ├── view/              # UI and Console Interaction
│   ├── Main.java          # Entry point
│   └── config.properties      # Database config file
├── lib/                   # External JAR dependencies (if any)
│   └── ...                # Example: postgresql.jar
├── user.properties        # User login/session config
├── README.md
└── mini-project.iml       # IntelliJ Project File
```

## 🔧 Requirements

- Java Development Kit (JDK 8 or later)
- IntelliJ IDEA or Eclipse IDE


## 🚀 How to Run the Project

### ✅ 1. Clone the Repository

```bash
git clone https://github.com/anda-g/istad-java-mini-project.git
cd istad-java-mini-project
```

### ✅ 2. Open the Project in IntelliJ or Eclipse

- Open the folder as a new project.
- Make sure your IDE is using Java 8+.

### ✅ 3. Add JAR Dependencies (if using external JARs)

**For IntelliJ:**

- `File → Project Structure → Modules → Dependencies`
- Click ➕ → "JARs or directories" → Select from `lib/`
- Apply changes

**For Eclipse:**

- `Right-click project → Build Path → Configure Build Path → Libraries`
- Click "Add JARs..." and select from `lib/`

### ✅ 4. Run the Main Class

Find `Main.java` in the `src/view/` or root `src/` folder and run it.

## 🔑 Notes

- ✅ **Use role `admin` during registration to get admin access.**
- All inputs are validated using the custom `InputValidator` utility.
- Colored console outputs are used for better readability.

## ❤️ Special Thanks

To our instructor **Kim Chansokpheng** and the ISTAD team for the guidance and support.
