package contests;

import java.io.PrintWriter;
import java.util.*;

public class EmployeeLevels {

    /*
    X — main boss.

    Input: pairs of Employee — Boss.

    Output: alphabetical list of employees with their level in hierarchy (0 — X, 1 — his subordinates and so on).

    0     X
         / \
    1   A   B
        |   |
    2   C   D
        |
    3   E

    input:
        6
        E C
        D B
        C A
        A X
        B X

    output:
        A 1
        B 1
        C 2
        D 2
        E 3
        X 0
    */

    private static class Employee {
        private String name;
        private String boss;
        private int level;
        private List<Employee> subordinates;

        Employee(String name, String boss) {
            this.name = name;
            this.boss = boss;
            this.level = -1;
            this.subordinates = new ArrayList<>();
        }

        String getName() {
            return this.name;
        }

        String getBoss() {
            return this.boss;
        }

        int getLevel() {
            return this.level;
        }

        void addSubordinate(Employee e) {
            subordinates.add(e);
        }

        void setLevel(int level) {
            this.level = level;
        }

        void setLevels() {
            for (Employee subordinate : subordinates) {
                subordinate.setLevel(this.level + 1);
                subordinate.setLevels();
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        Map<String, Employee> employeeMap = new HashMap<>();
        Employee x = new Employee("X", "");
        x.setLevel(0);
        employeeMap.put("X", x);

        int N = Integer.parseInt(in.nextLine().trim());

        for (int i = 1; i < N; i++) {
            String[] employeeLine = in.nextLine().split(" ");
            String name = employeeLine[0];
            String boss = employeeLine[1];
            employeeMap.put(name, new Employee(name, boss));
        }

        for (Map.Entry<String, Employee> entry : employeeMap.entrySet()) {
            String boss = entry.getValue().getBoss();
            if (employeeMap.containsKey(boss)) {
                employeeMap.get(boss).addSubordinate(entry.getValue());
            }
        }

        x.setLevels();

        List<Employee> employeesList = new ArrayList<>(employeeMap.values());

        employeesList.sort(Comparator.comparing(Employee::getName));

        for (Employee employee : employeesList) {
            out.println(employee.getName() + " " + employee.getLevel());
        }

        out.flush();
    }
}