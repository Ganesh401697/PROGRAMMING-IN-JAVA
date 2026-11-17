import java.util.*;

class Student {
    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return regNo.equals(s.regNo);
    }

    // TODO completed: equal registration numbers produce the same hash code.
    @Override public int hashCode() {
        return regNo.hashCode();
    }

    @Override public String toString() { return regNo + " " + name; }
}

public class Q3HashSetContract {
    public static void main(String[] args) {
        Set<Student> scans = new HashSet<Student>();
        scans.add(new Student("192311001", "Arun"));
        scans.add(new Student("192311002", "Divya"));
        scans.add(new Student("192311001", "Arun"));
        System.out.println("Corrected HashSet size: " + scans.size());
        System.out.println("Students: " + scans);

        // Additional test: same regNo, different name is still the same student.
        scans.add(new Student("192311001", "Arun Kumar"));
        System.out.println("After same ID with changed name: " + scans.size());

        // Deliberately broken class: equals uses ID, but hashCode uses object identity.
        class BrokenStudent {
            String id;
            BrokenStudent(String id) { this.id = id; }
            public boolean equals(Object o) {
                return o instanceof BrokenStudent && id.equals(((BrokenStudent)o).id);
            }
            public int hashCode() { return System.identityHashCode(this); }
        }
        Set<BrokenStudent> broken = new HashSet<BrokenStudent>();
        broken.add(new BrokenStudent("A1"));
        broken.add(new BrokenStudent("A1"));
        System.out.println("Broken HashSet duplicate size: " + broken.size());

        // HashMap alternative: key is the unique registration number.
        Map<String, Student> byId = new HashMap<String, Student>();
        byId.put("192311001", new Student("192311001", "Arun"));
        byId.put("192311002", new Student("192311002", "Divya"));
        byId.put("192311001", new Student("192311001", "Arun"));
        System.out.println("HashMap unique students: " + byId.size());
    }
}
