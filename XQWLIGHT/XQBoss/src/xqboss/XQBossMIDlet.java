/*
XQBossMIDlet.java - Source Code for XiangQi Boss, Part V

XiangQi Boss - a Chinese Chess PGN File Reader for Java ME
Designed by Morning Yellow, Version: 1.0, Last Modified: Jun. 2008
Copyright (C) 2004-2008 www.elephantbase.net

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package xqboss;

import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

public class XQBossMIDlet extends MIDlet {
	private static Image imgFolder, imgPgn;

	static {
		try {
			imgFolder = Image.createImage("/images/folder.png");
			imgPgn = Image.createImage("/images/pgn_s.png");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private boolean started = false;

	private XQBossCanvas canvas = new XQBossCanvas(this);

	String currDir = null;

	void openFile(String file) {
		FileConnection dir = null;
		try {
			dir = (FileConnection) Connector.open("file://localhost/" +
					currDir + "/" + file);
			GBLineInputStream in = new GBLineInputStream(dir.openInputStream());
			PgnFile pgn = new PgnFile(in);
			in.close();
			dir.close();
			canvas.load(pgn, file);
			Display.getDisplay(this).setCurrent(canvas);
		} catch (Exception e) {
			if (dir != null) {
				try {
					dir.close();
				} catch (Exception ee) {
					// Ignored
				}
			}
		}
	}

	void chDir(String strDir) {
		if (strDir.equals("..")) {
			// __ASSERT(currDir != null);
			int i = currDir.lastIndexOf('/');
			if (i < 0) {
				currDir = null;
			} else {
				currDir = currDir.substring(0, i);
			}
		} else {
			if (currDir == null) {
				currDir = strDir;
			} else {
				currDir += "/" + strDir;
			}
		}
	}

	void listDir() {
		final Command cmdOpen = new Command("��", Command.OK, 1);
		final Command cmdExit = new Command("�˳�", Command.EXIT, 1);
		final List lstDir = new List("����С��ʿ", Choice.IMPLICIT);

		Enumeration enumDir;
		if (currDir == null) {
			enumDir = FileSystemRegistry.listRoots();
		} else {
			FileConnection dir = null;
			try {
				dir = (FileConnection) Connector.open("file://localhost/" + currDir + "/");
				enumDir = dir.list();
			} catch (Exception e) {
				if (dir != null) {
					try {
						dir.close();
					} catch (Exception ee) {
						// Ignored
					}
				}
				return;
			}
			lstDir.append("[..]", imgFolder);
		}
		while (enumDir.hasMoreElements()) {
			String strDir = (String) enumDir.nextElement();
			if (strDir.endsWith("/")) {
				lstDir.append("[" + strDir.substring(0, strDir.length() - 1) + "]", imgFolder);
			} else if (strDir.toLowerCase().endsWith(".pgn")) {
				lstDir.append(strDir, imgPgn);
			}
		}
		lstDir.setSelectCommand(cmdOpen);
		lstDir.addCommand(cmdExit);
		lstDir.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c == cmdOpen) {
					final String selDir = lstDir.getString(lstDir.getSelectedIndex());
					if (selDir.startsWith("[") && selDir.endsWith("]")) {
						new Thread() {
							public void run() {
								chDir(selDir.substring(1, selDir.length() - 1));
								listDir();
							}
						}.start();
					} else {
						new Thread() {
							public void run() {
								openFile(selDir);
							}
						}.start();
					}
				} else if (c == cmdExit) {
					destroyApp(false);
					notifyDestroyed();
				}
			}
		});
		Display.getDisplay(this).setCurrent(lstDir);
	}

	protected void startApp() {
		if (started) {
			return;
		}
		started = true;
		listDir();
	}

	protected void pauseApp() {
		// Do Nothing
	}

	protected void destroyApp(boolean unc) {
		started = false;
	}
}