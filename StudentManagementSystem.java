import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentManagementSystem {

    private JFrame frame;
    private JTextField studentNameField, studentIDField, gradeField;
    private JComboBox<String> studentDropdown, courseDropdown;
    private DefaultListModel<String> studentListModel;
    private JList<String> studentList;
    private ArrayList<String> courses;
    private ArrayList<Student> students;
    private JTable studentDetailsTable;
    private JScrollPane tableScrollPane;

    public StudentManagementSystem() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        studentNameField = new JTextField(10);
        studentIDField = new JTextField(10);
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton viewDetailsButton = new JButton("View Student Details");
        addButton.addActionListener(new AddStudentListener());
        updateButton.addActionListener(new UpdateStudentListener());
        viewDetailsButton.addActionListener(new ViewDetailsListener());
        topPanel.add(new JLabel("Name:"));
        topPanel.add(studentNameField);
        topPanel.add(new JLabel("ID:"));
        topPanel.add(studentIDField);
        topPanel.add(addButton);
        topPanel.add(updateButton);
        topPanel.add(viewDetailsButton);

        // Center panel for displaying students
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        JScrollPane scrollPane = new JScrollPane(studentList);

        JPanel bottomPanel = new JPanel();
        courses = new ArrayList<>();
        students = new ArrayList<>();
        courses.add("Math 101");
        courses.add("History 201");
        courses.add("Physics 301");
        studentDropdown = new JComboBox<>();
        courseDropdown = new JComboBox<>(courses.toArray(new String[0]));
        gradeField = new JTextField(5);
        JButton enrollButton = new JButton("Enroll");
        JButton assignGradeButton = new JButton("Assign Grade");
        enrollButton.addActionListener(new EnrollStudentListener());
        assignGradeButton.addActionListener(new AssignGradeListener());
        bottomPanel.add(new JLabel("Student:"));
        bottomPanel.add(studentDropdown);
        bottomPanel.add(new JLabel("Course:"));
        bottomPanel.add(courseDropdown);
        bottomPanel.add(new JLabel("Grade:"));
        bottomPanel.add(gradeField);
        bottomPanel.add(enrollButton);
        bottomPanel.add(assignGradeButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    class AddStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String studentName = studentNameField.getText();
            String studentID = studentIDField.getText();
            if (!studentName.isEmpty() && !studentID.isEmpty()) {
                Student student = new Student(studentName, studentID);
                students.add(student);
                studentListModel.addElement(student.toString());
                studentDropdown.addItem(student.toString());
                studentNameField.setText("");
                studentIDField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both name and ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class UpdateStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String studentName = studentNameField.getText();
            String studentID = studentIDField.getText();
            if (!studentName.isEmpty() && !studentID.isEmpty()) {
                int selectedIndex = studentList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Student student = students.get(selectedIndex);
                    student.setName(studentName);
                    student.setID(studentID);
                    studentListModel.set(selectedIndex, student.toString());
                    studentDropdown.removeItemAt(selectedIndex);
                    studentDropdown.insertItemAt(student.toString(), selectedIndex);
                    studentNameField.setText("");
                    studentIDField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a student from the list.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both name and ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class ViewDetailsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] columnNames = {"Name", "ID", "Courses", "Grades"};
            Object[][] data = new Object[students.size()][4];
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                data[i][0] = student.getName();
                data[i][1] = student.getID();
                data[i][2] = String.join(", ", student.getCourses());
                data[i][3] = String.join(", ", student.getGrades());
            }
            studentDetailsTable = new JTable(data, columnNames);
            if (tableScrollPane != null) {
                frame.remove(tableScrollPane);
            }
            tableScrollPane = new JScrollPane(studentDetailsTable);
            frame.add(tableScrollPane, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        }
    }

    class EnrollStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int studentIndex = studentDropdown.getSelectedIndex();
            int courseIndex = courseDropdown.getSelectedIndex();
            if (studentIndex != -1 && courseIndex != -1) {
                Student student = students.get(studentIndex);
                String course = courses.get(courseIndex);
                student.enrollCourse(course);
                JOptionPane.showMessageDialog(frame, student.getName() + " has been enrolled in " + course, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select both student and course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class AssignGradeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int studentIndex = studentDropdown.getSelectedIndex();
            int courseIndex = courseDropdown.getSelectedIndex();
            String grade = gradeField.getText();
            if (studentIndex != -1 && courseIndex != -1 && !grade.isEmpty()) {
                Student student = students.get(studentIndex);
                String course = courses.get(courseIndex);
                student.assignGrade(course, grade);
                JOptionPane.showMessageDialog(frame, "Grade assigned to " + student.getName() + " for " + course, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select both student and course and enter a grade.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new StudentManagementSystem();
    }
}

class Student {
    private String name;
    private String ID;
    private ArrayList<String> courses;
    private ArrayList<String> grades;

    public Student(String name, String ID) {
        this.name = name;
        this.ID = ID;
        this.courses = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public ArrayList<String> getGrades() {
        return grades;
    }

    public void enrollCourse(String course) {
        if (!courses.contains(course)) {
            courses.add(course);
            grades.add(""); 
        }
    }

    public void assignGrade(String course, String grade) {
        int index = courses.indexOf(course);
        if (index != -1) {
            grades.set(index, grade);
        }
    }

    @Override
    public String toString() {
        return name + " (" + ID + ")";
    }
}
