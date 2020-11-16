package com.dzcx.netdisk.util.entity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.dzcx.netdisk.util.NetworkImp;
import com.sun.management.OperatingSystemMXBean;


public class Error extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	int xOld = 0, yOld = 0;
	private JPanel main, info, title, btn;
	private JButton exit;
	private JTextArea textArea;
	private JButton report;

	public Error() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Error.class.getResource("photo/" + "error.png")));
		setSize(700, 400);
		setUndecorated(true);
		setLocationRelativeTo(null);

		main = new JPanel();
		main.setBorder(new LineBorder(Color.PINK));
		getContentPane().add(main, BorderLayout.CENTER);
		main.setLayout(new BorderLayout(0, 0));

		title = new JPanel();
		title.setBorder(new EmptyBorder(10, 0, 0, 0));
		main.add(title, BorderLayout.NORTH);

		JLabel label = new JLabel("\u9519\u8bef");
		label.setIcon(new ImageIcon(Error.class.getResource("photo/" + "error.png")));
		GroupLayout gl_title = new GroupLayout(title);
		gl_title.setHorizontalGroup(gl_title.createParallelGroup(Alignment.LEADING).addGroup(gl_title
				.createSequentialGroup().addContainerGap().addComponent(label).addContainerGap(633, Short.MAX_VALUE)));
		gl_title.setVerticalGroup(
				gl_title.createParallelGroup(Alignment.LEADING).addGroup(gl_title.createSequentialGroup()
						.addComponent(label).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		title.setLayout(gl_title);

		info = new JPanel();
		main.add(info, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_info = new GroupLayout(info);
		gl_info.setHorizontalGroup(gl_info.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_info.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE).addContainerGap()));
		gl_info.setVerticalGroup(gl_info.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE));

		textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setEditable(false);
		textArea.setFont(new Font("Simsun", Font.BOLD, 13));
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setViewportView(textArea);
		info.setLayout(gl_info);

		btn = new JPanel();
		btn.setBorder(new EmptyBorder(0, 0, 10, 0));
		main.add(btn, BorderLayout.SOUTH);

		exit = new JButton("\u9000\u51fa");
		exit.addActionListener(this);

		report = new JButton("\u53cd\u9988\u9519\u8bef");
		report.addActionListener(this);
		
		GroupLayout gl_btn = new GroupLayout(btn);
		gl_btn.setHorizontalGroup(gl_btn.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_btn.createSequentialGroup().addContainerGap().addComponent(report)
						.addPreferredGap(ComponentPlacement.RELATED, 528, Short.MAX_VALUE).addComponent(exit)
						.addContainerGap()));
		gl_btn.setVerticalGroup(gl_btn.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_btn.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(
						gl_btn.createParallelGroup(Alignment.BASELINE).addComponent(exit).addComponent(report))));
		btn.setLayout(gl_btn);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xOld = e.getX();
				yOld = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Error.this.setLocation(e.getXOnScreen() - xOld, e.getYOnScreen() - yOld);
			}
		});
	}

	public void error(String errorData) {
		textArea.setText(errorData);
	}


	
	private static String getCpu() {
		try {
			Process process = Runtime.getRuntime().exec("wmic cpu get name");
			process.getOutputStream().close();
			Scanner sc = new Scanner(process.getInputStream());
			String cpuName = "";
			while (sc.hasNext()) {
				cpuName += sc.next() + " ";
			}
			sc.close();
			return cpuName.substring(cpuName.indexOf(" ") + 1, cpuName.length() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static int getSystemMemorySize() {
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long size = osmb.getTotalPhysicalMemorySize();
        size = size / 1024 / 1024;
        return Integer.parseInt(size + "");
    }

	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object == report) {
			try {
				String param =
						"&cpu=" + getCpu() +
								"&memory=" + getSystemMemorySize() +
								"&os=" + System.getProperty("os.name") +
								"&jvm=" + System.getProperty("java.version") +
								"&date=" + new SimpleDateFormat("yyyy-MM-dd H:mm:ss").format(new Date()) +
								"&data=" + textArea.getText()
						;
				int i = JOptionPane.showConfirmDialog(null, "\u5373\u5c06\u6536\u96c6\u4f60\u7684\u4ee5\u4e0b\u6570\u636e\uff1a\n\u3000\u3000\u5904\u7406\u5668\u578b\u53f7\n\u3000\u3000\u6700\u5927\u5185\u5b58\n\u3000\u3000\u7cfb\u7edf\u7248\u672c\n\u3000\u3000JVM \u865a\u62df\u673a\u7248\u672c\n\u3000\u3000\u62a5\u9519\u4ee3\u7801\n\n\u6536\u96c6\u8fd9\u4e9b\u6570\u636e\u4ec5\u7528\u4e8e\u5b8c\u5584\u672c\u7a0b\u5e8f", "\u6570\u636e\u6536\u96c6\u8bf4\u660e", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {
					new NetworkImp().doPost("https://www.imyeyu.net/java/report", param);
					JOptionPane.showMessageDialog(null, "\u53cd\u9988\u6210\u529f\uff0c\u611f\u8c22\u63d0\u4ea4", "", JOptionPane.DEFAULT_OPTION);
					System.exit(0);
				}
			} catch (ConnectException e) {
				JOptionPane.showMessageDialog(null, "\u53cd\u9988\u5931\u8d25\uff1a\u65e0\u6cd5\u8fde\u63a5\u53cd\u9988\u670d\u52a1\u5668_CONNECT_ECX", "", JOptionPane.DEFAULT_OPTION);
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "\u53cd\u9988\u5931\u8d25\uff1a\u65e0\u6cd5\u8fde\u63a5\u53cd\u9988\u670d\u52a1\u5668_HOST_ECX", "", JOptionPane.DEFAULT_OPTION);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "\u53cd\u9988\u5931\u8d25\uff1a\u53cd\u9988\u670d\u52a1\u5668\u5f02\u5e38_IO_ECX", "", JOptionPane.DEFAULT_OPTION);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "\u53cd\u9988\u5931\u8d25\uff1a\u7a0b\u5e8f\u5f02\u5e38_ECX", "", JOptionPane.DEFAULT_OPTION);
			}
		}
		if (object == exit) {
			System.exit(0);
		}
	}
}
