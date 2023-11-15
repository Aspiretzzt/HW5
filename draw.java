/*
 * name: Zhongtian Zhai
 * zzhai4@u.rochester.edu
 * last modified at 14 Nov,2023
 * Assignment name: draw
 * Lab section: CSC 171-23
 */

import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class MySlider extends JSlider {
	public MySlider() {
		super(JSlider.HORIZONTAL, 0, 100, 50);
		this.setMajorTickSpacing(10);
		this.setMinorTickSpacing(5);
		this.setPaintTicks(true);
		this.setPaintLabels(true);
	}
}

class Circle extends JPanel implements ActionListener, ChangeListener {
	private int status = 1;
	private int value = 50;
	protected Timer timer;
	private int inix = 300;
	private int iniy = 300;
	private int delx = 1;
	private int dely = 1;
	private int cirdelx = 0;
	private int cirdely = 0;
	private double theta = 0.0;
	private double delt = 0.1;
	private int x = inix;
	private int y = iniy;
	private Boolean refresh = true;
	private int radius;
	private int red, green, blue = 0;
	private int count = 0;
	private store[] cirstore;
	private int timeDelay = 50;

	public Circle() {
		timer = new Timer(timeDelay, new TimerCallback());
		timer.start();
		cirstore = new store[100];
		for (int i = 0; i < 100; i++) {
			cirstore[i] = new store(0, 0, 0, 0, 0, 0);
		}
		setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (status != 3) {
			g.setColor(Color.BLACK);
			if (refresh == true) {
				g.fillOval(inix, iniy, 20, 20);
			} else {
				g.fillOval(x, y, 20, 20);
			}
		}
		if (status == 3 && value !=0) {
			for (int i = 0; i < count; i++) {
				g.setColor(new Color(cirstore[i].r, cirstore[i].g, cirstore[i].b));
				g.fillOval(cirstore[i].x, cirstore[i].y, cirstore[i].ra * 2, cirstore[i].ra * 2);
			}
			for (int j = count; j < 100; j++) {
				cirstore[j] = new store(0, 0, 0, 0, 0, 0);
			}
		}

	}

	protected class TimerCallback implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			switch (status) {
			case 1:
				if (refresh == true) {
					x = inix;
					y = iniy;
					delx = 1;
					dely = 1;
					repaint();
					refresh = false;
				} else {
					if (x < 0 || x > 630)
						delx *= -1;
					if (y < 0 || y > 601)
						dely *= -1;
					x += delx;
					y += dely;
					repaint();
				}
				break;
			case 2:
				if (refresh == true) {
					cirdelx = 0;
					cirdely = 0;
					theta = 0.0;
					x = inix;
					y = iniy;
					repaint();
					refresh = false;
				} else {
					cirdelx = (int) (Math.sqrt(20000) * (Math.cos(theta + delt) - Math.cos(theta)));
					cirdely = (int) (Math.sqrt(20000) * (Math.sin(theta + delt) - Math.sin(theta)));
					x += cirdelx;
					y += cirdely;
					theta += delt;
					if (theta > 2 * Math.PI) {
						theta = 0.0;
						x = inix;
						y = iniy;
					}
					repaint();
				}
				break;
			case 3:
				if (refresh == true) {
					count = 0;
					repaint();
					refresh = false;
				} else {
					if (value == 0)
					{
						count=0;
						repaint();
					}
					if (count >= value)
						count = 0;
					Random random = new Random();
					x = random.nextInt(600);
					y = random.nextInt(550);
					radius = random.nextInt(20) + 5;
					red = random.nextInt(255);
					green = random.nextInt(255);
					blue = random.nextInt(255);
					cirstore[count] = new store(x, y, radius, red, green, blue);
					count++;
					repaint();

				}
				break;
			}
		}
	}

	public void setSpeed(int v) {
		if (status != 3) {
			this.timeDelay = v;
			if (timer != null) {
				timer.stop();
				timer = new Timer(timeDelay, new TimerCallback());
				timer.start();
			}
		}
		if (status == 3) {
			timer.stop();
			timer = new Timer(100, new TimerCallback());
			timer.start();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		if (ac.equals("Mode 1")) {
			status = 1;
			setSpeed(value);
		}

		if (ac.equals("Mode 2")) {
			status = 2;
			setSpeed(value);
		}

		if (ac.equals("Mode 3")) {
			status = 3;
			setSpeed(value);
		}
		refresh = true;
		repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider info = (JSlider) e.getSource();
		if (!info.getValueIsAdjusting()) {
			value = info.getValue();
			setSpeed(value);
		}
	}

	public void attachToButton(JButton button) {
		button.addActionListener(this);
	}

	public void attachToSlider(JSlider slider) {
		slider.addChangeListener(this);
	}
}

class store {
	int x, y, ra, r, g, b;

	public store(int x, int y, int ra, int r, int g, int b) {
		this.x = x;
		this.y = y;
		this.ra = ra;
		this.r = r;
		this.g = g;
		this.b = b;
	}
}

public class draw {

	public static void main(String[] args) {

		JFrame frame = new JFrame("Draw");
		frame.setBounds(100, 100, 650, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		panel1.setBackground(Color.GRAY);
		panel1.setPreferredSize(new Dimension(650, 150));

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setPreferredSize(new Dimension(650, 50));
		buttonPanel.setBackground(Color.GRAY);
		JButton button1 = new JButton("Mode 1");
		JButton button2 = new JButton("Mode 2");
		JButton button3 = new JButton("Mode 3");
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);

		MySlider slider = new MySlider();
		slider.setPreferredSize(new Dimension(450, 50));

		Circle c = new Circle();
		c.attachToButton(button1);
		c.attachToButton(button2);
		c.attachToButton(button3);
		c.attachToSlider(slider);

		panel1.add(buttonPanel, BorderLayout.NORTH);
		panel1.add(slider, BorderLayout.CENTER);
		frame.add(panel1, BorderLayout.NORTH);
		frame.add(c, BorderLayout.CENTER);
		frame.setVisible(true);
	}
}