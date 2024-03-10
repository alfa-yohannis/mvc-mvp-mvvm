package test.mvi;

import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mvi.Model;
import mvi.View;

public class TestMVIApp {

	private static final String DATA_FILE = "data.csv";
	static final int NUMBER_OF_VIEWS = 100;
	static final int INCREMENT = 10;
	

	public static void main(String[] args) throws IOException {

		Files.deleteIfExists(Path.of(DATA_FILE));

		Files.writeString(Path.of(DATA_FILE),
				"iter,total_view,view_num,spin_view,time,memory" + System.lineSeparator(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE);

		for (int i = 1; i <= 15; i++) {
			performMeasurement(i);
		}

	}

	private static void performMeasurement(int iteration) {
		List<Integer> numberOfViewsList = new ArrayList<>();
		for (int i = 1; i <= NUMBER_OF_VIEWS + 1; i = INCREMENT + i) {
			numberOfViewsList.add(i);
		}

		List<View> viewList = new ArrayList<>();

		for (int numberOfViews : numberOfViewsList) {

			viewList.clear();
			System.gc();

			for (int iView = 1; iView <= numberOfViews; iView++) {
				// create the view and the model
				Model model = new Model(String.valueOf(((NUMBER_OF_VIEWS * 10) + iView)));
				View view = new View(model);
				view.setName(String.valueOf(iView));
				view.setTitle("View" + ((NUMBER_OF_VIEWS * 10) + iView));
				view.getContentPane().removeAll();
				view.setDefaultCloseOperation(View.DISPOSE_ON_CLOSE);
				viewList.add(view);

				// create the input and output spinners
				Map<JSpinner, JSpinner> spinnerPairs = new HashMap<JSpinner, JSpinner>();

				long[] startTimes = new long[iView];
				long[] startMemories = new long[iView];

				for (int iSpinner = 0; iSpinner < iView; iSpinner = iSpinner + 1) {

					JSpinner inputSpinner = new JSpinner();
					inputSpinner.setName("spinner" + ((numberOfViews * 10) + iSpinner));
					((JSpinner.DefaultEditor) inputSpinner.getEditor()).getTextField().setColumns(3);
					inputSpinner.setFont(new Font("Dialog", Font.BOLD, 32));
					view.getContentPane().add(inputSpinner);
					inputSpinner.addChangeListener(view.new JSpinnerChangeListenger());

					JSpinner outputSpinner = new JSpinner();
					outputSpinner.setEnabled(false);
					outputSpinner.setName("spinner" + ((numberOfViews * 10) + iSpinner) + "b");
					((JSpinner.DefaultEditor) outputSpinner.getEditor()).getTextField().setColumns(3);
					outputSpinner.setFont(new Font("Dialog", Font.BOLD, 32));
					view.getContentPane().add(outputSpinner);

					final int finalIView = iView;
					final int finalISpinner = iSpinner;
					outputSpinner.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent event) {
							Integer value = (Integer) outputSpinner.getValue();
							long delta = System.nanoTime() - startTimes[value - 2];
							long deltaMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
//									- startMemories[value - 2]
							;
//							System.out.println(deltaMemory);
							try {
								// "iter,total_view,view_num,spin_view,time,memory",
								List<String> output = Arrays
										.asList(new String[] { String.valueOf(iteration), String.valueOf(numberOfViews),
												String.valueOf(finalIView), String.valueOf(finalISpinner),
												String.valueOf(delta), String.valueOf(deltaMemory)

										});
								Files.writeString(Path.of(DATA_FILE),
										String.join(",", output) + System.lineSeparator(), StandardCharsets.UTF_8,
										StandardOpenOption.APPEND);
								System.out.println(String.join(",", output));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

					spinnerPairs.put(inputSpinner, outputSpinner);
				}

				view.setVisible(true);

				int i = 0;
				for (Entry<JSpinner, JSpinner> pair : spinnerPairs.entrySet()) {
					JSpinner inputSpinner = pair.getKey();
					startMemories[i] = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					startTimes[i] = System.nanoTime();
					inputSpinner.setValue(i + 1);
					i++;
				}

			}
			viewList.forEach(v -> v.dispose());
		}
	}

}
