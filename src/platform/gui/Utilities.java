package platform.gui;

import java.awt.Container;
import java.awt.FontMetrics;

import java.io.File;

import java.text.BreakIterator;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class Utilities {
    public static void wrapLabelText(JLabel label, String text) {
        FontMetrics fm = label.getFontMetrics(label.getFont());
        Container container = label.getParent();
        int containerWidth = container.getWidth();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);

        StringBuffer trial = new StringBuffer();
        StringBuffer real = new StringBuffer("<html>");

        int start = boundary.first();

        for (int end = boundary.next(); end != BreakIterator.DONE;
                start = end, end = boundary.next()) {
            String word = text.substring(start, end);
            trial.append(word);

            int trialWidth = SwingUtilities.computeStringWidth(fm, trial.toString());

            if (trialWidth > containerWidth) {
                trial = new StringBuffer(word);
                real.append("<br>");
            }

            real.append(word);
        }

        real.append("</html>");

        label.setText(real.toString());
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if ((i > 0) && (i < (s.length() - 1))) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return ext;
    }
}
