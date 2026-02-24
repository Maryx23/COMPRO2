public class Grades {
    private String subject;
    private double prelim;
    private double midterm;
    private double final_grade;

    public Grades (String subject, double prelim, double midterm, double final_grade) {
        this.subject = subject;
        this.prelim = prelim;
        this.midterm = midterm;
        this.final_grade = final_grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getPrelim() {
        return prelim;
    }

    public void setPrelim(double prelim) {
        this.prelim = prelim;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getFinal() {
        return final_grade;
    }

    public void setFinal(double final_grade) {
        this.final_grade = final_grade;
    }

    public String displayInfo() {
        return "Subject: " + subject + ", Prelim: " + prelim + ", Midterm: " + midterm + ", Final: " + final_grade;
    }

    public String toCSV() {
        return subject + "," + prelim + "," + midterm + "," + final_grade;
    }
}