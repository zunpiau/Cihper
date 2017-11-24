import cipher.CaesarCipher;
import cipher.Cipher;
import cipher.HillCipher;
import cipher.PlayfairCipher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import static java.awt.Component.CENTER_ALIGNMENT;

public class Application {

    private static String[] method = new String[]{"CaesarCipher", "PlayfairCipher", "HillCipher"};
    private static Cipher[] ciphers = new Cipher[]{new CaesarCipher (3),
            new PlayfairCipher (),
            new HillCipher ()};

    public static void main(String[] args) {

        setSystemProperty ();
        javax.swing.SwingUtilities.invokeLater (Application::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        JTextArea plainTextArea = new JTextArea (8, 24);
        plainTextArea.setLineWrap (true);
        plainTextArea.setWrapStyleWord (true);

        JComboBox<String> methodComboBox = new JComboBox<> (method);
        methodComboBox.setAlignmentX (CENTER_ALIGNMENT);
        methodComboBox.setMaximumSize (methodComboBox.getPreferredSize ());

        JButton encryptButton = new JButton ();
        encryptButton.setAlignmentX (CENTER_ALIGNMENT);

        JButton decryptButton = new JButton ();
        decryptButton.setAlignmentX (CENTER_ALIGNMENT);

        JPanel controlPanel = new JPanel ();
        controlPanel.setLayout (new BoxLayout (controlPanel, BoxLayout.Y_AXIS));
        controlPanel.add (methodComboBox);
        controlPanel.add (Box.createVerticalStrut (8));
        controlPanel.add (encryptButton);
        controlPanel.add (Box.createVerticalStrut (8));
        controlPanel.add (decryptButton);
        controlPanel.setBorder (new EmptyBorder (72, 8, 72, 8));
        controlPanel.setMaximumSize (new Dimension (controlPanel.getPreferredSize ().width, 1080));

        JTextArea cipherTestArea = new JTextArea (8, 24);
        cipherTestArea.setLineWrap (true);
        cipherTestArea.setWrapStyleWord (true);

        encryptButton.setAction (new AbstractAction () {
            @Override
            public void actionPerformed(ActionEvent e) {
                cipherTestArea.setText (encrypt (methodComboBox.getSelectedIndex (),
                        plainTextArea.getText ()));
            }
        });
        encryptButton.setText ("Encrypt >>");
        decryptButton.setAction (new AbstractAction () {
            @Override
            public void actionPerformed(ActionEvent e) {
                plainTextArea.setText (decrypt (methodComboBox.getSelectedIndex (),
                        cipherTestArea.getText ()));
            }
        });
        decryptButton.setText ("<< Decrypt");

        JPanel mainPanel = new JPanel ();
        mainPanel.setLayout (new BoxLayout (mainPanel, BoxLayout.X_AXIS));
        mainPanel.add (new JScrollPane (plainTextArea));
        mainPanel.add (controlPanel);
        mainPanel.add (new JScrollPane (cipherTestArea));

        JFrame frame = new JFrame ("cipher.Cipher");
        frame.setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane (mainPanel);
        frame.setMinimumSize (new Dimension (frame.getPreferredSize ().width + 32,
                frame.getPreferredSize ().height + 32));
        frame.setLocationRelativeTo (null);
        frame.pack ();
        frame.setVisible (true);
    }

    private static String encrypt(int methodIndex, String input) {
        return ciphers[methodIndex].encrypt (input);
    }

    private static String decrypt(int methodIndex, String input) {
        return ciphers[methodIndex].decrypt (input);
    }

    private static void setSystemProperty() {
        System.setProperty ("awt.useSystemAAFontSettings", "on");
        System.setProperty ("swing.aatext", "true");
        JFrame.setDefaultLookAndFeelDecorated (false);
        FontUIResource fontRes = new FontUIResource (new Font ("Source Code Pro", Font.PLAIN, 16));
        for (Enumeration<Object> keys = UIManager.getDefaults ().keys ();
             keys.hasMoreElements (); ) {
            Object key = keys.nextElement ();
            Object value = UIManager.get (key);
            if (value instanceof FontUIResource) {
                UIManager.put (key, fontRes);
            }
        }
    }

}
