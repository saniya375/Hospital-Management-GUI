import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
public class HospitalManagementGUI extends JFrame {

    static class Patient {
        int id;
        String name;
        int age;
        String gender;
        String disease;
        String doctor;
        String status; // "Admitted" or "Discharged"

        Patient(int id, String name, int age, String gender, String disease, String doctor, String status) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.disease = disease;
            this.doctor = doctor;
            this.status = status;
        }
    }

    private final List<Patient> patients = new ArrayList<>();
    private int nextId = 1;

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField nameField = new JTextField(15);
    private final JTextField ageField = new JTextField(4);
    private final JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
    private final JTextField diseaseField = new JTextField(15);
    private final JTextField doctorField = new JTextField(15);
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{"Admitted", "Discharged"});
    private final JTextField searchField = new JTextField(15);

    private Integer selectedId = null;

    public HospitalManagementGUI() {
        super("Hospital Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Name", "Age", "Gender", "Disease", "Doctor", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedRowIntoForm();
            }
        });
        JScrollPane tableScroll = new JScrollPane(table);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addFormRow(formPanel, gbc, 0, "Name:", nameField);
        addFormRow(formPanel, gbc, 1, "Age:", ageField);
        addFormRow(formPanel, gbc, 2, "Gender:", genderBox);
        addFormRow(formPanel, gbc, 3, "Disease:", diseaseField);
        addFormRow(formPanel, gbc, 4, "Doctor Assigned:", doctorField);
        addFormRow(formPanel, gbc, 5, "Status:", statusBox);

        JButton addBtn = new JButton("Add Patient");
        JButton updateBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton clearBtn = new JButton("Clear Form");

        addBtn.addActionListener(this::onAdd);
        updateBtn.addActionListener(this::onUpdate);
        deleteBtn.addActionListener(this::onDelete);
        clearBtn.addActionListener(e -> clearForm());

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Search by Name");
        JButton showAllBtn = new JButton("Show All");
        searchBtn.addActionListener(this::onSearch);
        showAllBtn.addActionListener(e -> refreshTable(patients));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
  
