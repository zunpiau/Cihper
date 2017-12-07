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
import java.util.Locale;

public class Application {

    private static String[] method = new String[]{"CaesarCipher", "PlayfairCipher", "HillCipher"};
    private static CaesarCipher caesarCipher = new CaesarCipher (3);
    private static PlayfairCipher playfairCipher = new PlayfairCipher ("monarchy");
    private static HillCipher hillCipher = new HillCipher ("17,17,5,21,18,21,2,2,19");
    private static Cipher[] ciphers = new Cipher[]{caesarCipher, playfairCipher, hillCipher};


    public static void main(String[] args) {

        setSystemProperty ();
        javax.swing.SwingUtilities.invokeLater (Application::createAndShowGUI);
    }

    private static void createAndShowGUI() {

        JFrame frame = new JFrame ("cipher.Cipher");

        JTextArea plainTextArea = new JTextArea (8, 24);
        plainTextArea.setLineWrap (true);
        plainTextArea.setWrapStyleWord (true);

        JLabel caesarParamLabel = new JLabel ("Offset of Caesar: ", JLabel.LEFT);
        SpinnerModel model = new SpinnerNumberModel (3, 1, 25, 1);
        JSpinner caesarParamSpinner = new JSpinner (model);
        caesarParamSpinner.setEditor (new JSpinner.DefaultEditor (caesarParamSpinner));
        caesarParamSpinner.addChangeListener (e -> caesarCipher.setOffset ((Integer) caesarParamSpinner.getValue ()));

        JLabel playfairParamLabel = new JLabel ("Keyword of Playfair: ", JLabel.LEFT);
        JTextField playfairParamTextField = new JTextField ("monarchy");
        playfairParamTextField.addActionListener (e -> playfairCipher.setKeyword (playfairParamTextField.getText ()));

        JLabel hillParamLabel = new JLabel ("Key of Hill: ", JLabel.LEFT);
        JButton hillParamButton = new JButton ("CHANGE");
        playfairParamTextField.addActionListener (e -> playfairCipher.setKeyword (playfairParamTextField.getText ()));

        JComboBox<String> methodComboBox = new JComboBox<> (method);

        JButton encryptButton = new JButton ();
        JButton decryptButton = new JButton ();

        JPanel controlPanel = new JPanel ();
        controlPanel.setLayout (new GridBagLayout ());
        GridBagConstraints constraints = new GridBagConstraints ();

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        controlPanel.add (caesarParamLabel, constraints);
        constraints.gridy++;
        controlPanel.add (caesarParamSpinner, constraints);

        constraints.gridy++;
        constraints.insets = new Insets (16, 0, 0, 0);
        controlPanel.add (playfairParamLabel, constraints);
        constraints.insets = new Insets (0, 0, 0, 0);
        constraints.gridy++;
        controlPanel.add (playfairParamTextField, constraints);

        constraints.gridy++;
        constraints.insets = new Insets (16, 0, 0, 0);
        controlPanel.add (hillParamLabel, constraints);
        constraints.gridy++;
        constraints.insets = new Insets (0, 0, 0, 0);
        controlPanel.add (hillParamButton, constraints);

        constraints.gridy++;
        constraints.insets = new Insets (32, 0, 0, 0);
        controlPanel.add (methodComboBox, constraints);
        constraints.insets = new Insets (8, 0, 0, 0);
        constraints.gridy++;
        controlPanel.add (encryptButton, constraints);
        constraints.gridy++;
        controlPanel.add (decryptButton, constraints);
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
        hillParamButton.setAction (new AbstractAction () {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object input = JOptionPane.showInputDialog (frame,
                        "input nine numbers separated by ','",
                        "Key",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        hillCipher.getKey ());
                if (input != null && !hillCipher.getKey ().equals (input)) {
                    try {
                        hillCipher.setKey (String.valueOf (input));
                    } catch (IllegalArgumentException exception) {
                        JOptionPane.showMessageDialog (frame,
                                exception.getMessage ());
                    }
                }
            }
        });
        hillParamButton.setText ("CHANGE");

        JPanel mainPanel = new JPanel ();
        mainPanel.setLayout (new BoxLayout (mainPanel, BoxLayout.X_AXIS));
        mainPanel.add (new JScrollPane (plainTextArea));
        mainPanel.add (controlPanel);
        mainPanel.add (new JScrollPane (cipherTestArea));

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
        Locale.setDefault (Locale.ENGLISH);
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
