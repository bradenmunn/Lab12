import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Panel that holds and displays a signature as a list of points.
 *
 * @author Stephen
 * @version 2019-04-24
 */
public class SignaturePanel extends JPanel
{
	private int pointRadius;
	private ArrayList<Point> signature;

	public SignaturePanel()
	{
		this(4);
	}

	public SignaturePanel(int radius)
	{
		signature = new ArrayList<>();
		pointRadius = radius;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void addPoint(Point p)
	{
		signature.add(p);
	}

	public void clear()
	{
		signature.clear();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);

		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(5));

		for (Point pt : signature) g2d.fillOval(pt.x - pointRadius/2, pt.y - pointRadius/2, pointRadius, pointRadius);
	}

	public List<Point> getSignature()
	{
		return signature;
	}

	public void setSignature(List<Point> sign)
	{
		this.signature = new ArrayList<Point>(sign);
		this.repaint();
	}
}