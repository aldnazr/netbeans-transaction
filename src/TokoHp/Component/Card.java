package TokoHp.Component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Card extends JPanel {

    private int corner = 20;

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /*
        GradientPaint gradient = new GradientPaint(0, 0, getForeground(), getWidth(), 0, new Color(240, 240, 240));
        g2d.setPaint(gradient);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getCorner(), getCorner()));
         */
        g2d.setColor(getForeground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), getCorner(), getCorner());

        g2d.dispose();
    }
}
