
import java.util.List;
import java.util.Scanner;

/*
 * App: lightweight console UI to perform CRUD.
 * Minimal but extendable: menu calls DAOs. Keep methods small.
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();
    private static final CategoryDAO catDAO = new CategoryDAO();
    private static final TaskDAO taskDAO = new TaskDAO();

    public static void main(String[] args) {
        System.out.println("Simple Task Manager (CRUD) — CSCE548 Project 1");
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listUsers();
                    case "2" -> createUser();
                    case "3" -> updateUser();
                    case "4" -> deleteUser();
                    case "5" -> listCategories();
                    case "6" -> createCategory();
                    case "7" -> updateCategory();
                    case "8" -> deleteCategory();
                    case "9" -> listTasks();
                    case "10" -> createTask();
                    case "11" -> updateTask();
                    case "12" -> deleteTask();
                    case "q", "Q" -> {
                        System.out.println("Bye.");
                        return;
                    }
                    default -> System.out.println("Unknown option");
                }
            } catch (Exception e) {
                System.err.println("Operation failed: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) List users    2) Create user   3) Update user   4) Delete user");
        System.out.println("5) List categories 6) Create category 7) Update category 8) Delete category");
        System.out.println("9) List tasks    10) Create task   11) Update task  12) Delete task");
        System.out.println("Q) Quit");
        System.out.print("Choose: ");
    }

    // --- Users ---
    private static void listUsers() throws Exception {
        List<User> users = userDAO.findAll();
        users.forEach(u -> System.out.println(u));
    }

    private static void createUser() throws Exception {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        User u = new User(null, name);
        userDAO.create(u);
        System.out.println("Created: " + u);
    }

    private static void updateUser() throws Exception {
        System.out.print("User id to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        User u = userDAO.findById(id);
        if (u == null) {
            System.out.println("Not found");
            return;
        }
        System.out.print("New name (enter to keep '" + u.name + "'): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            u.name = name;
        }
        userDAO.update(u);
        System.out.println("Updated: " + u);
    }

    private static void deleteUser() throws Exception {
        System.out.print("User id to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        if (userDAO.delete(id)) {
            System.out.println("Deleted " + id); 
        }else {
            System.out.println("Delete failed (maybe foreign key).");
        }
    }

    // --- Categories ---
    private static void listCategories() throws Exception {
        List<Category> cs = catDAO.findAll();
        cs.forEach(c -> System.out.println(c));
    }

    private static void createCategory() throws Exception {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        Category c = new Category(null, name);
        catDAO.create(c);
        System.out.println("Created: " + c);
    }

    private static void updateCategory() throws Exception {
        System.out.print("Category id to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Category c = catDAO.findById(id);
        if (c == null) {
            System.out.println("Not found");
            return;
        }
        System.out.print("New name (enter to keep '" + c.name + "'): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            c.name = name;
        }
        catDAO.update(c);
        System.out.println("Updated: " + c);
    }

    private static void deleteCategory() throws Exception {
        System.out.print("Category id to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        if (catDAO.delete(id)) {
            System.out.println("Deleted " + id); 
        }else {
            System.out.println("Delete failed (maybe foreign key).");
        }
    }

    // --- Tasks ---
    private static void listTasks() throws Exception {
        List<String> rows = taskDAO.findAllWithNames();
        rows.forEach(System.out::println);
    }

    private static void createTask() throws Exception {
        System.out.print("User id: ");
        int uid = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Category id: ");
        int cid = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Completed? (y/N): ");
        boolean done = scanner.nextLine().trim().toLowerCase().startsWith("y");
        Task t = new Task(null, uid, cid, desc, done);
        taskDAO.create(t);
        System.out.println("Created: " + t);
    }

    private static void updateTask() throws Exception {
        System.out.print("Task id to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Task t = taskDAO.findById(id);
        if (t == null) {
            System.out.println("Not found");
            return;
        }
        System.out.print("New user id (enter to keep " + t.userId + "): ");
        String s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            t.userId = Integer.valueOf(s);
        }
        System.out.print("New category id (enter to keep " + t.categoryId + "): ");
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            t.categoryId = Integer.valueOf(s);
        }
        System.out.print("New description (enter to keep): ");
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            t.description = s;
        }
        System.out.print("Completed? (y/N): ");
        s = scanner.nextLine().trim();
        if (!s.isEmpty()) {
            t.completed = s.toLowerCase().startsWith("y");
        }
        taskDAO.update(t);
        System.out.println("Updated: " + t);
    }

    private static void deleteTask() throws Exception {
        System.out.print("Task id to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        if (taskDAO.delete(id)) {
            System.out.println("Deleted " + id); 
        }else {
            System.out.println("Delete failed.");
        }
    }
}