package com.mr.draw; // �����ڵİ���

import javax.swing.JFrame; // ���봰����
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import com.mr.util.DrawImageUtil;
import com.mr.util.FrameGetShape;
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;

/**
 * ��ͼ������
 */

public class DrawPictureFrame extends JFrame implements FrameGetShape { // �̳д�����
	// ����һ��8λBGR��ɫ������ͼ��
	BufferedImage image = new BufferedImage(570, 390, BufferedImage.TYPE_INT_BGR);
	Graphics gs = image.getGraphics(); // ���ͼ��Ļ�ͼ����
	Graphics2D g = (Graphics2D) gs; // ����ͼ����ת��ΪGraphics2D����
	DrawPictureCanvas canvas = new DrawPictureCanvas(); // ������������
	Color foreColor = Color.BLACK; // ����ǰ��ɫ
	Color backgroundColor = Color.WHITE; // ���屳��ɫ
	int x = -1; // ��һ�������Ƶ�ĺ�����
	int y = -1; // ��һ�������Ƶ��������
	boolean rubber = false; // ��Ƥ��ʶ����
	private JToolBar toolBar; // ������
	private JButton eraserButton;// ��Ƥ��ť
	private JToggleButton strokeButton1; // ϸ�߰�ť
	private JToggleButton strokeButton2; // ���߰�ť
	private JToggleButton strokeButton3;// �ϴְ�ť
	private JButton backgroundButton; // ����ɫ��ť
	private JButton foregroundButton; // ǰ��ɫ��ť
	private JButton clearButton; // �����ť
	private JButton saveButton; // ���水ť
	private JButton shapeButton; // ͼ�ΰ�ť
	boolean drawShape = false; // ��ͼ�α�ʶ����
	Shapes shape; // ��ͼ��ͼ��

	/**
	 * ���췽������������������
	 */
	public DrawPictureFrame() {
		setResizable(false); // ���岻�ܸı��С
		setTitle("��ͼ����"); // ���ñ���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����ر���ֹͣ����
		setBounds(500, 100, 574, 460); // ���ô���λ�úͿ��
		init(); // �����ʼ��
		addListener(); // ����������
	} // DrawPictureFrame()����

	/**
	 * �����ʼ������������ӹ��������������ϵİ�ť
	 */
	private void init() {
		g.setColor(backgroundColor); // �ñ���ɫ���û�ͼ�������ɫ
		g.fillRect(0, 0, 570, 390); // �ñ���ɫ�����������
		g.setColor(foreColor); // ��ǰ��ɫ���û�ͼ�������ɫ
		canvas.setImage(image); // ���û�����ͼ��
		getContentPane().add(canvas); // ��������ӵ���������Ĭ�ϲ��ֵ��в�λ��

		toolBar = new JToolBar(); // ��ʼ��������
		getContentPane().add(toolBar, BorderLayout.NORTH);// ������������������λ��

		saveButton = new JButton("����"); // ��ʼ����ť���󣬲�����ı�����

		toolBar.add(saveButton); // ��������Ӱ�ť
		toolBar.addSeparator(); // ��ӷָ���
		// ��ʼ����ť���󣬲�����ı�����
		strokeButton1 = new JToggleButton("ϸ��");
		strokeButton1.setSelected(true);// ϸ�߰�ť���ڱ�ѡ��״̬
		toolBar.add(strokeButton1); // ��������Ӱ�ť
		// ��ʼ����ť���󣬲�����ı�����
		strokeButton2 = new JToggleButton("����");
		toolBar.add(strokeButton2);
		// ��ʼ����ѡ��״̬�İ�ť���󣬲�����ı�����
		strokeButton3 = new JToggleButton("�ϴ�");
		// ���ʴ�ϸ��ť�飬ͬʱ��ֻ֤��һ����ť��ѡ��
		ButtonGroup strokeGroup = new ButtonGroup();
		strokeGroup.add(strokeButton1); // ��ť����Ӱ�ť
		strokeGroup.add(strokeButton2); // ��ť����Ӱ�ť
		strokeGroup.add(strokeButton3); // ��ť����Ӱ�ť
		toolBar.add(strokeButton3); // ��������Ӱ�ť
		toolBar.addSeparator();// ��ӷָ�

		backgroundButton = new JButton("������ɫ"); // ��ʼ����ť���󣬲�����ı�����
		toolBar.add(backgroundButton); // ��������Ӱ�ť
		foregroundButton = new JButton("ǰ����ɫ");// ��ʼ����ť���󣬲�����ı�����
		toolBar.add(foregroundButton);// ��������Ӱ�ť
		toolBar.addSeparator(); // ��ӷָ���
		shapeButton = new JButton("ͼ��"); // ��ʼ����ť���󣬲�����ı�����
		toolBar.add(shapeButton); // ��������Ӱ�ť
		clearButton = new JButton("���"); // ��ʼ����ť���󣬲�����ı�����
		toolBar.add(clearButton);// ��������Ӱ�ť
		eraserButton = new JButton("��Ƥ"); // ��ʼ����ť���󣬲�����ı�����
		toolBar.add(eraserButton);// ��������Ӱ�ť

	}// init()����

