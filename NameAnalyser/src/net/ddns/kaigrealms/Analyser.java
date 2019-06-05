package net.ddns.kaigrealms;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

public class Analyser {
	public static void main(String[] args) throws FileNotFoundException {
		HashMap<String, Integer> names = getAllNames(args[0]);
		ArrayList<String> sorter = getNames(args[0]);

		if(!(args.length > 0)) {
			throw new IllegalArgumentException(); 
		}
		
		runGUI(args[0], names, sorter);
	}

	@SuppressWarnings("deprecation") // Yes, I know I could use another method, but this is the one I learned first
	private static void runGUI(String filename, HashMap<String, Integer> names, ArrayList<String> nameList) {
		JFrame frame = new JFrame("Name Manager");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 500);
		JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.setContentPane(pane);
		ScrollPaneLayout layout = new ScrollPaneLayout();
		frame.setLayout(layout);
		JPanel panel = new JPanel();
		BoxLayout boxer = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxer);

		for (int i = 0; i < nameList.size(); i++) {
			panel.add(createNameLine(nameList.get(i), names.get(nameList.get(i))));
			System.out.println("Added a name");
			pane.validate();
		}

		JButton temp = new JButton("Choose names");
		temp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Component[] components = panel.getComponents();
				int iterator = 0;
				for (int i = 0; i < components.length - 1; i++) {
					Component[] panelContents = ((JPanel) components[i]).getComponents();
					if (((JCheckBox) panelContents[0]).isSelected()) {
						// Add name to a new file
						try {
							addNameToFile(filename, ((JLabel) panelContents[1]).getText(),
									((JLabel) panelContents[3]).getText(), iterator);
							iterator++;
							System.out.println("Added name " + ((JLabel) panelContents[1]).getText());
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				frame.dispose();
			}

			private void addNameToFile(String filename, String text, String pop, int iterator) throws IOException {
				File file = new File(filename);
				file.createNewFile();
				
				Scanner scan = new Scanner(file);
				ArrayList<String> lines = new ArrayList<String>();
				while (scan.hasNextLine() && iterator > 0) {
					lines.add(scan.nextLine());
				}
				scan.close();

				if (!lines.contains(text + "," + pop)) {
					lines.add(text + "," + pop);
				}
				
				Collections.sort(lines);

				PrintStream ps = new PrintStream(file);
				for (int i = 0; i < lines.size(); i++) {
					ps.println(lines.get(i));
				}
				ps.close();
			}
		});

		JPanel panelThing = new JPanel();
		panelThing.add(temp);
		panel.add(panelThing);

		pane.getViewport().add(panel);
		frame.show();
	}

	private static JPanel createNameLine(String name, Integer desc) {
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);

		JCheckBox checker = new JCheckBox();
		JLabel box = new JLabel(name);
		JLabel spacer = new JLabel("    ");
		JLabel num = new JLabel(Integer.toString(desc));
		panel.add(checker);
		panel.add(box);
		panel.add(spacer);
		panel.add(num);

		return panel;
	}

	private static HashMap<String, Integer> getAllNames(String filename) throws FileNotFoundException {
		HashMap<String, Integer> names = new HashMap<String, Integer>();
		ArrayList<String> sorter = new ArrayList<String>();

		File nameFile = new File(filename); // The first file to read from that is standardised name,population

		Scanner scan = new Scanner(nameFile);
		while (scan.hasNextLine()) {
			String[] parts = scan.nextLine().split(",");
			sorter.add(parts[0]);
			names.put(parts[0], Integer.parseInt(parts[1]));
		}
		scan.close();

		Collections.sort(sorter);

		PrintStream ps = new PrintStream(nameFile);
		for (int i = 0; i < sorter.size(); i++) {
			ps.println(sorter.get(i) + "," + names.get(sorter.get(i)));
		}
		ps.close();

		return names;
	}

	public static ArrayList<String> getNames(String filename) throws FileNotFoundException {
		HashMap<String, Integer> names = new HashMap<String, Integer>();
		ArrayList<String> sorter = new ArrayList<String>();

		File nameFile = new File(filename);

		Scanner scan = new Scanner(nameFile);
		while (scan.hasNextLine()) {
			String[] parts = scan.nextLine().split(",");
			sorter.add(parts[0]);
			names.put(parts[0], Integer.parseInt(parts[1]));
		}
		scan.close();

		Collections.sort(sorter);

		PrintStream ps = new PrintStream(nameFile);
		for (int i = 0; i < sorter.size(); i++) {
			ps.println(sorter.get(i) + "," + names.get(sorter.get(i)));
		}
		ps.close();

		return sorter;
	}
}
