package org_structure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Employee {
    private Long id;
    private Long bossId;
    private String name;
    private String position;
    private Employee boss;
    private List<Employee> subordinate = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBossId() {
        return bossId;
    }

    public void setBossId(Long bossId) {
        this.bossId = bossId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Employee getBoss() {
        return boss;
    }

    public void setBoss(Employee boss) {
        this.boss = boss;
    }

    public List<Employee> getSubordinate() {
        return subordinate;
    }

    @Override
    public String toString() {
        String bossName = boss == null ? "null" : boss.getName();
        StringBuilder sb = new StringBuilder();
        sb.append("id=")
                .append(id)
                .append(", ")
                .append("name=")
                .append(name)
                .append(", ")
                .append("position=")
                .append(position)
                .append(", ")
                .append("bossName=")
                .append(bossName).append("\n");

        if (!subordinate.isEmpty()) {
            sb.append("Subordinates: \n");
            for (Employee employee : subordinate) {
                sb.append("\t").append(employee.toString().replace("\n", "\n\t")).append("\n");
            }
        }

        return sb.toString();
    }
}