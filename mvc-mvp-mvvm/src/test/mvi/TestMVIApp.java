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

	private static final String OUTPUT_FILE = "data.csv";
	static final int NUMBER_OF_VIEWS = 100;
	static final int NUMBER_OF_SPINNER = NUMBER_OF_VIEWS;
	static final int DIVIDER = 4;

	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();

		// delete the target data file if the file exists
		Files.deleteIfExists(Path.of(OUTPUT_FILE));

		// create a new target data file
		Files.writeString(Path.of(OUTPUT_FILE),
				"iter,view_total,view_num,spin_total,spin_num,time,memory" + System.lineSeparator(),
				StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		System.out.println("iter,view_total,view_num,spin_total,spin_num,time,memory");

		// perform performance measurement 12 times
		for (int i = 1; i <= 12; i++) {
			performMeasurement(i);
		}

		System.out.println(
				"Total Time: " + String.valueOf((System.currentTimeMillis() - startTime) / 60000) + " minutes");
	}

	/***
	 * The method to perform measurement.
	 * 
	 * @param iteration Number of iteration
	 */
	private static void performMeasurement(int iteration) {

		// create number of views array. In this experiment, it consists of four
		// categories: 1, 25, 50, 75, 100.
		List<Integer> viewTotalList = new ArrayList<>();
		viewTotalList.add(1);
		for (int multiplier1 = 1; multiplier1 <= DIVIDER; multiplier1++) {
			int val = (int) (NUMBER_OF_VIEWS / DIVIDER * multiplier1);
			viewTotalList.add(val);
		}

		for (int viewTotal : viewTotalList) {

			// create number of spinner pairs array. In this experiment, it consists of four
			// categories: 1, 25, 50, 75, 100.
			List<Integer> spinnerTotalList = new ArrayList<>();
			spinnerTotalList.add(1);
			for (int multiplier2 = 1; multiplier2 <= DIVIDER; multiplier2++) {
				int val = (int) (NUMBER_OF_SPINNER / DIVIDER * multiplier2);
				spinnerTotalList.add(val);
			}

			List<View> viewList = new ArrayList<>();

			for (int spinnerTotal : spinnerTotalList) {

				// clear views created in the previous iteration
				// and also do garbage collection
				viewList.clear();
				System.gc();

				Map<View, Map<JSpinner, JSpinner>> viewToSpinnerPairMap = new HashMap<View, Map<JSpinner, JSpinner>>();
				Map<View, List<Long>> viewToStartTimeRecordsMap = new HashMap<View, List<Long>>();

				for (int viewNumber = 1; viewNumber <= viewTotal; viewNumber++) {
					// create the view and the model
					Model model = new Model(String.valueOf(((NUMBER_OF_VIEWS * 10) + viewNumber)));
					View view = new View(model);
					view.setName(String.valueOf(viewNumber));
					view.setTitle("View" + ((NUMBER_OF_VIEWS * 10) + viewNumber));
					view.getContentPane().removeAll();
					view.setDefaultCloseOperation(View.DISPOSE_ON_CLOSE);
					viewList.add(view);

					// create the input and output spinners
					Map<JSpinner, JSpinner> spinnerPairs = new HashMap<JSpinner, JSpinner>();
					viewToSpinnerPairMap.put(view, spinnerPairs);

					List<Long> startTimeRecords = new ArrayList<>();
					for (int a = 0; a < spinnerTotal; a++) {
						startTimeRecords.add((long) 0);
					}
					viewToStartTimeRecordsMap.put(view, startTimeRecords);

					// create the input and output spinners.
					// the number of spinners is as many as the order of the view.
					// for example, if the current view is the 8th view, than the view contains
					// 8 input spinners and 8 output spinners
					for (int spinnerNumber = 0; spinnerNumber < spinnerTotal; spinnerNumber = spinnerNumber + 1) {

						JSpinner inputSpinner = new JSpinner();
						inputSpinner.setName("spinner" + ((viewTotal * 10) + spinnerNumber));
						((JSpinner.DefaultEditor) inputSpinner.getEditor()).getTextField().setColumns(3);
						inputSpinner.setFont(new Font("Dialog", Font.BOLD, 32));
						view.getContentPane().add(inputSpinner);
						inputSpinner.addChangeListener(view.new JSpinnerChangeListenger());

						JSpinner outputSpinner = new JSpinner();
						outputSpinner.setEnabled(false);
						outputSpinner.setName("spinner" + ((viewTotal * 10) + spinnerNumber) + "b");
						((JSpinner.DefaultEditor) outputSpinner.getEditor()).getTextField().setColumns(3);
						outputSpinner.setFont(new Font("Dialog", Font.BOLD, 32));
						view.getContentPane().add(outputSpinner);

						final int finalViewNumber = viewNumber;
						final int finalSpinnerNumber = spinnerNumber;
						outputSpinner.addChangeListener(new ChangeListener() {
							@Override
							public void stateChanged(ChangeEvent event) {

								// get the returned value
								// index is already increased two times: When the input spinner is changed
								// and when the value is retrieved from the model to be displayed in the output
								// spinner
								int indexAlreadyIncreasedByTwo = (Integer) outputSpinner.getValue();
								int originalIndex = indexAlreadyIncreasedByTwo - 2;
								// get the start time
								long startTime = startTimeRecords.get(originalIndex);
								// calculate the delta time
								long delta = System.nanoTime() - startTime;
								// calculate the in-usage memory
								long deltaMemory = (Runtime.getRuntime().totalMemory()
										- Runtime.getRuntime().freeMemory());

								try {
									// the order of the output string corresponds to the following order
									// ""iter,view_total,view_num,spin_total,spin_num,time,memory""
									List<String> output = Arrays.asList(new String[] { String.valueOf(iteration),
											String.valueOf(viewTotal), String.valueOf(finalViewNumber),
											String.valueOf(spinnerTotal), String.valueOf(finalSpinnerNumber),
											String.valueOf(delta), String.valueOf(deltaMemory) });

									// get the output string
									String outputString = String.join(",", output) + System.lineSeparator();
									// write the output string to the target file
									if (viewTotal > 1 && spinnerTotal > 1)
										Files.writeString(Path.of(OUTPUT_FILE), outputString, StandardCharsets.UTF_8,
												StandardOpenOption.APPEND);
									// print the output string to the screen
									System.out.print(outputString);

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});

						spinnerPairs.put(inputSpinner, outputSpinner);
					}

					view.setVisible(true);
				}

				for (Entry<View, Map<JSpinner, JSpinner>> viewToSpinnerPair : viewToSpinnerPairMap.entrySet()) {

					Map<JSpinner, JSpinner> spinnerPairs = viewToSpinnerPair.getValue();
					List<Long> startTimeRecords = viewToStartTimeRecordsMap.get(viewToSpinnerPair.getKey());

					// iterate through every input spinner and set its value
					int originalIndex = 0;
					for (Entry<JSpinner, JSpinner> pair : spinnerPairs.entrySet()) {
						JSpinner inputSpinner = pair.getKey();
						startTimeRecords.set(originalIndex, System.nanoTime());
						// update the spinner's value based on the index value and is increased by one
						inputSpinner.setValue(originalIndex + 1);
						originalIndex++;
					}
				}

				viewToSpinnerPairMap.clear();
				viewToStartTimeRecordsMap.clear();
				viewList.forEach(v -> v.dispose());
			}
		}
	}

}
