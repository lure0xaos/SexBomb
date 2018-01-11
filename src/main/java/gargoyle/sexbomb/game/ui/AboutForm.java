package gargoyle.sexbomb.game.ui;

import gargoyle.sexbomb.util.log.Log;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class AboutForm extends JDialog {
    private static final String RES_COPYRIGHT = "res/copyright.html";
    private static final String STR_OK = "OK";

    public AboutForm(Frame frame) {
        super(frame, true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        init(frame);
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() / 2));
        pack();
        setLocation(frame.getX(), frame.getY() + (frame.getHeight() - getHeight()) / 2);
    }

    private void destroy() {
        setVisible(false);
        dispose();
    }

    private void init(Frame frame) {
        setTitle(frame.getTitle());
        setLayout(new BorderLayout());
        try {
            JEditorPane content = new JEditorPane(AboutForm.class.getResource(RES_COPYRIGHT));
            content.setEditable(false);
            add(content, BorderLayout.CENTER);
        } catch (IOException e) {
            Log.error(e.getLocalizedMessage(), e);
        }
        JLabel header = new JLabel(getTitle());
        header.setFont(getFont().deriveFont(getFont().getSize2D() * 2));
        add(header, BorderLayout.NORTH);
        add(new JButton(new AbstractAction(STR_OK) {
            @Override
            public void actionPerformed(ActionEvent e) {
                destroy();
            }
        }), BorderLayout.SOUTH);
        invalidate();
    }
}
