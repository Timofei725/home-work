package org_structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgStructureParserImpl implements OrgStructureParser {
    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] separatedLine = line.split(";");
                Employee thisEmployee = new Employee();
                thisEmployee.setId(Long.valueOf(separatedLine[0]));
                thisEmployee.setBossId(separatedLine[1].isEmpty() ? null : Long.valueOf(separatedLine[1]));
                thisEmployee.setName(separatedLine[2]);
                thisEmployee.setPosition(separatedLine[3]);
                employees.add(thisEmployee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        addSubordinatesAndBosses(employees);
        return findBoss(employees);
    }

    private static void addSubordinatesAndBosses(List<Employee> employees) {
        Map<Long, Employee> employeeMap = new HashMap<>();
        for (Employee employee : employees) {
            employeeMap.put(employee.getId(), employee);
        }
        for (Employee employee : employees) {
            Long bossId = employee.getBossId();
            if (bossId != null) {
                Employee boss = employeeMap.get(bossId);
                employee.setBoss(boss);
                boss.getSubordinate().add(employee);
            }
        }

    }

    public Employee findBoss(List<Employee> employees) {
        for (Employee employee : employees) {
            if (employee.getBoss() == null) return employee;
        }
        return null;
    }
}
