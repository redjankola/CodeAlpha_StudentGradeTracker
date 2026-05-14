import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GradeTracker extends JFrame {

    private JLabel inpNota;
    private JTextArea informacioni;
    private JTextField nota, emriField, kerkimiField;
    private JButton shtoNote, average, notat, regjistroBtn, kerkoBtn;

    private static final String SKEDARI = //---Enter your desired file path here to store the data---//
    GradeTracker() {
        setTitle("Grade Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 40));
        shtoKomponente();
        setVisible(true);
    }


    private Map<String, List<Double>> lexoTeDhenat() {
        Map<String, List<Double>> mapa = new LinkedHashMap<>();
        File f = new File(SKEDARI);
        if (!f.exists()) return mapa;

        try (BufferedReader reader = new BufferedReader(new FileReader(SKEDARI))) {
            String rresht;
            while ((rresht = reader.readLine()) != null) {
                rresht = rresht.trim();
                if (rresht.isEmpty()) continue;

                String[] parts = rresht.split(":", 2);

                String emri = parts[0].trim();
                List<Double> notat = new ArrayList<>();

                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    for (String g : parts[1].split(",")) {
                        g = g.trim();
                        if (!g.isEmpty()) {
                            notat.add(Double.parseDouble(g));
                        }
                    }
                }

                mapa.put(emri, notat);
            }
        } catch (IOException ex) {
            informacioni.setText("Could not read file: " + ex.getMessage());
        }

        return mapa;
    }

    //Writes the grades
    private void shkruajTeDhenat(Map<String, List<Double>> mapa) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SKEDARI, false))) {
            for (Map.Entry<String, List<Double>> entry : mapa.entrySet()) {
                StringBuilder sb = new StringBuilder(entry.getKey()).append(":");
                List<Double> lista = entry.getValue();
                for (int i = 0; i < lista.size(); i++) {
                    sb.append(lista.get(i));
                    if (i < lista.size() - 1) sb.append(",");
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ex) {
            informacioni.setText("Error writing file: " + ex.getMessage());
        }
    }

    //style
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField styledTextField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setBackground(new Color(50, 50, 65));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(80, 80, 110), 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(180, 180, 210));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    // User Interface
    void shtoKomponente() {
        Color bg = new Color(30, 30, 40);

        // ── TOP: register student + add grade ──
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(bg);
        topPanel.setBorder(new EmptyBorder(12, 16, 8, 16));

        // Row 1: name
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        nameRow.setBackground(bg);
        nameRow.add(styledLabel("Student Name:"));
        emriField = styledTextField(14);
        nameRow.add(emriField);

        regjistroBtn = styledButton("Register", new Color(70, 130, 180));
        regjistroBtn.addActionListener(e -> {
            String emri = emriField.getText().trim();
            if (emri.isEmpty()) { showMsg("Enter a name first!"); return; }
            Map<String, List<Double>> mapa = lexoTeDhenat();
            if (mapa.containsKey(emri)) {
                showMsg("Student \"" + emri + "\" already exists!");
            } else {
                mapa.put(emri, new ArrayList<>());
                shkruajTeDhenat(mapa);
                showMsg("Student \"" + emri + "\" registered!");
            }
        });
        nameRow.add(regjistroBtn);
        topPanel.add(nameRow);

        // Row 2: grade
        JPanel gradeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        gradeRow.setBackground(bg);
        inpNota = styledLabel("Add Grade:");
        gradeRow.add(inpNota);
        nota = styledTextField(8);
        //placeholder
        String placeholder = "From 4-10";
        nota.setText(placeholder);
        nota.setForeground(Color.GRAY);
        nota.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (nota.getText().equals(placeholder)) {
                    nota.setText("");
                    nota.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (nota.getText().isEmpty()) {
                    nota.setText(placeholder);
                    nota.setForeground(Color.GRAY);
                }
            }
        });
        gradeRow.add(nota);

        shtoNote = styledButton("Add Grade", new Color(60, 160, 100));
        shtoNote.addActionListener(e -> {
            String emri = emriField.getText().trim();
            String teksti = nota.getText().trim();
            int grade = Integer.parseInt(nota.getText().trim());
            if(grade > 10 || grade < 4){ showMsg("Grade must be between 4 and 10!"); return; }
            if (emri.isEmpty()) { showMsg("Enter a student name first!"); return; }
            if (teksti.isEmpty()) { showMsg("Enter a grade!"); return; }
            try {
                double vlera = Double.parseDouble(teksti);
                Map<String, List<Double>> mapa = lexoTeDhenat();
                if (!mapa.containsKey(emri)) {
                    showMsg("Student not found! Register first.");
                    return;
                }
                mapa.get(emri).add(vlera);
                shkruajTeDhenat(mapa);
                nota.setText("");
                informacioni.setText("Grade " + vlera + " added for \"" + emri + "\".");
            } catch (NumberFormatException ex) {
                showMsg("Grade must be a number!");
            }
        });
        gradeRow.add(shtoNote);
        topPanel.add(gradeRow);

        // Row 3: search
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        searchRow.setBackground(bg);
        searchRow.add(styledLabel("Search Student:"));
        kerkimiField = styledTextField(14);
        searchRow.add(kerkimiField);
        kerkoBtn = styledButton("Search", new Color(160, 100, 200));
        kerkoBtn.addActionListener(e -> {
            String emri = kerkimiField.getText().trim();
            if (emri.isEmpty()) { informacioni.setText("Enter a name to search!"); return; }
            Map<String, List<Double>> mapa = lexoTeDhenat();
            if (!mapa.containsKey(emri)) {
                informacioni.setText("Student \"" + emri + "\" not found.");
                return;
            }
            List<Double> lista = mapa.get(emri);
            double shuma = lista.stream().mapToDouble(Double::doubleValue).sum();
            double avg = lista.isEmpty() ? 0 : shuma / lista.size();
            StringBuilder sb = new StringBuilder();
            sb.append("Student: ").append(emri).append("\n");
            sb.append("Grades:  ");
            if (lista.isEmpty()) sb.append("none");
            else for (int i = 0; i < lista.size(); i++) {
                sb.append(lista.get(i));
                if (i < lista.size() - 1) sb.append(", ");
            }
            sb.append("\nSum:     ").append(String.format("%.2f", shuma));
            sb.append("\nAverage: ").append(String.format("%.2f", avg));
            informacioni.setText(sb.toString());
        });
        searchRow.add(kerkoBtn);
        topPanel.add(searchRow);

        add(topPanel, BorderLayout.NORTH);

        // ── CENTER: output area ──
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(bg);
        centerPanel.setBorder(new EmptyBorder(0, 16, 8, 16));
        informacioni = new JTextArea(8, 40);
        informacioni.setEditable(false);
        informacioni.setBackground(new Color(22, 22, 32));
        informacioni.setForeground(new Color(200, 230, 200));
        informacioni.setFont(new Font("Consolas", Font.PLAIN, 14));
        informacioni.setBorder(new EmptyBorder(10, 12, 10, 12));
        JScrollPane scroll = new JScrollPane(informacioni);
        scroll.setBorder(new LineBorder(new Color(70, 70, 100), 1, true));
        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ── BOTTOM: show all / average all ──
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panel3.setBackground(new Color(22, 22, 32));
        panel3.setBorder(new MatteBorder(1, 0, 0, 0, new Color(60, 60, 80)));

        average = styledButton("All Averages", new Color(200, 140, 40));
        average.addActionListener(e -> {
            Map<String, List<Double>> mapa = lexoTeDhenat();
            if (mapa.isEmpty()) { informacioni.setText("No students registered!"); return; }
            StringBuilder sb = new StringBuilder("── Averages ──\n");
            for (Map.Entry<String, List<Double>> entry : mapa.entrySet()) {
                List<Double> l = entry.getValue();
                double avg = l.isEmpty() ? 0 : l.stream().mapToDouble(Double::doubleValue).sum() / l.size();
                sb.append(String.format("%-20s %.2f%n", entry.getKey(), avg));
            }
            informacioni.setText(sb.toString());
        });
        panel3.add(average);

        notat = styledButton("Show All Grades", new Color(70, 130, 180));
        notat.addActionListener(e -> {
            Map<String, List<Double>> mapa = lexoTeDhenat();
            if (mapa.isEmpty()) { informacioni.setText("No students registered!"); return; }
            StringBuilder sb = new StringBuilder("── All Grades ──\n");
            for (Map.Entry<String, List<Double>> entry : mapa.entrySet()) {
                sb.append(entry.getKey()).append(": ");
                List<Double> l = entry.getValue();
                if (l.isEmpty()) sb.append("no grades");
                else for (int i = 0; i < l.size(); i++) {
                    sb.append(l.get(i));
                    if (i < l.size() - 1) sb.append(", ");
                }
                sb.append("\n");
            }
            informacioni.setText(sb.toString());
        });
        panel3.add(notat);

        add(panel3, BorderLayout.SOUTH);
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        new GradeTracker();
    }
}
