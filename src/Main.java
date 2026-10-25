import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Main extends JFrame {

    // Modernized Color Palette
    private static final Color NAVY = new Color(30, 41, 59);         // Slate 800
    private static final Color SIDEBAR_BG = new Color(15, 23, 42);   // Slate 900
    private static final Color BLUE = new Color(37, 99, 235);        // Blue 600
    private static final Color BLUE_HOVER = new Color(29, 78, 216);  // Blue 700
    private static final Color BG = new Color(248, 250, 252);        // Slate 50
    private static final Color TEXT = new Color(51, 65, 85);         // Slate 700
    private static final Color GREEN = new Color(16, 185, 129);      // Emerald 500
    private static final Color RED = new Color(239, 68, 68);         // Red 500
    private static final Color BORDER_COLOR = new Color(226, 232, 240); // Slate 200

    private final ArrayList<Student> students = new ArrayList<>();
    private int nextId = 1;
    private int selectedId = -1;

    private JTextField nameField, rollField, ageField;
    private JTextField emailField, phoneField, searchField;
    private JTextArea addressField;
    private JComboBox<String> genderBox, deptBox, sectionBox;
    private JTable table;
    private DefaultTableModel model;

    private JLabel totalValue, maleValue, femaleValue, deptValue;

    static class Student {
        int id, age;
        String name, roll, gender, department, section;
        String email, phone, address;

        Student(int id, String name, String roll, int age,
                String gender, String department, String section,
                String email, String phone, String address) {
            this.id = id;
            this.name = name;
            this.roll = roll;
            this.age = age;
            this.gender = gender;
            this.department = department;
            this.section = section;
            this.email = email;
            this.phone = phone;
            this.address = address;
        }
    }

    public Main() {
        super("Student Management System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 650));
        setSize(1200, 760);
        setLocationRelativeTo(null);

        buildUI();
        addSampleStudents();
        refreshTable();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        setContentPane(root);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 16, 20, 16));

        JLabel brand = new JLabel("<html><b>STUDENT</b><br><span style='color: #38bdf8;'>PORTAL</span></html>");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 20));
        brand.setBorder(new EmptyBorder(0, 4, 35, 0));
        sidebar.add(brand);

        nav(sidebar, "Dashboard", () -> {
            searchField.setText("");
            clearForm();
            refreshTable();
        });

        nav(sidebar, "Add Student", () -> {
            clearForm();
            nameField.requestFocusInWindow();
        });

        nav(sidebar, "View Records", () -> {
            searchField.setText("");
            refreshTable();
        });

        nav(sidebar, "Search", () -> searchField.requestFocusInWindow());

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("<html>v2.0 Modern Edition<br>Java Swing & Flat UI</html>");
        footer.setForeground(new Color(148, 163, 184));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sidebar.add(footer);

        root.add(sidebar, BorderLayout.WEST);

        // Main content area
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(24, 24, 24, 24));
        root.add(main, BorderLayout.CENTER);

        // Heading Header
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);

        JLabel subtitle = new JLabel("Manage student profiles, academic sections, and data records seamlessly.");
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);
        heading.add(titles, BorderLayout.WEST);

        JLabel status = new JLabel("● SYSTEM ACTIVE ");
        status.setForeground(GREEN);
        status.setFont(new Font("SansSerif", Font.BOLD, 12));
        heading.add(status, BorderLayout.EAST);

        main.add(heading, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        main.add(content, BorderLayout.CENTER);

        // Summary Cards
        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);

        totalValue = new JLabel("0");
        maleValue = new JLabel("0");
        femaleValue = new JLabel("0");
        deptValue = new JLabel("0");

        stats.add(statCard("TOTAL STUDENTS", totalValue, BLUE));
        stats.add(statCard("MALE STUDENTS", maleValue, GREEN));
        stats.add(statCard("FEMALE STUDENTS", femaleValue, new Color(236, 72, 153))); // Pink 500
        stats.add(statCard("DEPARTMENTS", deptValue, new Color(139, 92, 246)));     // Violet 500

        content.add(stats, BorderLayout.NORTH);

        // Workspace (Form + Table)
        JPanel workspace = new JPanel(new BorderLayout(16, 0));
        workspace.setOpaque(false);
        content.add(workspace, BorderLayout.CENTER);

        // Left Form Card
        JPanel form = card();
        form.setPreferredSize(new Dimension(310, 0));
        form.setLayout(new BorderLayout(0, 12));

        JLabel formTitle = new JLabel("Student Profile");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTitle.setForeground(NAVY);
        form.add(formTitle, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(0, 1, 0, 4));
        fields.setOpaque(false);

        nameField = new JTextField();
        rollField = new JTextField();
        ageField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        deptBox = new JComboBox<>(new String[]{
                "Computer Science", "Data Science",
                "Information Technology", "Electronics",
                "Mechanical", "Civil"
        });
        sectionBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});

        field(fields, "Full Name *", nameField);
        field(fields, "Roll Number *", rollField);
        field(fields, "Age *", ageField);
        field(fields, "Gender", genderBox);
        field(fields, "Department", deptBox);
        field(fields, "Section", sectionBox);
        field(fields, "Email", emailField);
        field(fields, "Phone", phoneField);

        fields.add(new JLabel("Address"));
        addressField = new JTextArea(2, 15);
        addressField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressField);
        addressScroll.setPreferredSize(new Dimension(200, 48));
        fields.add(addressScroll);

        form.add(fields, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(2, 2, 8, 8));
        actions.setOpaque(false);

        JButton add = button("Add", GREEN);
        JButton update = button("Update", BLUE);
        JButton delete = button("Delete", RED);
        JButton clear = button("Clear", new Color(100, 116, 139));

        actions.add(add);
        actions.add(update);
        actions.add(delete);
        actions.add(clear);
        form.add(actions, BorderLayout.SOUTH);

        workspace.add(form, BorderLayout.WEST);

        // Right Table Card
        JPanel records = card();
        records.setLayout(new BorderLayout(0, 12));
        workspace.add(records, BorderLayout.CENTER);

        JPanel tableHeader = new JPanel(new BorderLayout(8, 8));
        tableHeader.setOpaque(false);

        JLabel recordsTitle = new JLabel("Directory Records");
        recordsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        recordsTitle.setForeground(NAVY);
        tableHeader.add(recordsTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search by name, roll, department...");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton reset = button("Reset Filter", new Color(100, 116, 139));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(reset, BorderLayout.EAST);
        tableHeader.add(searchPanel, BorderLayout.CENTER);

        records.add(tableHeader, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Roll", "Age", "Gender", "Department", "Section"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 || column == 3 ? Integer.class : String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setSelectionBackground(new Color(224, 242, 254)); // Light Sky Blue
        table.setSelectionForeground(NAVY);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        // Center align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        records.add(tableScroll, BorderLayout.CENTER);

        // Actions and Listeners
        add.addActionListener(e -> addStudent());
        update.addActionListener(e -> updateStudent());
        delete.addActionListener(e -> deleteStudent());
        clear.addActionListener(e -> clearForm());
        reset.addActionListener(e -> {
            searchField.setText("");
            applySearch();
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applySearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applySearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applySearch(); }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                int id = (Integer) model.getValueAt(row, 0);
                students.stream().filter(s -> s.id == id).findFirst().ifPresent(this::loadForm);
            }
        });
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)));
        return p;
    }

    private JPanel statCard(String title, JLabel value, Color color) {
        JPanel p = card();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(100, 116, 139));

        value.setFont(new Font("SansSerif", Font.BOLD, 26));
        value.setForeground(color);

        p.add(label);
        p.add(Box.createVerticalStrut(6));
        p.add(value);
        return p;
    }

    private void nav(JPanel panel, String text, Runnable action) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setForeground(new Color(203, 213, 225));
        b.setBackground(SIDEBAR_BG);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(0, 12, 0, 8));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(30, 41, 59));
                b.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(SIDEBAR_BG);
                b.setForeground(new Color(203, 213, 225));
            }
        });

        b.addActionListener(e -> action.run());
        panel.add(b);
        panel.add(Box.createVerticalStrut(4));
    }

    private void field(JPanel panel, String label, JComponent input) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(TEXT);

        input.setFont(new Font("SansSerif", Font.PLAIN, 12));
        input.setPreferredSize(new Dimension(100, 30));
        if (input instanceof JTextField || input instanceof JComboBox) {
            input.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)));
        }

        panel.add(l);
        panel.add(input);
        panel.add(Box.createVerticalStrut(2));
    }

    private JButton button(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 12, 8, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(color);
            }
        });
        return b;
    }

    private void addSampleStudents() {
        students.add(new Student(nextId++, "Aarav Sharma", "101", 19, "Male", "Computer Science", "A", "aarav@example.com", "9876543210", "Bangalore"));
        students.add(new Student(nextId++, "Diya Patel", "102", 20, "Female", "Data Science", "B", "diya@example.com", "9123456780", "Mumbai"));
        students.add(new Student(nextId++, "Rohan Gupta", "103", 21, "Male", "Electronics", "A", "rohan@example.com", "9988776655", "Delhi"));
    }

    private boolean valid() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();

        if (name.isEmpty() || roll.isEmpty()) {
            error("Name and roll number are required.");
            return false;
        }

        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException ex) {
            error("Age must be a valid number.");
            return false;
        }

        if (age < 1 || age > 120) {
            error("Age must be between 1 and 120.");
            return false;
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            error("Enter a valid email address.");
            return false;
        }

        for (Student s : students) {
            if (s.roll.equalsIgnoreCase(roll) && s.id != selectedId) {
                error("Roll number already exists.");
                return false;
            }
        }

        return true;
    }

    private Student readForm(int id) {
        return new Student(id,
                nameField.getText().trim(),
                rollField.getText().trim(),
                Integer.parseInt(ageField.getText().trim()),
                genderBox.getSelectedItem().toString(),
                deptBox.getSelectedItem().toString(),
                sectionBox.getSelectedItem().toString(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                addressField.getText().trim());
    }

    private void addStudent() {
        if (!valid()) return;
        students.add(readForm(nextId++));
        refreshTable();
        clearForm();
        message("Student added successfully.");
    }

    private void updateStudent() {
        if (selectedId == -1) {
            error("Select a student from the table first.");
            return;
        }
        if (!valid()) return;

        Student replacement = readForm(selectedId);
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).id == selectedId) {
                students.set(i, replacement);
                break;
            }
        }

        refreshTable();
        clearForm();
        message("Student updated successfully.");
    }

    private void deleteStudent() {
        if (selectedId == -1) {
            error("Select a student from the table first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student record?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) return;

        students.removeIf(s -> s.id == selectedId);
        refreshTable();
        clearForm();
        message("Student deleted successfully.");
    }

    private void loadForm(Student s) {
        selectedId = s.id;
        nameField.setText(s.name);
        rollField.setText(s.roll);
        ageField.setText(String.valueOf(s.age));
        genderBox.setSelectedItem(s.gender);
        deptBox.setSelectedItem(s.department);
        sectionBox.setSelectedItem(s.section);
        emailField.setText(s.email);
        phoneField.setText(s.phone);
        addressField.setText(s.address);
    }

    private void clearForm() {
        selectedId = -1;
        nameField.setText("");
        rollField.setText("");
        ageField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        genderBox.setSelectedIndex(0);
        deptBox.setSelectedIndex(0);
        sectionBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Student s : students) {
            model.addRow(new Object[]{
                    s.id, s.name, s.roll, s.age,
                    s.gender, s.department, s.section
            });
        }
        applySearch();
        updateStats();
    }

    private void applySearch() {
        if (table == null || sorterOrNull() == null) return;
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            sorterOrNull().setRowFilter(null);
        } else {
            sorterOrNull().setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(query)));
        }
    }

    private TableRowSorter<DefaultTableModel> sorterOrNull() {
        if (table == null) return null;
        if (table.getRowSorter() instanceof TableRowSorter<?>) {
            @SuppressWarnings("unchecked")
            TableRowSorter<DefaultTableModel> s = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
            return s;
        }
        return null;
    }

    private void updateStats() {
        totalValue.setText(String.valueOf(students.size()));
        long males = students.stream().filter(s -> s.gender.equals("Male")).count();
        long females = students.stream().filter(s -> s.gender.equals("Female")).count();
        Set<String> departments = new HashSet<>();
        for (Student s : students) departments.add(s.department);

        maleValue.setText(String.valueOf(males));
        femaleValue.setText(String.valueOf(females));
        deptValue.setText(String.valueOf(departments.size()));
    }

    private void error(String text) {
        JOptionPane.showMessageDialog(this, text, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Main().setVisible(true);
        });
    }
}
