import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class ByteArrayQueue {
	private byte[] array;
	private int offset = 0;
	private int length = 0;

	public ByteArrayQueue() {
		this(32);
	}

	public ByteArrayQueue(int capacity) {
		array = new byte[capacity];
	}

	public byte[] array() {
		return array;
	}

	public int offset() {
		return offset;
	}

	public int length() {
		return length;
	}

	public void clear() {
		offset = 0;
		length = 0;
	}

	public void setCapacity(int capacity) {
		byte[] newArray = new byte[capacity];
		System.arraycopy(array, offset, newArray, 0, length);
		array = newArray;
		offset = 0;
	}

	public void add(byte[] b) {
		add(b, 0, b.length);
	}

	public void add(byte[] b, int off, int len) {
		int newLength = length + len;
		if (newLength > array.length) {
			setCapacity(Math.max(array.length << 1, newLength));
		} else if (offset + newLength > array.length) {
			System.arraycopy(array, offset, array, 0, length);
			offset = 0;
		}
		System.arraycopy(b, off, array, offset + length, len);
		length = newLength;
	}

	public OutputStream getOutputStream() {
		return new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				write(new byte[] {(byte) b});				
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				add(b, off, len);
			}
		};
	}

	public void remove(byte[] b) {
		remove(b, 0, b.length);
	}

	public void remove(byte[] b, int off, int len) {
		System.arraycopy(array, offset, b, off, len);
		remove(len);
	}

	public void remove(int len) {
		offset += len;
		length -= len;
	}

	public InputStream getInputStream() {
		return new InputStream() {
			@Override
			public int read() throws IOException {
				byte[] b = new byte[1];
				if (read(b) <= 0) {
					return -1;
				}
				return b[0] & 0xff;
			}

			@Override
			public int read(byte[] b, int off, int len) throws IOException {
				int bytesToRead = Math.min(len, length());
				remove(b, off, bytesToRead);
				return bytesToRead;
			}
		};
	}

	@Override
	public String toString() {
		return new String(array, offset, length);
	}

	public String toString(String charSet) throws UnsupportedEncodingException {
		return new String(array, offset, length, charSet);
	}
}

public class Echo {
	private static final int BUFFER_SIZE = 32768;

	static TrayIcon tray;

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static volatile boolean running = false;

	static ByteArrayQueue baq = new ByteArrayQueue();
	static AudioFormat af = new AudioFormat(44100, 16, 2, true, false);

	static MenuItem miStart = new MenuItem("Start");
	static JButton btnStart = new JButton("Start");
	static JButton btnExit = new JButton("Exit");
	static JSlider slider = new JSlider(0, 8, 0);
	static JLabel label = new JLabel("Delay 1 Second");

	static void input() {
		byte[] b = new byte[BUFFER_SIZE];
		TargetDataLine target;
		try {
			target = (TargetDataLine) AudioSystem.getLine(new Info(TargetDataLine.class, af));
			target.open();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		target.start();
		while (running) {
			int bytesRead = target.read(b, 0, BUFFER_SIZE);
			synchronized (baq) {
				baq.add(b, 0, bytesRead);
			}
		}
		target.stop();
		target.close();
		synchronized (baq) {
			baq.clear();
		}
	}

	static void output() {
		byte[] b = new byte[BUFFER_SIZE];
		int delay = slider.getValue();
		SourceDataLine source;
		try {
			source = (SourceDataLine) AudioSystem.getLine(new Info(SourceDataLine.class, af));
			source.open();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		source.start();
		while (running) {
			int bytesRead = 0;
			synchronized (baq) {
				if (baq.length() > BUFFER_SIZE * 5 * delay) {
					bytesRead = Math.min(baq.length(), BUFFER_SIZE);
					baq.remove(b, 0, BUFFER_SIZE);
				}
			}
			if (bytesRead == 0) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {/**/}
			} else {
				source.write(b, 0, bytesRead);
			}
		}
		tray.setToolTip("Echo");
		source.stop();
		source.close();
	}

	static void start() {
		running = true;
		new Thread() {
			@Override
			public void run() {
				input();
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				output();
			}
		}.start();
		tray.setToolTip("Echo (" + label.getText() + ")");
		miStart.setLabel("Stop");
		btnStart.setText("Stop");
		slider.setEnabled(false);
	}

	static void stop() {
		running = false;
		miStart.setLabel("Start");
		btnStart.setText("Start");
		slider.setEnabled(true);
	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame("Echo");
		frame.setSize(320, 60);
		frame.setResizable(false);

		MenuItem miOpen = new MenuItem("Open"), miExit = new MenuItem("Exit");
		Font font = btnStart.getFont();
		miOpen.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
		miStart.setFont(font);
		miExit.setFont(font);
        PopupMenu popup = new PopupMenu();
        popup.add(miOpen);
        popup.add(miStart);
		popup.add(miExit);
		InputStream in = Echo.class.getResourceAsStream("/EchoIcon16.gif");
		try {
			tray = new TrayIcon(ImageIO.read(in), "Echo", popup);
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tray.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					SystemTray.getSystemTray().remove(tray);
					frame.setVisible(true);		
					frame.setState(Frame.NORMAL);
				}
			}
		});

		miOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SystemTray.getSystemTray().remove(tray);
				frame.setVisible(true);		
				frame.setState(Frame.NORMAL);
			}
		});
		miStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (running) {
					stop();
				} else {
					start();
				}
			}
		});
        miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SystemTray.getSystemTray().remove(tray);
				frame.dispose();
			}
		});

        frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				if (SystemTray.isSupported()) {
					frame.setVisible(false);
					try {
						SystemTray.getSystemTray().add(tray);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				running = false;
			}
		});
		InputStream in16 = Echo.class.getResourceAsStream("/EchoIcon16.gif");
		InputStream in32 = Echo.class.getResourceAsStream("/EchoIcon32.gif");
		InputStream in48 = Echo.class.getResourceAsStream("/EchoIcon48.gif");
		try {
			frame.setIconImages(Arrays.asList(new Image[] {ImageIO.read(in16),
					ImageIO.read(in32), ImageIO.read(in48)}));
			in16.close();
			in32.close();
			in48.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Insets insets = new Insets(0, 0, 0, 0);
		KeyAdapter ka = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					frame.dispose();
				}
			}
		};

		label.setBounds(100, 5, 110, 25);
		slider.setBounds(5, 5, 90, 25);
		slider.setSnapToTicks(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int delay = slider.getValue();
				label.setText("Delay " + (delay + 1) +
						" Second" + (delay > 0 ? "s" : ""));
			}
		});
		slider.addKeyListener(ka);

		btnStart.setBounds(205, 5, 50, 25);
		btnStart.setMargin(insets);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (running) {
					stop();
				} else {
					start();
				}
			}
		});
		btnStart.addKeyListener(ka);

		btnExit.setBounds(260, 5, 50, 25);
		btnExit.setMargin(insets);
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnExit.addKeyListener(ka);

		final JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(slider);
		panel.add(label);
		panel.add(btnStart);
		panel.add(btnExit);

		frame.setContentPane(panel);
		frame.setVisible(true);
	}
}