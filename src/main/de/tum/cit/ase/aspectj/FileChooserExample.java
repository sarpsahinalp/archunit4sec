package de.tum.cit.ase.aspectj;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserExample extends JFrame {
    private JButton openButton;
    private JTextArea log;
    private JFileChooser fileChooser;

    public FileChooserExample() {
        setTitle("File Chooser Example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        log = new JTextArea();
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        add(logScrollPane, BorderLayout.CENTER);

        openButton = new JButton("Open File Chooser");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFileChooserDialog();
            }
        });
        add(openButton, BorderLayout.PAGE_START);

        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        pack();
        setLocationRelativeTo(null); // Center the window
    }

    private void showFileChooserDialog() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            log.append("Selected file: " + file.getAbsolutePath() + "\n");
        } else {
            log.append("Open command cancelled by user.\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FileChooserExample().setVisible(true);
            }
        });
    }
}

