package com.itheima.UI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagementSystem extends JFrame {

    private DefaultTableModel tableModel;
    private List<Employee> employeeList = new ArrayList<>();
    private JTextField nameField, searchField;
    private JTextArea addressArea;
    private JComboBox<String> departmentComboBox;
    private int selectedRow = -1;

    public EmployeeManagementSystem() {
        setTitle("员工管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置背景颜色
        getContentPane().setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // 表格部分
        String[] columnNames = {"ID", "姓名", "部门", "地址"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 添加选择监听器
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        fillFormFields(selectedRow);
                    }
                }
            }
        });

        // 搜索栏
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(240, 248, 255));
        JLabel searchLabel = new JLabel("搜索姓名:");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // 表单部分
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("姓名:");
        nameField = new JTextField();

        JLabel departmentLabel = new JLabel("部门:");
        departmentComboBox = new JComboBox<>(new String[]{"销售", "技术", "市场", "人力资源"});

        JLabel addressLabel = new JLabel("地址:");
        addressArea = new JTextArea(3, 20);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);

        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("更新");
        JButton deleteButton = new JButton("删除");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(departmentLabel);
        formPanel.add(departmentComboBox);
        formPanel.add(addressLabel);
        formPanel.add(addressScrollPane);
        formPanel.add(addButton);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);
        formPanel.add(new JLabel()); // 空白占位符

        mainPanel.add(formPanel, BorderLayout.SOUTH);

        // 添加按钮事件监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        // 更新按钮事件监听器
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        // 删除按钮事件监听器
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        // 搜索按钮事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmployee();
            }
        });

        add(mainPanel);
    }

    private void addEmployee() {
        String name = nameField.getText();
        String department = (String) departmentComboBox.getSelectedItem();
        String address = addressArea.getText();

        if (!name.isEmpty() && !address.isEmpty()) {
            int id = employeeList.size() + 1;
            Employee employee = new Employee(id, name, department, address);
            employeeList.add(employee);
            tableModel.addRow(new Object[]{id, name, department, address});
            clearFormFields();
        } else {
            JOptionPane.showMessageDialog(this, "请填写所有字段!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        if (selectedRow != -1) {
            String name = nameField.getText();
            String department = (String) departmentComboBox.getSelectedItem();
            String address = addressArea.getText();

            if (!name.isEmpty() && !address.isEmpty()) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Employee employee = new Employee(id, name, department, address);
                employeeList.set(selectedRow, employee);
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(department, selectedRow, 2);
                tableModel.setValueAt(address, selectedRow, 3);
                clearFormFields();
                selectedRow = -1; // 清除选中状态
            } else {
                JOptionPane.showMessageDialog(this, "请填写所有字段!", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要更新的员工!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        if (selectedRow != -1) {
            employeeList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            clearFormFields();
            selectedRow = -1; // 清除选中状态
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的员工!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchEmployee() {
        String searchName = searchField.getText().trim();
        if (!searchName.isEmpty()) {
            boolean found = false;
            for (Employee employee : employeeList) {
                if (employee.getName().equalsIgnoreCase(searchName)) {
                    tableModel.setRowCount(0); // 清空表格
                    tableModel.addRow(new Object[]{employee.getId(), employee.getName(), employee.getDepartment(), employee.getAddress()});
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "未找到该员工!", "提示", JOptionPane.INFORMATION_MESSAGE);
                loadAllEmployees(); // 加载所有员工
            }
        } else {
            JOptionPane.showMessageDialog(this, "请输入要搜索的姓名!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllEmployees() {
        tableModel.setRowCount(0); // 清空表格
        for (Employee employee : employeeList) {
            tableModel.addRow(new Object[]{employee.getId(), employee.getName(), employee.getDepartment(), employee.getAddress()});
        }
    }

    private void clearFormFields() {
        nameField.setText("");
        departmentComboBox.setSelectedIndex(0);
        addressArea.setText("");
    }

    private void fillFormFields(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            Integer id = (Integer) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);
            String department = (String) tableModel.getValueAt(row, 2);
            String address = (String) tableModel.getValueAt(row, 3);

            nameField.setText(name);
            departmentComboBox.setSelectedItem(department);
            addressArea.setText(address);
        }
    }

    private static class Employee {
        private final int id;
        private final String name;
        private final String department;
        private final String address;

        public Employee(int id, String name, String department, String address) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.address = address;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public String getAddress() {
            return address;
        }
    }
}
