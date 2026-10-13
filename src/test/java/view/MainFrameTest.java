package view;

import org.junit.jupiter.api.Test;

import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.*;

class MainFrameTest {

    @Test
    void buildsWithExpectedTitleAndButtons() {
        // The MainController is only dereferenced from action/window listeners, which this
        // construction-only test never triggers, so a null controller is safe here.
        MainFrame frame = new MainFrame(null);

        assertEquals("Contact Management System", frame.getTitle());

        JButton contactsButton = SwingTestUtils.getField(frame, "contactsButton");
        JButton groupsButton = SwingTestUtils.getField(frame, "groupsButton");
        assertEquals("Contacts", contactsButton.getText());
        assertEquals("Groups", groupsButton.getText());
    }
}