	/**
	 * Ϊ�����Ӷ�����������ӡ�ϸ�ߡ���ť�������ߡ���ť�����ϴ֡���ť�Ķ�������,����������ť����Ƥ��ť�ļ���
	 */
	private void addListener() {
		// �����������ƶ��¼�����
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(final MouseEvent e) { // �������קʱ
				if (x > 0 && y > 0) { // ���x��y��������¼
					if (rubber) { // ��Ƥ��ʶΪtrue����ʾʹ����Ƥ
						g.setColor(backgroundColor); // ��ͼ����ʹ�ñ���ɫ
						g.fillRect(x, y, 10, 10); // ����껬����λ�û�����������
					} else { // �����Ƥ��ʶΪfalse����ʾ�û��ʻ�ͼ
						g.drawLine(x, y, e.getX(), e.getY()); // ����껬����λ�û�ֱ��
					} // else����
				} // if ����
				x = e.getX(); // ��һ�������Ƶ�ĺ�����
				y = e.getY(); // ��һ�������Ƶ��������
				canvas.repaint(); // ���»���
			}// mouseDragged()����
		}); // canvas.addMouseMotionListener()����
		canvas.addMouseListener(new MouseAdapter() { // ���������굥���¼�����
			public void mouseReleased(final MouseEvent arg0) {// �����̧��ʱ
				x = -1; // ����¼��һ�������Ƶ�ĺ�����ָ���-1
				y = -1; // ����¼��һ�������Ƶ��������ָ���-1
			} // mouseReleased()����

			public void mousePressed(MouseEvent e) {// ����������ʱ
				if (drawShape) { // �����ʱ��껭����ͼ��
					switch (shape.getType()) {// �ж�ͼ�ε�����
					case Shapes.YUAN:
						// �������꣬������괦��Բ�ε�����λ��
						int yuanX = e.getX() - shape.getWidth() / 2;
						int yuanY = e.getY() - shape.getHeigth() / 2;
						// ����Բ��ͼ�Σ���ָ������Ϳ��
						Ellipse2D yuan = new Ellipse2D.Double(yuanX, yuanY, shape.getWidth(), shape.getHeigth());
						g.draw(yuan); // ��ͼ���߻���Բ��
						break;
					case Shapes.FANG:
						// �������꣬������괦�ڷ��ε�����λ��
						int fangX = e.getX() - shape.getWidth() / 2;
						int fangY = e.getY() - shape.getHeigth() / 2;
						// ��������ͼ�Σ���ָ������Ϳ��
						Rectangle2D fang = new Rectangle2D.Double(fangX, fangY, shape.getWidth(), shape.getHeigth());
						g.draw(fang); // ��ͼ���߻���Բ��
						break;
					} // switch ����
					canvas.repaint();
					// ��ͼ�α�ʶ����Ϊfalse,˵��������껭����ͼ�Σ�����������
					drawShape = false;
				} // if����
			}// mousePressed()����
		});// canvas.addMouseListener()����

		strokeButton1.addActionListener(new ActionListener() { // "ϸ��"��ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				// �����������ԣ���ϸΪ1���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs); // ��ͼ����ʹ�ô˻���
			}// actionPerformed()����
		});// strokeButton1.addActionListener()����

		strokeButton2.addActionListener(new ActionListener() { // "����"��ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				// �����������ԣ���ϸΪ2���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs); // ��ͼ����ʹ�ô˻���
			}// actionPerformed()����
		});// strokeButton2.addActionListener()����

		strokeButton3.addActionListener(new ActionListener() { // "�ϴ�"��ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				// �����������ԣ���ϸΪ4���أ�����ĩ�������Σ����ߴ��ʼ��
				BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
				g.setStroke(bs); // ��ͼ����ʹ�ô˻���
			}// actionPerformed()����
		});// strokeButton3.addActionListener()����

		backgroundButton.addActionListener(new ActionListener() { // ������ɫ��ť��Ӷ�������

			public void actionPerformed(final ActionEvent arg0) {
				// ����ʱ��ѡ����ɫ�Ի��򣬲�������Ϊ�������塢���⡢Ĭ��ѡ�е���ɫ����ɫ��
				Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "ѡ����ɫ�Ի���", Color.CYAN);

				if (bgColor != null) { // ���ѡ�����ɫ���ǿյ�
					backgroundColor = bgColor; // ��ѡ�е���ɫ��������ɫ����
				}
				// ����ɫ��ťҲ����Ϊ���ֱ�����ɫ
				backgroundButton.setBackground(backgroundColor);
				g.setColor(backgroundColor); // ��ͼ����ʹ�ñ���ɫ
				g.fillRect(0, 0, 570, 390); // ��һ��������ɫ�ķ���������������
				g.setColor(foreColor);// ��ͼ����ʹ��ǰ��ɫ
				canvas.repaint();// ���»���
			}// actionPerformed()����
		});// backgroundButton.addActionListener()����

		foregroundButton.addActionListener(new ActionListener() { // ǰ��ɫ��ɫ��ť��Ӷ�������

			public void actionPerformed(final ActionEvent arg0) {
				// ����ʱ��ѡ����ɫ�Ի��򣬲�������Ϊ�������塢���⡢Ĭ��ѡ�е���ɫ����ɫ��
				Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "ѡ����ɫ�Ի���", Color.CYAN);

				if (fColor != null) { // ���ѡ�����ɫ���ǿյ�
					foreColor = fColor; // ��ѡ�е���ɫ����ǰ��ɫ����
				}
				// ǰ��ɫ��ťҲ����Ϊ���ֱ�����ɫ
				foregroundButton.setForeground(foreColor);
				g.setColor(foreColor); // ��ͼ����ʹ��ǰ��ɫ
			}// actionPerformed()����
		});// foregroundButton.addActionListener()����

		clearButton.addActionListener(new ActionListener() {// �����ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				g.setColor(backgroundColor); // ��ͼ����ʹ�ñ���ɫ
				g.fillRect(0, 0, 570, 390);// ��һ��������ɫ�ķ���������������
				g.setColor(foreColor); // ��ͼ����ʹ��ǰ��ɫ
				canvas.repaint(); // ���»���
			}// actionPerformed()����
		});// clearButton.addActionListener()����

		eraserButton.addActionListener(new ActionListener() {// ��Ƥ��ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				// �����������ϵ���Ƥ��ť��ʹ����Ƥ
				if (eraserButton.getText().equals("��Ƥ")) {
					rubber = true; // ������Ƥ��ʶΪtrue
					eraserButton.setText("��ͼ");// �ı䰴ť����ʾ���ı�Ϊ��ͼ
				} else { // �����������ϵĻ�ͼ��ť��ʹ�û���
					rubber = false;
					eraserButton.setText("��Ƥ");
					g.setColor(foreColor);
				} // else����
			}// actionPerformed()����
		});// eraserButton.addActionListener()����

		shapeButton.addActionListener(new ActionListener() {// ͼ�ΰ�ť��Ӷ�������
			public void actionPerformed(ActionEvent e) {// ����ʱ
				// ����ͼ��ѡ��ť
				ShapeWindow shapeWindow = new ShapeWindow(DrawPictureFrame.this);
				int shapeButtonWidth = shapeButton.getWidth(); // ��ȡͼ�ΰ�ť���
				int shapeWindowWidth = shapeWindow.getWidth();// ��ȡͼ�ΰ�ť�߶�
				int shapeButtonX = shapeButton.getX(); // ��ȡͼ�ΰ�ť������
				int shapeButtonY = shapeButton.getY(); // ��ȡͼ�ΰ�ť������
				// ����ͼ����������꣬������롰ͼ�Ρ���ť���ж���
				int ShapeWindowX = getX() + shapeButtonX - (shapeWindowWidth - shapeButtonWidth) / 2;
				// ����ͼ����������꣬�������ʾ�ڡ�ͼ�Ρ���ť�·�
				int ShapeWindowY = getY() + shapeButtonY + 80;
				// ����ͼ���������λ��
				shapeWindow.setLocation(ShapeWindowX, ShapeWindowY);
				shapeWindow.setVisible(true);// ͼ������ɼ�
			}// actionPerformed()����
		});// shapeButton.addActionListener()����

		saveButton.addActionListener(new ActionListener() {// ���水ť��Ӷ�������
			public void actionPerformed(final ActionEvent arg0) {// ����ʱ
				DrawImageUtil.saveImage(DrawPictureFrame.this, image); // ��ӡͼƬ
			}
		});

	}// addListener()����

	/**
	 * FrameGetShape�ӿ�ʵ���࣬���ڻ�ȡͼ�οռ䷵�صı�ѡ��ͼ��
	 */
	public void getShape(Shapes shape) {
		this.shape = shape; // �����ص�ͼ�ζ��󸳸����ȫ�ֱ���
		drawShape = true; // ��ͼ�α�ʶ����Ϊtrue,˵��������껭����ͼ�Σ�����������
	}

	/**
	 * ��������������
	 */
	public static void main(String[] args) {
		DrawPictureFrame frame = new DrawPictureFrame(); // �����������
		frame.setVisible(true); // �ô���ɼ�
	} // main()����
} // DrawPictureFrame�����
